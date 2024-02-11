package ru.ndevelop.tinkofflab2024.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDAO {
    @Query("SELECT * FROM movies")
    fun getAll(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieEntity: MovieEntity)

    @Query("DELETE FROM movies WHERE filmId = :filmId")
    fun deleteMovie(filmId: Int)

    @Query("SELECT EXISTS (SELECT * FROM movies WHERE filmId = :filmId)")
    fun isFavourite(filmId: Int): Boolean

}