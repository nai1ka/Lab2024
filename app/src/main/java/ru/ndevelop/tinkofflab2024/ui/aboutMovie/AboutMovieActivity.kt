package ru.ndevelop.tinkofflab2024.ui.aboutMovie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.ndevelop.tinkofflab2024.R
import ru.ndevelop.tinkofflab2024.databinding.ActivityAboutMovieBinding

class AboutMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutMovieBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAboutMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        val filmId = intent.getIntExtra("filmId", -1)
        val aboutFilmFragment = AboutMovieFragment.newInstance(filmId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.about_film_container, aboutFilmFragment)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }


}