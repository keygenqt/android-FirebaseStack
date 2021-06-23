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

package com.keygenqt.firebasestack.ui.guest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.ui.theme.BlackLight
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme
import com.keygenqt.firebasestack.ui.theme.Purple700

@Composable
fun Login(
    upPress: () -> Unit = {},
    navigateToUserGraph: () -> Unit = {},
) {
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    val login = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.login_title),
                        color = LocalContentColor.current
                    )
                },
                navigationIcon = {
                    IconButton(onClick = upPress) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_navigate_up)
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            val padding = 16.dp
            Row {
                Column(
                    modifier = modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .background(MaterialTheme.colors.background)
                ) {
                    TextField(
                        value = login.value,
                        onValueChange = {
                            login.value = it
                        },
                        label = { Text("Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(Modifier.size(padding))
                    TextField(
                        value = password.value,
                        onValueChange = {
                            password.value = it
                        },
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisibility = !passwordVisibility
                            }) {
                                Icon(
                                    imageVector = if (passwordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = ""
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(Modifier.size(padding))
                    Button(
                        onClick = navigateToUserGraph,
                        colors = ButtonDefaults.textButtonColors(backgroundColor = Purple700),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.welcome_btn_login),
                            color = Color.White,
                        )
                    }
                    Spacer(Modifier.size(padding))
                    Text(
                        text = stringResource(id = R.string.login_subtitle),
                        style = MaterialTheme.typography.subtitle2,
                    )
                    Spacer(Modifier.size(padding))
                    OutlinedButton(
                        onClick = {},
                        colors = ButtonDefaults.textButtonColors(backgroundColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.ic_google_original),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(24.dp)
                            )
                            Column(
                                modifier = Modifier.width(200.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    style = MaterialTheme.typography.subtitle1,
                                    text = stringResource(id = R.string.login_google),
                                    color = BlackLight,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.size(padding))
                    OutlinedButton(
                        onClick = {},
                        colors = ButtonDefaults.textButtonColors(backgroundColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.ic_github_original),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(24.dp)
                            )
                            Column(
                                modifier = Modifier.width(200.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    style = MaterialTheme.typography.subtitle1,
                                    text = stringResource(id = R.string.login_github),
                                    color = BlackLight,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.size(padding))
                    OutlinedButton(
                        onClick = {},
                        colors = ButtonDefaults.textButtonColors(backgroundColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.ic_facebook_original),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(24.dp)
                            )
                            Column(
                                modifier = Modifier.width(200.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    style = MaterialTheme.typography.subtitle1,
                                    text = stringResource(id = R.string.login_facebook),
                                    color = BlackLight,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun LoginPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        Login()
    }
}

@Preview
@Composable
fun LoginPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        Login()
    }
}