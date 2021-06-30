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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.keygenqt.firebasestack.models.ModelUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelUser @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val crashlytics: FirebaseCrashlytics,
) : ViewModel() {

    private val _user: MutableStateFlow<ModelUser?> = MutableStateFlow(null)
    val user: StateFlow<ModelUser?> get() = _user

    init {
        firebaseAuth.currentUser?.let { user ->
            database.reference.child("users").child(user.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    _user.value = dataSnapshot.getValue<ModelUser>()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    try {
                        throw databaseError.toException()
                    } catch (e: Exception) {
                        crashlytics.recordException(e)
                    }
                }
            })
        }
    }
}