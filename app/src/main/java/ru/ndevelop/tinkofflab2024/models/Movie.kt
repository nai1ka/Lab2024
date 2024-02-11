package ru.ndevelop.tinkofflab2024.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


class Movie(
    val filmId: Int,
    val nameRu: String?,
    val genres: List<Genre>,
    val year: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val description: String = ""

)  {
    var isFavourite = false
    val genre: String
        get() = genres.first().genre ?: ""



    constructor(movieEntity: MovieEntity) : this(
        movieEntity.filmId,
        movieEntity.nameRu,
        listOf(Genre(movieEntity.genre)),
        movieEntity.year,
        null,
        null
    )


}