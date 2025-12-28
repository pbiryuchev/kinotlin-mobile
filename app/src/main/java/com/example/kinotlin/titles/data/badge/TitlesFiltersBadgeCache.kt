package com.example.kinotlin.titles.data.badge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.kinotlin.titles.domain.TitlesFilter

class TitlesFiltersBadgeCache {
    var shouldShowBadge by mutableStateOf(false)
        private set

    fun update(filter: TitlesFilter) {
        shouldShowBadge = filter != TitlesFilter()
    }
}
