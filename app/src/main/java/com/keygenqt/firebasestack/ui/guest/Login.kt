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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.extension.visible
import com.keygenqt.firebasestack.ui.form.BoxTextFieldError
import com.keygenqt.firebasestack.ui.form.Email
import com.keygenqt.firebasestack.ui.form.EmailState
import com.keygenqt.firebasestack.ui.theme.BlackLight
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme
import com.keygenqt.firebasestack.ui.theme.Purple700

sealed class LoginEvent {
    data class LoginPassword(val email: String, val password: String) : LoginEvent()
    object LoginGoogle : LoginEvent()
    object LoginGitHub : LoginEvent()
    object LoginFacebook : LoginEvent()
    object NavigateBack : LoginEvent()
}

@Composable
fun Loader(modifier: Modifier = Modifier) {
    val animationSpec = remember { LottieAnimationSpec.RawRes(R.raw.loading) }
    val animationState = rememberLottieAnimationState(autoPlay = true, repeatCount = 99)
    LottieAnimation(
        spec = animationSpec,
        animationState = animationState,
        modifier = modifier.fillMaxHeight()
    )
}

@Composable
fun Login(
    loading: Boolean = false,
    commonError: String? = null,
    onNavigationEvent: (LoginEvent) -> Unit = {},
) {


    val focusRequester = remember { FocusRequester() }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    val password = remember { mutableStateOf(TextFieldValue("")) }


    val emailState = remember { EmailState() }
    val listState = rememberScrollState()

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
                    IconButton(onClick = { onNavigationEvent(LoginEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_navigate_up)
                        )
                    }
                },
                actions = {
                    Loader(Modifier.visible(loading))
                }
            )
        },
        content = { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            val padding = 16.dp
            Row {
                Column(
                    modifier = modifier
                        .padding(16.dp)
                        .background(MaterialTheme.colors.background)
                        .verticalScroll(listState)
                ) {
                    commonError?.let {
                        BoxTextFieldError(textError = it)
                        Spacer(Modifier.size(padding))
                        LaunchedEffect(commonError) { listState.animateScrollTo(0) }
                    }

                    Email(emailState, onImeAction = { focusRequester.requestFocus() })
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
                        onClick = { onNavigationEvent(LoginEvent.LoginPassword(emailState.text, "")) },
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
                        onClick = { onNavigationEvent(LoginEvent.LoginGoogle) },
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
                        onClick = { onNavigationEvent(LoginEvent.LoginGitHub) },
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
                        onClick = { onNavigationEvent(LoginEvent.LoginFacebook) },
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