package com.example.core_database.dao

import androidx.room.*
import com.example.core_database.model.AuthorEntity
import com.samples.nowinandroid_demo.core.database.dao.upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorDao {
    @Query(value = """
        SELECT * FROM authors
        where id = :authorId
    """)
    fun getAuthorEntityStream(authorId: String): Flow<AuthorEntity>

    @Query(value = "SELECT * FROM authors")
    fun getAuthorEntitiesStream(): Flow<List<AuthorEntity>>

    /**
     * Inserts [authorEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreAuthors(authorEntities: List<AuthorEntity>): List<Long>

    /**
     * Updates [entities] in the db that match the primary key, and no-ops if they don't
     */
    @Update
    suspend fun updateAuthors(entities: List<AuthorEntity>)

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Transaction
    suspend fun upsertAuthors(entities: List<AuthorEntity>) = upsert(
        items = entities,
        insertMany = ::insertOrIgnoreAuthors,
        updateMany = ::updateAuthors
    )

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    @Query(
        value = """
            DELETE FROM authors
            WHERE id in (:ids)
        """
    )
    suspend fun deleteAuthors(ids: List<String>)
}