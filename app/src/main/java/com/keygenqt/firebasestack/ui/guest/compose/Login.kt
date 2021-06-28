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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.base.FormFieldsState
import com.keygenqt.firebasestack.extension.visible
import com.keygenqt.firebasestack.ui.base.Loader
import com.keygenqt.firebasestack.ui.form.BoxTextFieldError
import com.keygenqt.firebasestack.ui.form.fields.FieldEmail
import com.keygenqt.firebasestack.ui.form.fields.FieldPassword
import com.keygenqt.firebasestack.ui.form.states.FormStates.FieldEmail
import com.keygenqt.firebasestack.ui.form.states.FormStates.FieldPassword
import com.keygenqt.firebasestack.ui.guest.components.EventsLogin
import com.keygenqt.firebasestack.ui.theme.BlackLight
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme

@ExperimentalComposeUiApi
@Composable
fun Login(
    loading: Boolean = false,
    commonError: String? = null,
    onNavigationEvent: (EventsLogin) -> Unit = {},
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberScrollState()

    val formFields = FormFieldsState().apply {
        add(FieldEmail, remember { FieldEmail.state })
        add(FieldPassword, remember { FieldPassword.state })
    }

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
                    IconButton(onClick = { onNavigationEvent(EventsLogin.NavigateBack) }) {
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
                    // common error
                    commonError?.let {
                        BoxTextFieldError(textError = it)
                        Spacer(Modifier.size(padding))
                        LaunchedEffect(commonError) { listState.animateScrollTo(0) }
                    }

                    LoginBlockPassword(
                        loading = loading,
                        formFields = formFields,
                        onNavigationEvent = onNavigationEvent,
                        softwareKeyboardController = softwareKeyboardController
                    )


                    // social buttons
                    TitleSocialButtons {
                        Spacer(modifier = Modifier.size(16.dp))
                    }

                    SocialButtons(
                        loading = loading,
                        onNavigationEvent = { event ->
                            // clear error password login
                            formFields.clearError()
                            // click
                            onNavigationEvent(event)
                            // hide keyboard
                            softwareKeyboardController?.hide()
                        }
                    ) {
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        },
    )
}

@ExperimentalComposeUiApi
@Composable
fun LoginBlockPassword(
    loading: Boolean = false,
    onNavigationEvent: (EventsLogin) -> Unit = {},
    formFields: FormFieldsState = FormFieldsState(),
    softwareKeyboardController: SoftwareKeyboardController? = null,
    spacer: @Composable () -> Unit = { Spacer(modifier = Modifier.size(16.dp)) }
) {

    val passwRequester = remember { FocusRequester() }

    // password login click submit
    val passwClick = {
        // validate before send
        formFields.validate()
        // check has errors
        if (!formFields.hasErrors()) {
            // submit query
            onNavigationEvent(
                EventsLogin.LoginPassword(
                    email = formFields.get(FieldEmail).text,
                    password = formFields.get(FieldPassword).text,
                )
            )
            // hide keyboard
            softwareKeyboardController?.hide()
        }
    }

    FieldEmail(
        loading = loading,
        state = formFields.get(FieldEmail),
        imeAction = ImeAction.Next,
        keyboardActions = KeyboardActions(onNext = { passwRequester.requestFocus() })
    )

    spacer()

    FieldPassword(
        modifier = Modifier.focusRequester(passwRequester),
        loading = loading,
        state = formFields.get(FieldPassword),
        imeAction = ImeAction.Done,
        keyboardActions = KeyboardActions(onDone = { passwClick.invoke() })
    )

    spacer()

    Button(
        enabled = !loading,
        onClick = { passwClick.invoke() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.welcome_btn_login),
            color = Color.White,
        )
    }
}

@Composable
fun TitleSocialButtons(
    spacer: @Composable () -> Unit = { Spacer(modifier = Modifier.size(16.dp)) }
) {
    spacer()
    Text(
        text = stringResource(id = R.string.login_subtitle),
        style = MaterialTheme.typography.subtitle2,
    )
    spacer()
}

@Composable
fun SocialButtons(
    loading: Boolean = false,
    onNavigationEvent: (EventsLogin) -> Unit = {},
    spacer: @Composable () -> Unit = { Spacer(modifier = Modifier.size(16.dp)) }
) {
    OutlinedButton(
        enabled = !loading,
        onClick = { onNavigationEvent(EventsLogin.LoginGoogle) },
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
    spacer()
    OutlinedButton(
        enabled = !loading,
        onClick = { onNavigationEvent(EventsLogin.LoginGitHub) },
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
    spacer()
    OutlinedButton(
        enabled = !loading,
        onClick = { onNavigationEvent(EventsLogin.LoginFacebook) },
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

@ExperimentalComposeUiApi
@Preview
@Composable
fun LoginPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        Login()
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun LoginPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        Login()
    }
}