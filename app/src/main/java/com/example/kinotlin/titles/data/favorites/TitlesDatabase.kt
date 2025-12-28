package com.example.kinotlin.titles.data.favorites

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteTitleEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class TitlesDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}
