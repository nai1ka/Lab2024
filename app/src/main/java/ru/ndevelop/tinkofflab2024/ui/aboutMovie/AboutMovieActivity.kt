package ru.ndevelop.tinkofflab2024.ui.aboutMovie

import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import ru.ndevelop.tinkofflab2024.ApiClient
import ru.ndevelop.tinkofflab2024.Repository
import ru.ndevelop.tinkofflab2024.databinding.ActivityAboutMovieBinding
import ru.ndevelop.tinkofflab2024.models.Movie
import ru.ndevelop.tinkofflab2024.models.Response
import java.net.URL

class AboutMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutMovieBinding
    private val retrofitClient = ApiClient.apiService
    private lateinit var movie: Movie
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAboutMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shimmerPreview.apply {
            visibility = View.VISIBLE
            startShimmer()
        }

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.errorLayout.btnRetry.setOnClickListener {

        }

        val intent = intent
        val filmId = intent.getIntExtra("filmId", -1)
        if (filmId == -1) {
            handleError()
        } else {
            loadFilmInfo(filmId)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    fun showFilmInfo(movie: Movie) {
        binding.errorLayout.llError.visibility = View.GONE
        binding.tvMovieName.text = movie.nameRu

        binding.tvMovieDescription.text = movie.description
        val spannableString =
            SpannableString("Жанры: " + movie.genres.joinToString { it.genre ?: "" })

        val genreTitleLen = 6
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            genreTitleLen,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvGenres.text = spannableString

        if (movie.posterUrl != null) {

            val scope = CoroutineScope(Dispatchers.Default)
            // Launch a new coroutine in the scope
            scope.launch {
                try {
                    val url = URL(movie.posterUrl)
                    val imageData = url.readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                    withContext(Dispatchers.Main) {
                        binding.shimmerPreview.apply {
                            visibility = View.GONE
                            stopShimmer()
                        }
                        binding.ivMoviePoster.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    //TODO check
                    println(e.message)
                }

            }


        }
    }

    fun handleError() {
        binding.errorLayout.llError.visibility = View.VISIBLE

    }

    fun loadFilmInfo(filmId: Int) {
        retrofitClient.getFilmInfo(filmId).enqueue(object : Callback<Movie> {
            override fun onResponse(
                call: Call<Movie>,
                response: retrofit2.Response<Movie>
            ) {
                movie = response.body()!!
                showFilmInfo(movie)
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                handleError()
            }
        })
    }

}