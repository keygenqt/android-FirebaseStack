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
 
package com.keygenqt.firebasestack.initializer

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.keygenqt.firebasestack.R

class RemoteConfigInitializer : Initializer<FirebaseRemoteConfig> {

    override fun create(context: Context): FirebaseRemoteConfig {
        return Firebase.remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 30 else 3600
            })
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
