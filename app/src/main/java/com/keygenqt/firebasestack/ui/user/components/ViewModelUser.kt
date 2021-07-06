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
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.perf.FirebasePerformance
import com.keygenqt.firebasestack.base.DatabaseChild
import com.keygenqt.firebasestack.data.models.ModelChat
import com.keygenqt.firebasestack.data.models.ModelUser
import com.keygenqt.firebasestack.data.paging.PageSourceChats
import com.keygenqt.firebasestack.utils.ConstantsPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelUser @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val crashlytics: FirebaseCrashlytics,
    private val database: FirebaseDatabase,
    private val performance: FirebasePerformance,
) : ViewModel() {

    private val _commonError: MutableStateFlow<String?> = MutableStateFlow(null)
    val commonError: StateFlow<String?> get() = _commonError.asStateFlow()

    private val _commonSuccess: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val commonSuccess: StateFlow<Boolean> get() = _commonSuccess.asStateFlow()

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading.asStateFlow()

    private val _user: MutableStateFlow<ModelUser?> = MutableStateFlow(null)
    val user: StateFlow<ModelUser?> get() = _user.asStateFlow()

    private val _chatIsExist: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val chatIsExist: StateFlow<Boolean> get() = _chatIsExist.asStateFlow()

    val chats: Flow<PagingData<ModelChat>> = Pager(PagingConfig(pageSize = ConstantsPaging.PER_PAGE)) {
        PageSourceChats(database.reference)
    }.flow.cachedIn(viewModelScope)

    init {
        val trace = performance.newTrace("init_user").apply {
            start()
        }
        firebaseAuth.currentUser?.let { user ->
            database.reference.child(DatabaseChild.USERS.name).child(user.uid).get()
                .addOnSuccessListener {
                    it.getValue<ModelUser>()?.let { model ->
                        _user.value = model.copy(email = user.email ?: "")
                    }
                    trace.stop()
                }
                .addOnFailureListener { databaseError ->
                    try {
                        throw RuntimeException(databaseError.message)
                    } catch (e: Exception) {
                        crashlytics.recordException(e)
                    }
                    trace.stop()
                }
        }
    }

    fun updateProfile(email: String, fname: String, lname: String) {
        val trace = performance.newTrace("update_profile").apply {
            start()
        }
        _commonError.value = null
        _loading.value = true
        firebaseAuth.currentUser?.let { user ->
            database.reference.child(DatabaseChild.USERS.name).child(user.uid).setValue(
                ModelUser(
                    email = email,
                    first_name = fname,
                    last_name = lname
                )
            ).addOnSuccessListener {
                _loading.value = false
                _commonSuccess.value = true
                viewModelScope.launch {
                    delay(1500)
                    _commonSuccess.value = false
                }
                trace.stop()
            }.addOnFailureListener {
                _loading.value = false
                _commonError.value = it.message
                trace.stop()
            }
        }
    }

    fun createChat(name: String, success: () -> Unit) {
        _chatIsExist.value = false
        database.reference.child(DatabaseChild.CHAT.name).child(name).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    // error
                    _chatIsExist.value = true
                } else {
                    // create
                    firebaseAuth.currentUser?.let { user ->
                        database.reference.child(DatabaseChild.CHAT.name).child(name).setValue(
                            ModelChat(
                                name = name,
                                userUid = user.uid,
                            )
                        ).addOnSuccessListener {
                            success.invoke()
                        }
                    }
                }
            }
    }
}