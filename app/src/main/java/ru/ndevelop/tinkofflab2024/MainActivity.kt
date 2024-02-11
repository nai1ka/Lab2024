package ru.ndevelop.tinkofflab2024

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import ru.ndevelop.tinkofflab2024.databinding.ActivityMainBinding
import ru.ndevelop.tinkofflab2024.ui.favourite.FavouriteListFragment
import ru.ndevelop.tinkofflab2024.ui.home.MovieListFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var selectedFragment: SearchableFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var movieListFragment = MovieListFragment()
        var favouriteListFragment = FavouriteListFragment()

        selectedFragment = movieListFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment)
            .commit()
        supportActionBar?.title = "Популярные"
        supportActionBar?.elevation = 0F
        binding.btnPopular.setOnClickListener {
            supportActionBar?.title = "Популярные"
            selectedFragment = movieListFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
        }
        binding.btnFavourite.setOnClickListener {
            supportActionBar?.title = "Избранное"
            selectedFragment = favouriteListFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
        }


    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks whether a keyboard is available
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "Landscape", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT).show()
        }
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
}