package ru.ndevelop.tinkofflab2024.ui.aboutMovie

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import ru.ndevelop.tinkofflab2024.data.ApiClient
import ru.ndevelop.tinkofflab2024.data.CacheManager
import ru.ndevelop.tinkofflab2024.data.Repository
import ru.ndevelop.tinkofflab2024.databinding.FragmentAboutMovieBinding
import ru.ndevelop.tinkofflab2024.models.Movie
import java.net.URL


class AboutMovieFragment : Fragment() {

    private var _binding: FragmentAboutMovieBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutMovieBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.shimmerPreview.apply {
            visibility = View.VISIBLE
            startShimmer()
        }

        val filmId = arguments?.getInt(ARG_INT_VALUE)

        binding.errorLayout.btnRetry.setOnClickListener {
            loadFilmInfo(filmId)
        }
        loadFilmInfo(filmId)
        return root
    }

    fun showFilmInfo(movie: Movie) {
        binding.dataLayout.visibility = View.VISIBLE
        binding.shimmerLayout.visibility = View.GONE

        binding.errorLayout.llError.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
        binding.tvMovieName.text = movie.nameRu
        binding.tvMovieDescription.text = movie.description
        val genresString =
            SpannableString("Жанры: " + movie.genres.joinToString { it.genre ?: "" })

        val genreTitleLen = 6
        genresString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            genreTitleLen,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvGenres.text = genresString

        val countriesString =
            SpannableString("Страны: " + movie.countries.joinToString { it.country ?: "" })

        val countryTitleLen = 7
        countriesString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            countryTitleLen,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvCountries.text = countriesString

        if (movie.poster != null) {
            binding.ivMoviePoster.setImageBitmap(movie.poster)
            binding.shimmerPreview.visibility = View.GONE
            return
        }
        if (movie.posterUrl != null) {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                try {
                    val url = URL(movie.posterUrl)
                    val imageData = url.readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                    movie.poster = bitmap
                    withContext(Dispatchers.Main) {
                        binding.shimmerPreview.apply {
                            visibility = View.GONE
                            stopShimmer()
                        }
                        binding.ivMoviePoster.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    fun handleError() {
        binding.dataLayout.visibility = View.GONE
        binding.shimmerLayout.visibility = View.GONE
        binding.scrollView.visibility = View.GONE
        binding.errorLayout.llError.visibility = View.VISIBLE
    }

    private fun loadFilmInfo(filmId: Int?) {


        if (filmId == -1 || filmId == null) {
            handleError()
            return
        }
        binding.dataLayout.visibility = View.GONE
        binding.shimmerLayout.visibility = View.VISIBLE
        Repository.getFilmAbout(filmId, onSuccess = {
            showFilmInfo(it)
        },
            onError = {
                handleError()
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_INT_VALUE = "filmID"
        fun newInstance(intValue: Int): AboutMovieFragment {
            val fragment = AboutMovieFragment()
            val args = Bundle().apply {
                putInt(ARG_INT_VALUE, intValue)
            }
            fragment.arguments = args
            return fragment
        }
    }



}
