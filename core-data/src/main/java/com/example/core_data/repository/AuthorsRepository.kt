package com.example.core_data.repository

import com.example.core_data.Syncable
import com.samples.nowinandroid_demo.core.model.data.Author
import kotlinx.coroutines.flow.Flow

interface AuthorsRepository: Syncable {
    /**
     * Gets the available Authors as a stream
     */
    fun getAuthorsStream(): Flow<List<Author>>

    /**
     * Gets data for a specific author
     */
    fun getAuthorStream(id: String): Flow<Author>
}