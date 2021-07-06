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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.base.FormFieldsState
import com.keygenqt.firebasestack.extension.visible
import com.keygenqt.firebasestack.ui.common.other.BoxTextFieldError
import com.keygenqt.firebasestack.ui.common.form.fields.FieldEmail
import com.keygenqt.firebasestack.ui.common.form.fields.FieldPassword
import com.keygenqt.firebasestack.ui.common.form.fields.FieldSimpleEditText
import com.keygenqt.firebasestack.ui.common.other.Loader
import com.keygenqt.firebasestack.ui.guest.components.EventsRegistration
import com.keygenqt.firebasestack.ui.guest.components.FormStatesRegistration
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme


@ExperimentalComposeUiApi
@Composable
fun Registration(
    loading: Boolean = false,
    commonError: String? = null,
    onNavigationEvent: (EventsRegistration) -> Unit = {},
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberScrollState()

    val formFields = FormFieldsState().apply {
        add(FormStatesRegistration.FieldFirstName, remember { FormStatesRegistration.FieldFirstName.state })
        add(FormStatesRegistration.FieldLastName, remember { FormStatesRegistration.FieldLastName.state })
        add(FormStatesRegistration.FieldEmail, remember { FormStatesRegistration.FieldEmail.state })
        add(FormStatesRegistration.FieldPassword, remember { FormStatesRegistration.FieldPassword.state })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.registration_title),
                        color = LocalContentColor.current
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigationEvent(EventsRegistration.NavigateBack) }) {
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
                        .padding(start = padding, end = padding)
                        .background(MaterialTheme.colors.background)
                        .verticalScroll(listState)
                ) {

                    Spacer(modifier = Modifier.size(16.dp))

                    // common error
                    commonError?.let {
                        BoxTextFieldError(textError = it)
                        Spacer(Modifier.size(padding))
                        LaunchedEffect(commonError) { listState.animateScrollTo(0) }
                    }

                    FormRegistration(
                        loading = loading,
                        formFields = formFields,
                        onNavigationEvent = onNavigationEvent,
                        softwareKeyboardController = softwareKeyboardController
                    )

                }
            }
        },
    )
}


@ExperimentalComposeUiApi
@Composable
fun FormRegistration(
    loading: Boolean = false,
    onNavigationEvent: (EventsRegistration) -> Unit = {},
    formFields: FormFieldsState = FormFieldsState(),
    softwareKeyboardController: SoftwareKeyboardController? = null,
    spacer: @Composable () -> Unit = { Spacer(modifier = Modifier.size(16.dp)) }
) {

    val lnameRequester = remember { FocusRequester() }
    val emailRequester = remember { FocusRequester() }
    val passwRequester = remember { FocusRequester() }

    // click submit
    val submitClick = {
        // validate before send
        formFields.validate()
        // check has errors
        if (!formFields.hasErrors()) {
            // submit query
            onNavigationEvent(
                EventsRegistration.Registration(
                    first_name = formFields.get(FormStatesRegistration.FieldFirstName).getValue(),
                    last_name = formFields.get(FormStatesRegistration.FieldLastName).getValue(),
                    email = formFields.get(FormStatesRegistration.FieldEmail).getValue(),
                    password = formFields.get(FormStatesRegistration.FieldPassword).getValue(),
                )
            )
            // hide keyboard
            softwareKeyboardController?.hide()
        }
    }

    Text(
        text = stringResource(id = R.string.registration_subtitle_info),
        style = MaterialTheme.typography.subtitle2,
    )

    spacer()

    FieldSimpleEditText(
        labelText = R.string.form_fname,
        enabled = !loading,
        state = formFields.get(FormStatesRegistration.FieldFirstName),
        imeAction = ImeAction.Next,
        keyboardActions = KeyboardActions(onNext = { lnameRequester.requestFocus() })
    )

    spacer()

    FieldSimpleEditText(
        modifier = Modifier.focusRequester(lnameRequester),
        labelText = R.string.form_lname,
        enabled = !loading,
        state = formFields.get(FormStatesRegistration.FieldLastName),
        imeAction = ImeAction.Next,
        keyboardActions = KeyboardActions(onNext = { emailRequester.requestFocus() })
    )

    spacer()

    Text(
        text = stringResource(id = R.string.registration_subtitle_credentials),
        style = MaterialTheme.typography.subtitle2,
    )

    spacer()

    FieldEmail(
        modifier = Modifier.focusRequester(emailRequester),
        enabled = !loading,
        state = formFields.get(FormStatesRegistration.FieldEmail),
        imeAction = ImeAction.Next,
        keyboardActions = KeyboardActions(onNext = { passwRequester.requestFocus() })
    )

    spacer()

    FieldPassword(
        modifier = Modifier.focusRequester(passwRequester),
        enabled = !loading,
        state = formFields.get(FormStatesRegistration.FieldPassword),
        imeAction = ImeAction.Done,
        keyboardActions = KeyboardActions(onDone = { submitClick.invoke() })
    )

    spacer()

    Button(
        enabled = !loading,
        onClick = { submitClick.invoke() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.common_form_button),
            color = Color.White,
        )
    }

    spacer()
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun RegistrationPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        Registration()
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun RegistrationPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        Registration()
    }
}