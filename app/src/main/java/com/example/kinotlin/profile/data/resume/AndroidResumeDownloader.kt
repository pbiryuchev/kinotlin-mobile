package com.example.kinotlin.profile.data.resume

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.webkit.URLUtil
import androidx.core.content.ContextCompat
import com.example.kinotlin.profile.domain.ResumeDownloader
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidResumeDownloader(
    private val context: Context,
) : ResumeDownloader {

    override suspend fun download(url: String): Uri? {
        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val sanitizedUrl = sanitizeUrl(url)
        val fileName = URLUtil.guessFileName(sanitizedUrl, null, null)

        val downloadId = runCatching {
            val request = DownloadManager.Request(Uri.parse(sanitizedUrl))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            if (Build.VERSION.SDK_INT >= 29) {
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            } else {
                request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
            }

            dm.enqueue(request)
        }.getOrNull() ?: return null

        return withTimeoutOrNull(120_000L) {
            suspendCancellableCoroutine { cont ->
                val receiver = object : BroadcastReceiver() {
                    override fun onReceive(ctx: Context?, intent: Intent?) {
                        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L) ?: -1L
                        if (id != downloadId) return

                        val uri = runCatching {
                            val query = DownloadManager.Query().setFilterById(downloadId)
                            dm.query(query)?.use { cursor ->
                                if (!cursor.moveToFirst()) return@use null

                                val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                                val status = if (statusIndex >= 0) cursor.getInt(statusIndex) else DownloadManager.STATUS_FAILED
                                if (status != DownloadManager.STATUS_SUCCESSFUL) return@use null

                                dm.getUriForDownloadedFile(downloadId)
                            }
                        }.getOrNull()

                        if (cont.isActive) cont.resume(uri)

                        runCatching { context.unregisterReceiver(this) }
                    }
                }

                ContextCompat.registerReceiver(
                    context,
                    receiver,
                    IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                    ContextCompat.RECEIVER_NOT_EXPORTED,
                )

                cont.invokeOnCancellation {
                    runCatching { context.unregisterReceiver(receiver) }
                }
            }
        }
    }

    private fun sanitizeUrl(url: String): String {
        return url.trim()
            .replace("'", "%27")
            .replace(" ", "%20")
    }
}
