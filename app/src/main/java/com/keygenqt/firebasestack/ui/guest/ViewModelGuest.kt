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

package com.keygenqt.firebasestack.ui.guest

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelGuest @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _commonError: MutableStateFlow<String?> = MutableStateFlow(null)
    val commonError: StateFlow<String?> get() = _commonError

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _loginSuccess: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> get() = _loginSuccess

    fun login(email: String, password: String, success: () -> Unit) {
        _commonError.value = null
        _loading.value = true
        Handler(Looper.getMainLooper()).postDelayed({ // Simulate query
            success.invoke()
            _loading.value = false
        }, 5000)


//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this, OnCompleteListener<AuthResult?> { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithEmail:success")
//                    val user: FirebaseUser = mAuth.getCurrentUser()
//                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithEmail:failure", task.exception)
//                    Toast.makeText(
//                        this@EmailPasswordActivity, "Authentication failed.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    updateUI(null)
//                }
//            })
    }

    fun loginGoogle() {
        _commonError.value = null
        _loading.value = true
        Handler(Looper.getMainLooper()).postDelayed({ // Simulate query
            _commonError.value = "Error login Google"
            _loading.value = false
        }, 5000)
    }

    fun loginGitHub() {
        _commonError.value = null
        _loading.value = true
        Handler(Looper.getMainLooper()).postDelayed({ // Simulate query
            _commonError.value = "Error login GitHub"
            _loading.value = false
        }, 5000)
    }

    fun loginFacebook() {
        _commonError.value = null
        _loading.value = true
        Handler(Looper.getMainLooper()).postDelayed({ // Simulate query
            _commonError.value = "Error login Facebook"
            _loading.value = false
        }, 5000)
    }
}
