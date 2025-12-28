package com.example.kinotlin.profile.domain

import android.net.Uri

class DownloadResumeUseCase(
    private val downloader: ResumeDownloader,
) {
    suspend operator fun invoke(url: String): Uri? = downloader.download(url)
}
