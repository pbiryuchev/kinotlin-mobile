package com.example.kinotlin.titles.data.favorites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorite_titles ORDER BY primary_title ASC")
    fun observeAll(): Flow<List<FavoriteTitleEntity>>

    @Query("SELECT * FROM favorite_titles ORDER BY primary_title ASC")
    suspend fun getAll(): List<FavoriteTitleEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_titles WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: FavoriteTitleEntity): Long

    @Query("DELETE FROM favorite_titles WHERE id = :id")
    suspend fun deleteById(id: String): Int
}
