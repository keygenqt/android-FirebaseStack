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

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.perf.FirebasePerformance
import com.keygenqt.firebasestack.models.ModelUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val commonError: StateFlow<String?> get() = _commonError

    private val _commonSuccess: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val commonSuccess: StateFlow<Boolean> get() = _commonSuccess

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _user: MutableStateFlow<ModelUser?> = MutableStateFlow(null)
    val user: StateFlow<ModelUser?> get() = _user

    init {
        val trace = performance.newTrace("init_user").apply {
            start()
        }
        firebaseAuth.currentUser?.let { user ->
            database.reference.child("users").child(user.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.getValue<ModelUser>()?.let { model ->
                        _user.value = ModelUser(email = user.email ?: "") + model
                    }
                    trace.stop()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    try {
                        throw databaseError.toException()
                    } catch (e: Exception) {
                        crashlytics.recordException(e)
                    }
                    trace.stop()
                }
            })
        }
    }

    fun updateProfile(email: String, fname: String, lname: String) {
        val trace = performance.newTrace("update_profile").apply {
            start()
        }
        _commonError.value = null
        _loading.value = true
        firebaseAuth.currentUser?.let { user ->
            database.reference.child("users").child(user.uid).setValue(
                ModelUser(
                    email = email,
                    first_name = fname,
                    last_name = lname
                )
            ).addOnSuccessListener {
                _loading.value = false
                _commonSuccess.value = true
                Handler(Looper.getMainLooper()).postDelayed({
                    _commonSuccess.value = false
                }, 1500)
                trace.stop()
            }.addOnFailureListener {
                _loading.value = false
                _commonError.value = it.message
                trace.stop()
            }
        }
    }
}