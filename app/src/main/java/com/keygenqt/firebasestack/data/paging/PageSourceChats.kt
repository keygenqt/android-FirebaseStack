/*
 * Copyright 2021 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package com.keygenqt.firebasestack.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.keygenqt.firebasestack.base.DatabaseChild
import com.keygenqt.firebasestack.data.models.ModelChat
import com.keygenqt.firebasestack.utils.ConstantsPaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class PageSourceChats(
    private val reference: DatabaseReference
) : PagingSource<String, ModelChat>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ModelChat> {
        val query = params.key ?: ""
        delay(500) // For simulate long work
        return reference
            .child(DatabaseChild.CHAT.name)
            .orderByKey()
            .startAfter(query)
            .limitToFirst(ConstantsPaging.PER_PAGE)
            .get()
            .await()
            .children
            .map { it.getValue<ModelChat>()!! }
            .toList()
            .let { models ->
                LoadResult.Page(
                    data = models,
                    prevKey = if (query.isBlank()) null else query,
                    nextKey = if (models.isEmpty()) null else models.last().name
                )
            }
    }

    override fun getRefreshKey(state: PagingState<String, ModelChat>): String {
        return ""
    }
}