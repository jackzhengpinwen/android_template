package com.example.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core_database.dao.AuthorDao
import com.example.core_database.model.AuthorEntity

@Database(
    entities = [
        AuthorEntity::class
    ],
    version = 1
)
abstract class NiaDatabase: RoomDatabase() {
    abstract fun authorDao(): AuthorDao
}