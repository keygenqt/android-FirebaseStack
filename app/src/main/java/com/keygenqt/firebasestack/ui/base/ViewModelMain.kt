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

package com.keygenqt.firebasestack.ui.base

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelMain @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _isReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> get() = _isReady

    private val _isLogin: MutableStateFlow<Boolean> = MutableStateFlow(auth.currentUser != null)
    val isLogin: StateFlow<Boolean> get() = _isLogin

    private val _showSnackBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showSnackBar: StateFlow<Boolean> get() = _showSnackBar

    init {
        Handler(Looper.getMainLooper()).postDelayed({ // Simulate work for splash
            _isReady.value = true
        }, 1000)
    }

    fun startUser() {
        _isLogin.value = true
    }

    fun logout() {
        _isLogin.value = false
        auth.signOut()
    }

    fun isShowSnackBar(): Boolean {
        return _showSnackBar.value
    }

    fun toggleSnackBar() {
        _showSnackBar.tryEmit(true)
        Handler(Looper.getMainLooper()).postDelayed({
            _showSnackBar.tryEmit(false)
        }, 1500)
    }
}
