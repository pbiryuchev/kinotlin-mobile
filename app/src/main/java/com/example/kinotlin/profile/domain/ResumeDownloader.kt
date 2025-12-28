package com.example.kinotlin.profile.domain

import android.net.Uri

interface ResumeDownloader {
    suspend fun download(url: String): Uri?
}
