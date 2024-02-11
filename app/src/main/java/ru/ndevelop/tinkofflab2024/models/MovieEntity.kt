package ru.ndevelop.tinkofflab2024.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
class MovieEntity(
    @PrimaryKey val filmId: Int,
    @ColumnInfo(name = "nameRu") val nameRu: String?,
    @ColumnInfo(name = "genre") val genre: String?,
    @ColumnInfo(name = "year") val year: String?,
) {
    constructor(movie: Movie) : this(
        movie.filmId,
        movie.nameRu,
        movie.genres.first().genre ?: "",
        movie.year
    ) {
    }
}