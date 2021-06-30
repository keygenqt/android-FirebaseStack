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

package com.keygenqt.firebasestack.ui.guest.components

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.keygenqt.firebasestack.models.ModelUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelGuest @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val crashlytics: FirebaseCrashlytics,
    private val analytics: FirebaseAnalytics,
    private val database: FirebaseDatabase,
) : ViewModel() {

    private val _commonError: MutableStateFlow<String?> = MutableStateFlow(null)
    val commonError: StateFlow<String?> get() = _commonError

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    fun registration(fname: String, lname: String, email: String, password: String, success: () -> Unit) {
        _commonError.value = null
        _loading.value = true
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                // analytics
                analyticsLogin("Password")
                // database
                it.result.user?.let { user ->
                    database.reference.child("users").child(user.uid).setValue(
                        ModelUser(
                            first_name = fname,
                            last_name = lname
                        )
                    )
                }
                // change navGraph
                success.invoke()
                // disable animation loading
                _loading.value = false
            } else {
                try {
                    throw it.exception!!
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                }
                _commonError.value = it.exception?.message ?: "Registration failed"
                _loading.value = false
            }
        }
    }

    fun login(email: String, password: String, success: () -> Unit) {
        _commonError.value = null
        _loading.value = true
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                // analytics
                analyticsLogin("Password")
                // change navGraph
                success.invoke()
                // disable animation loading
                _loading.value = false
            } else {
                try {
                    throw it.exception!!
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                }
                _commonError.value = it.exception?.message ?: "Authentication failed"
                _loading.value = false
            }
        }
    }

    fun loginGoogle(email: String, idToken: String?, success: () -> Unit) {
        _commonError.value = null
        _loading.value = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                // analytics
                analyticsLogin("Google")
                // database
                it.result.user?.let { user ->
                    database.reference.child("users").child(user.uid).setValue(
                        ModelUser(
                            email = email,
                        )
                    )
                }
                // change navGraph
                success.invoke()
                // disable animation loading
                _loading.value = false
            } else {
                try {
                    throw it.exception!!
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                }
                _commonError.value = it.exception?.message ?: "Authentication failed"
                _loading.value = false
            }
        }
    }

    private fun analyticsLogin(method: String) {
        analytics.logEvent(FirebaseAnalytics.Event.LOGIN, Bundle().apply {
            putString(FirebaseAnalytics.Param.METHOD, method)
        })
    }
}
