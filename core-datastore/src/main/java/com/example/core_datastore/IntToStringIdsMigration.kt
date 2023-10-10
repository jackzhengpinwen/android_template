package com.example.core_datastore

import androidx.datastore.core.DataMigration
import com.zpw.android_tempalte.core.datastore.UserPreferences
import com.zpw.android_tempalte.core.datastore.copy

/**
 * Migrates saved ids from [Int] to [String] types
 */
object IntToStringIdsMigration : DataMigration<UserPreferences> {

    override suspend fun cleanUp() = Unit

    override suspend fun migrate(currentData: UserPreferences): UserPreferences =
        currentData.copy {
            // Migrate topic ids
            followedTopicIds.clear()
            followedTopicIds.addAll(
                currentData.deprecatedIntFollowedTopicIdsList.map(Int::toString)
            )
            deprecatedIntFollowedTopicIds.clear()

            // Migrate author ids
            followedAuthorIds.clear()
            followedAuthorIds.addAll(
                currentData.deprecatedIntFollowedAuthorIdsList.map(Int::toString)
            )
            deprecatedIntFollowedAuthorIds.clear()

            // Mark migration as complete
            hasDoneIntToStringIdMigration = true
        }

    override suspend fun shouldMigrate(currentData: UserPreferences): Boolean =
        !currentData.hasDoneIntToStringIdMigration
}
