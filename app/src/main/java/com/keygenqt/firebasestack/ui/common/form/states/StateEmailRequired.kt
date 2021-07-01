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

package com.keygenqt.firebasestack.ui.common.form.states

import android.content.Context
import android.util.Patterns.EMAIL_ADDRESS
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.base.FormFieldState
import com.keygenqt.firebasestack.base.getErrorIsBlank

class EmailStateRequired : FormFieldState(checkValid = ::checkValid)

private fun checkValid(target: String) = listOfNotNull(
    getErrorIsBlank(target),
    getErrorIsNotEmail(target),
)

private fun getErrorIsNotEmail(target: String) = when {
    !EMAIL_ADDRESS.matcher(target).matches() -> { it: Context ->
        it.getString(R.string.is_incorrect)
    }
    else -> null
}