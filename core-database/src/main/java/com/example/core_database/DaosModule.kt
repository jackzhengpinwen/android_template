package com.example.core_database

import com.example.core_database.dao.AuthorDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesAuthorDao(
        database: NiaDatabase,
    ): AuthorDao = database.authorDao()
}