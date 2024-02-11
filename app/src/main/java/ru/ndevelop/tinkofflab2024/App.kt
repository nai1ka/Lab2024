package ru.ndevelop.tinkofflab2024

import android.app.Application
import ru.ndevelop.tinkofflab2024.data.LocalRepository
import ru.ndevelop.tinkofflab2024.data.WebRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LocalRepository.createDatabase(this)
        WebRepository.applicationContext = this
    }
}