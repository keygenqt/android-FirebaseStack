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
 
package com.keygenqt.firebasestack.ui.common.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelMain @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val crashlytics: FirebaseCrashlytics,
    private val analytics: FirebaseAnalytics,
    remoteConfig: FirebaseRemoteConfig,
) : ViewModel() {

    private val _isReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> get() = _isReady

    private val _isLogin: MutableStateFlow<Boolean> = MutableStateFlow(firebaseAuth.currentUser != null)
    val isLogin: StateFlow<Boolean> get() = _isLogin

    private val _showSnackBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showSnackBar: StateFlow<Boolean> get() = _showSnackBar

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isReady.value = true
                } else {
                    throw RuntimeException("Fetch failed")
                }
            }
        }, 1) // For simulate long work for splash
    }

    fun startUser() {
        firebaseAuth.currentUser?.let { user ->
            _isLogin.value = true
            crashlytics.setUserId(user.uid)
        }
    }

    fun logout() {
        _isLogin.value = false
        crashlytics.setUserId("")
        firebaseAuth.signOut()
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

    fun analyticsCurrentRoute(navGraph: String, screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, navGraph)
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        })
    }
}
