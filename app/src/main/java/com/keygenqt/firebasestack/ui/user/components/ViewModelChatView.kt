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

package com.keygenqt.firebasestack.ui.user.components

import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.keygenqt.firebasestack.base.DatabaseChild
import com.keygenqt.firebasestack.data.models.ModelChat
import com.keygenqt.firebasestack.data.models.ModelUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelChatView @Inject constructor(
    private val crashlytics: FirebaseCrashlytics,
    private val database: FirebaseDatabase,
) : ViewModel() {

    private val _chat: MutableStateFlow<ModelChat?> = MutableStateFlow(null)
    val chat: StateFlow<ModelChat?> get() = _chat.asStateFlow()

    private val _user: MutableStateFlow<ModelUser?> = MutableStateFlow(null)
    val user: StateFlow<ModelUser?> get() = _user.asStateFlow()

    fun setChatName(name: String?) {
        database.reference.child(DatabaseChild.CHAT.name).child(name!!).get()
            .addOnSuccessListener {
                it.getValue<ModelChat>()?.let { model ->
                    _chat.value = model
                    updateUser(model.userUid)
                }
            }
            .addOnFailureListener { databaseError ->
                try {
                    throw RuntimeException(databaseError.message)
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                }
            }
    }

    private fun updateUser(userUid: String) {
        database.reference.child(DatabaseChild.USERS.name).child(userUid).get()
            .addOnSuccessListener {
                it.getValue<ModelUser>()?.let { model ->
                    _user.value = model
                }
            }
            .addOnFailureListener { databaseError ->
                try {
                    throw RuntimeException(databaseError.message)
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                }
            }
    }
}