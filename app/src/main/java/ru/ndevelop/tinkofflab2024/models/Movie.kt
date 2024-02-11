package ru.ndevelop.tinkofflab2024.models

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


class Movie(
    var filmId: Int,
    val nameRu: String?,
    val genres: List<Genre>,
    val year: String?,
    val posterUrl: String? = null,
    val posterUrlPreview: String? = null,
    val description: String = "",
    val countries: List<Country> = listOf(),
    var poster: Bitmap? = null

    ) {
    var isFavourite = false
    val genre: String
        get() = genres.first().genre ?: ""

    constructor(movieEntity: MovieEntity) : this(
        movieEntity.filmId,
        movieEntity.nameRu,
        listOf(Genre(movieEntity.genre)),
        movieEntity.year

    )


}