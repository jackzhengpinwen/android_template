/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.core_data.repository.fake

import com.example.core_common.Dispatcher
import com.example.core_common.NiaDispatchers
import com.example.core_data.Synchronizer
import com.example.core_data.repository.AuthorsRepository
import com.example.core_network.fake.FakeDataSource
import com.example.core_network.model.NetworkAuthor
import com.samples.nowinandroid_demo.core.model.data.Author
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Fake implementation of the [AuthorsRepository] that returns hardcoded authors.
 *
 * This allows us to run the app with fake data, without needing an internet connection or working
 * backend.
 */
class FakeAuthorsRepository @Inject constructor(
    @Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
) : AuthorsRepository {

    override fun getAuthorsStream(): Flow<List<Author>> = flow {
        emit(
            networkJson.decodeFromString<List<NetworkAuthor>>(FakeDataSource.authors).map {
                Author(
                    id = it.id,
                    name = it.name,
                    imageUrl = it.imageUrl,
                    twitter = it.twitter,
                    mediumPage = it.mediumPage,
                    bio = it.bio,
                )
            }
        )
    }
        .flowOn(ioDispatcher)

    override fun getAuthorStream(id: String): Flow<Author> {
        return getAuthorsStream().map { it.first { author -> author.id == id } }
    }

    override suspend fun syncWith(synchronizer: Synchronizer) = true
}
