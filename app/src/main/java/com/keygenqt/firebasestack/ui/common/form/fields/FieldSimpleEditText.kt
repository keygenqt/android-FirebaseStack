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

package com.keygenqt.firebasestack.ui.common.form.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.base.FormFieldState
import com.keygenqt.firebasestack.ui.common.form.TextFieldError
import com.keygenqt.firebasestack.ui.common.form.states.StateSimpleEditText
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme

@ExperimentalComposeUiApi
@Composable
fun FieldSimpleEditText(
    modifier: Modifier = Modifier,
    labelText: Int = R.string.form_edit_text,
    enabled: Boolean = true,
    state: FormFieldState = remember { StateSimpleEditText() },
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions(),
) {
    TextField(
        maxLines = 1,
        singleLine = true,
        enabled = enabled,
        value = state.text,
        onValueChange = { state.text = it },
        label = { Text(stringResource(id = labelText)) },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    state.positionToEnd()
                }
            },
        textStyle = MaterialTheme.typography.body2,
        isError = state.hasErrors,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = keyboardActions
    )

    state.getError(LocalContext.current)?.let { error ->
        TextFieldError(textError = error)
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun SimpleEditTextPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        FieldSimpleEditText()
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun SimpleEditTextPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        FieldSimpleEditText()
    }
}