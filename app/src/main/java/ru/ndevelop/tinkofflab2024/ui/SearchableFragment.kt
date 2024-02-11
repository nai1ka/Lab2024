package ru.ndevelop.tinkofflab2024.ui

import androidx.fragment.app.Fragment

abstract class SearchableFragment: Fragment() {
   abstract fun onSearchTextChanged(text: String)
   abstract fun showNoDataInfo()
   abstract fun hideNoDataInfo()
}