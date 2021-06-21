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

package com.keygenqt.firebasestack.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme

@Composable
fun FirstPage() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (constraintLayout) = createRefs()
        ConstraintLayout(
            modifier = Modifier
                .background(Color.Transparent)
                .constrainAs(constraintLayout) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "First page",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(30.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ErrorConnectPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        FirstPage()
    }
}

@Preview
@Composable
fun ErrorConnectPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        FirstPage()
    }
}