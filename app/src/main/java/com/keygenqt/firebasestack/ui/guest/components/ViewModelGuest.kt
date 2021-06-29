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

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelGuest @Inject constructor() : ViewModel() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val _commonError: MutableStateFlow<String?> = MutableStateFlow(null)
    val commonError: StateFlow<String?> get() = _commonError

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    fun registration(fname: String, lname: String, email: String, password: String, success: () -> Unit) {
        _commonError.value = null
        _loading.value = true
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                success.invoke()
                _loading.value = false
            } else {
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
                success.invoke()
                _loading.value = false
            } else {
                _commonError.value = it.exception?.message ?: "Authentication failed"
                _loading.value = false
            }
        }
    }

    fun loginGoogle(idToken: String?, success: () -> Unit) {
        idToken?.let {
            _commonError.value = null
            _loading.value = true
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    success.invoke()
                    _loading.value = false
                } else {
                    _commonError.value = it.exception?.message ?: "Registration failed"
                    _loading.value = false
                }
            }
        } ?: run {
            _commonError.value = "Registration failed"
        }
    }
}
