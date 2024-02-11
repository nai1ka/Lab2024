package ru.ndevelop.tinkofflab2024

import android.app.Application
import androidx.room.Room
import ru.ndevelop.tinkofflab2024.models.AppDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Repository.createDatabase(this)
    }
}