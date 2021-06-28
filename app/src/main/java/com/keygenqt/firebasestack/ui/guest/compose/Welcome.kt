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
 
package com.keygenqt.firebasestack.ui.guest.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.ui.theme.BlackLight
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme
import com.keygenqt.firebasestack.ui.theme.Purple700

@Composable
fun Welcome(modifier: Modifier = Modifier, navigateToLogin: () -> Unit = {}) {
    Row {
        Column(
            modifier = Modifier
                .background(BlackLight)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxWidth()
            ) {
                val (text) = createRefs()
                Text(
                    text = stringResource(id = R.string.welcome_title),
                    color = Color.White,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .constrainAs(text) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }
            ConstraintLayout(
                modifier = Modifier
                    .padding(top = 22.dp)
                    .weight(0.8f)
                    .fillMaxWidth()
            ) {
                val (image) = createRefs()
                Image(
                    painter = painterResource(R.drawable.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier
                        .width(160.dp)
                        .clip(CircleShape)
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }
            ConstraintLayout(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxWidth()
                    .padding(30.dp)
            ) {
                val (button) = createRefs()
                Button(
                    onClick = navigateToLogin,
                    colors = ButtonDefaults.textButtonColors(backgroundColor = Purple700),
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(button) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome_btn_login),
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun WelcomePreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        Welcome()
    }
}

@Preview
@Composable
fun WelcomePreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        Welcome()
    }
}