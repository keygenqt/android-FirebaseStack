/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.firebasestack.base

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class FormFieldState(
    text: String = "",
    private val checkValid: (String) -> List<(Context) -> String> = { emptyList() }
) {

    private var _text: String by mutableStateOf(text)
    private var _errors: List<(Context) -> String> by mutableStateOf(listOf())

    var text: String
        get() = _text
        set(value) {
            _text = value
            _errors = checkValid(value)
        }

    val hasErrors: Boolean
        get() = _errors.isNotEmpty()

    fun validate() {
        _errors = checkValid(_text)
    }

    fun clearError() {
        _errors = emptyList()
    }

    fun getError(context: Context): String? {
        return _errors.firstOrNull()?.invoke(context)
    }
}
