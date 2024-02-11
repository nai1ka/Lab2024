package ru.ndevelop.tinkofflab2024

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import ru.ndevelop.tinkofflab2024.databinding.ActivityMainBinding
import ru.ndevelop.tinkofflab2024.ui.SearchableFragment
import ru.ndevelop.tinkofflab2024.ui.aboutMovie.AboutMovieActivity
import ru.ndevelop.tinkofflab2024.ui.aboutMovie.AboutMovieFragment
import ru.ndevelop.tinkofflab2024.ui.favourite.FavouriteListFragment
import ru.ndevelop.tinkofflab2024.ui.popular.MovieListFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var selectedFragment: SearchableFragment
    private lateinit var favouriteListFragment: FavouriteListFragment
    private lateinit var popularListFragment: MovieListFragment
    private var selectedTab = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            selectedTab = savedInstanceState.getInt("selectedTab", 0)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        popularListFragment = MovieListFragment()
        popularListFragment.onMovieClickListener = {
            openInfoAboutFilm(it)
        }
        favouriteListFragment = FavouriteListFragment()
        favouriteListFragment.onMovieClickListener = {
            openInfoAboutFilm(it)
        }
        selectTab()
        supportActionBar?.elevation = 0F
        binding.btnPopular.setOnClickListener {
            selectedTab = 0
            selectTab()
        }
        binding.btnFavourite.setOnClickListener {
            selectedTab = 1
            selectTab()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("selectedTab", selectedTab)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        selectedTab = savedInstanceState.getInt("selectedTab", 0)
        selectTab()
    }

    private fun selectTab() {
        supportActionBar?.title = if (selectedTab == 0) "Популярные" else "Избранное"
        selectedFragment = if (selectedTab == 0) popularListFragment else favouriteListFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment)
            .commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null) return true
                // Pass the search query to the fragment
                selectedFragment.onSearchTextChanged(newText)
                return true
            }
        })


        return true
    }

    private fun openInfoAboutFilm(filmId: Int) {
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val myIntent = Intent(
                this,
                AboutMovieActivity::class.java
            )
            myIntent.putExtra("filmId", filmId)
            startActivity(myIntent)
        } else {
            val aboutMovieFragment = AboutMovieFragment.newInstance(filmId)
            supportFragmentManager.beginTransaction()
                .replace(R.id.about_film_container, aboutMovieFragment)
                .commit()
        }

    }
}