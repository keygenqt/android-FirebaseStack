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
 
package com.keygenqt.firebasestack.ui.common.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme

/**
 * To be removed when [TextField]s support error
 */
@Composable
fun BoxTextFieldError(
    modifier: Modifier = Modifier,
    textError: String = "Box Text Field Error Preview"
) {
    Card(
        elevation = 4.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.error)
        ) {
            Text(
                text = textError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Preview
@Composable
fun BoxTextFieldErrorPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        BoxTextFieldError()
    }
}

@Preview
@Composable
fun BoxTextFieldErrorPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        BoxTextFieldError()
    }
}