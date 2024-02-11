package ru.ndevelop.tinkofflab2024.models

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): MovieDAO
}