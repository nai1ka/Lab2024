package ru.ndevelop.tinkofflab2024

import androidx.fragment.app.Fragment

abstract class SearchableFragment: Fragment() {
   abstract fun onSearchTextChanged(text: String)
}