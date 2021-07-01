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
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.ui.guest.components.EventsWelcome
import com.keygenqt.firebasestack.ui.theme.BlackLight
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme
import com.keygenqt.firebasestack.ui.theme.Purple700

@Composable
fun Welcome(
    onNavigationEvent: (EventsWelcome) -> Unit = {},
) {
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
                    .weight(0.6f)
                    .fillMaxWidth()
            ) {
                val (image) = createRefs()
                Image(
                    painter = painterResource(R.drawable.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
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
                    .weight(0.25f)
                    .fillMaxWidth()
                    .padding(30.dp)
            ) {
                val (row) = createRefs()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(row) {
                            bottom.linkTo(parent.bottom)
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Column {
                        Button(
                            onClick = { onNavigationEvent(EventsWelcome.ToLogin) },
                            colors = ButtonDefaults.textButtonColors(backgroundColor = Purple700),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(id = R.string.welcome_btn_login),
                                color = Color.White,
                            )
                        }

                        Spacer(modifier = Modifier.size(20.dp))

                        OutlinedButton(
                            onClick = { onNavigationEvent(EventsWelcome.ToRegistration) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(id = R.string.welcome_btn_registration),
                            )
                        }
                    }
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