package ru.ndevelop.tinkofflab2024.data

import android.content.Context
import androidx.room.Room
import ru.ndevelop.tinkofflab2024.models.AppDatabase
import ru.ndevelop.tinkofflab2024.models.Movie
import ru.ndevelop.tinkofflab2024.models.MovieDAO
import ru.ndevelop.tinkofflab2024.models.MovieEntity

object LocalRepository {

    private lateinit var database: AppDatabase
    private lateinit var movieDAO: MovieDAO

    fun createDatabase(context: Context) {
        database =
            Room.databaseBuilder(context, AppDatabase::class.java, "my_database")
                .fallbackToDestructiveMigration()
                .build()
        movieDAO = database.userDao()
    }

    fun getListOfFavouriteMovies() =
        movieDAO.getAll()

    fun isFavourite(filmId: Int): Boolean {
        return movieDAO.isFavourite(filmId)
    }

    suspend fun inserMovie(movie: Movie) {
        movieDAO.insertMovie(MovieEntity(movie))
    }

    fun deleteMovie(filmId: Int) {
        movieDAO.deleteMovie(filmId)
    }


}