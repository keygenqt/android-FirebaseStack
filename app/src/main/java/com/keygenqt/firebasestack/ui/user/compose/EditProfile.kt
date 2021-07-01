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

package com.keygenqt.firebasestack.ui.user.compose

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
import com.keygenqt.firebasestack.models.ModelUser
import com.keygenqt.firebasestack.ui.common.base.Loader
import com.keygenqt.firebasestack.ui.common.form.fields.FieldEmail
import com.keygenqt.firebasestack.ui.common.form.fields.FieldSimpleEditText
import com.keygenqt.firebasestack.ui.common.other.BoxTextFieldError
import com.keygenqt.firebasestack.ui.common.other.BoxTextFieldSuccess
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme
import com.keygenqt.firebasestack.ui.user.components.EventsEditProfile
import com.keygenqt.firebasestack.ui.user.components.FormStatesEditProfile


@ExperimentalComposeUiApi
@Composable
fun EditProfile(
    user: ModelUser? = ModelUser.mock(),
    loading: Boolean = false,
    commonError: String? = null,
    commonSuccess: Boolean = false,
    onNavigationEvent: (EventsEditProfile) -> Unit = {},
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberScrollState()

    val formFields = FormFieldsState().apply {
        add(FormStatesEditProfile.FieldFirstName, remember { FormStatesEditProfile.FieldFirstName.state }, user?.first_name)
        add(FormStatesEditProfile.FieldLastName, remember { FormStatesEditProfile.FieldLastName.state }, user?.last_name)
        add(FormStatesEditProfile.FieldEmail, remember { FormStatesEditProfile.FieldEmail.state }, user?.email)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.edit_profile_title),
                        color = LocalContentColor.current
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigationEvent(EventsEditProfile.NavigateBack) }) {
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

                    // common success
                    if (commonSuccess) {
                        BoxTextFieldSuccess()
                        Spacer(Modifier.size(padding))
                    }

                    FormEditProfile(
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
fun FormEditProfile(
    loading: Boolean = false,
    onNavigationEvent: (EventsEditProfile) -> Unit = {},
    formFields: FormFieldsState = FormFieldsState(),
    softwareKeyboardController: SoftwareKeyboardController? = null,
    spacer: @Composable () -> Unit = { Spacer(modifier = Modifier.size(16.dp)) }
) {

    val fnameRequester = remember { FocusRequester() }
    val lnameRequester = remember { FocusRequester() }

    // password login click submit
    val passwClick = {
        // validate before send
        formFields.validate()
        // check has errors
        if (!formFields.hasErrors()) {
            // submit query
            onNavigationEvent(
                EventsEditProfile.Update(
                    first_name = formFields.get(FormStatesEditProfile.FieldFirstName).text,
                    last_name = formFields.get(FormStatesEditProfile.FieldLastName).text,
                    email = formFields.get(FormStatesEditProfile.FieldEmail).text,
                )
            )
            // hide keyboard
            softwareKeyboardController?.hide()
        }
    }

    Text(
        text = stringResource(id = R.string.edit_profile_subtitle_required),
        style = MaterialTheme.typography.subtitle2,
    )

    spacer()

    FieldEmail(
        enabled = false,
        state = formFields.get(FormStatesEditProfile.FieldEmail),
        imeAction = ImeAction.Next,
        keyboardActions = KeyboardActions(onNext = { fnameRequester.requestFocus() })
    )

    spacer()

    Text(
        text = stringResource(id = R.string.edit_profile_subtitle_optional),
        style = MaterialTheme.typography.subtitle2,
    )

    spacer()

    FieldSimpleEditText(
        labelText = R.string.form_fname,
        enabled = !loading,
        state = formFields.get(FormStatesEditProfile.FieldFirstName),
        imeAction = ImeAction.Next,
        keyboardActions = KeyboardActions(onNext = { lnameRequester.requestFocus() })
    )

    spacer()

    FieldSimpleEditText(
        modifier = Modifier.focusRequester(lnameRequester),
        labelText = R.string.form_lname,
        enabled = !loading,
        state = formFields.get(FormStatesEditProfile.FieldLastName),
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

    spacer()
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun SettingsEditProfile_PreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        EditProfile()
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun SettingsEditProfile_PreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        EditProfile()
    }
}