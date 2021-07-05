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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.base.FormFieldsState
import com.keygenqt.firebasestack.base.LocalBaseViewModel
import com.keygenqt.firebasestack.extension.visible
import com.keygenqt.firebasestack.models.ModelUser
import com.keygenqt.firebasestack.ui.common.form.fields.FieldSimpleEditText
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme
import com.keygenqt.firebasestack.ui.user.components.EventsChatList
import com.keygenqt.firebasestack.ui.user.components.FormStatesCreateChat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ChatList(
    user: ModelUser? = ModelUser.mock(),
    onNavigationEvent: (EventsChatList) -> Unit = {},
) {

    var expanded by remember { mutableStateOf(false) }
    val showDialogCreate = remember { mutableStateOf(false) }
    val nameRequester: FocusRequester = remember { FocusRequester() }
    val isShowSnackBar: Boolean by LocalBaseViewModel.current.showSnackBar.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = user?.nickname?.let { name ->
                            stringResource(id = R.string.chat_list_title_name, name)
                        } ?: run {
                            stringResource(id = R.string.chat_list_title)
                        },
                        color = LocalContentColor.current,
                        fontSize = 18.sp
                    )
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.TopEnd)
                        ) {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Options menu"
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {

                                DropdownMenuItem(onClick = { onNavigationEvent(EventsChatList.ToEditProfile) }) {
                                    Text(Firebase.remoteConfig.getString("string_menu_EditProfile"))
                                }

                                Divider(thickness = 1.dp)

                                DropdownMenuItem(onClick = { onNavigationEvent(EventsChatList.Logout) }) {
                                    Text("Logout")
                                }

                            }
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            ConstraintLayout {
                val (body, snackBarInfo, btn) = createRefs()
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(body) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Column(
                        modifier = modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                            .background(MaterialTheme.colors.background)
                    ) {
                        Text(text = "Will be list")
                    }
                }

                Snackbar(
                    modifier = Modifier
                        .visible(isShowSnackBar)
                        .padding(8.dp)
                        .constrainAs(snackBarInfo) {
                            bottom.linkTo(body.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Text(text = stringResource(id = R.string.common_exit))
                }

                FloatingActionButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .constrainAs(btn) {
                            bottom.linkTo(body.bottom)
                            end.linkTo(parent.end)
                        },
                    onClick = {
                        showDialogCreate.value = true
                    }
                ) {
                    Icon(Icons.Filled.Add, "Add")
                }
            }
        },
    )

    CreateNewChatDialog(
        onNavigationEvent = onNavigationEvent,
        openDialog = showDialogCreate,
        nameRequester = nameRequester,
    )
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun CreateNewChatDialog(
    onNavigationEvent: (EventsChatList) -> Unit = {},
    openDialog: MutableState<Boolean> = remember { mutableStateOf(false) },
    nameRequester: FocusRequester = remember { FocusRequester() },
) {
    if (openDialog.value) {

        val scope = rememberCoroutineScope()

        val formFields = FormFieldsState().apply {
            add(FormStatesCreateChat.FieldChatName, remember { FormStatesCreateChat.FieldChatName.state })
        }

        // click submit
        val submitClick = {
            // validate before send
            formFields.validate()
            // check has errors
            if (!formFields.hasErrors()) {
                openDialog.value = false
                onNavigationEvent(EventsChatList.ToChatView(formFields.get(FormStatesCreateChat.FieldChatName).getValue()))
            }
        }

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = stringResource(id = R.string.chat_create_chat_title))
            },
            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column {

                        FieldSimpleEditText(
                            modifier = Modifier.focusRequester(nameRequester),
                            labelText = R.string.form_name,
                            state = formFields.get(FormStatesCreateChat.FieldChatName),
                            imeAction = ImeAction.Done,
                            keyboardActions = KeyboardActions(onDone = { submitClick.invoke() })
                        )

                        val softwareKeyboardController = LocalSoftwareKeyboardController.current

                        DisposableEffect(Unit) {
                            // focus to name
                            nameRequester.requestFocus()
                            // load for open keyboard
                            scope.launch {
                                delay(300)
                                softwareKeyboardController?.show()
                            }
                            onDispose {
                                // clear error form
                                formFields.clearError()
                                // hide keyboard after close dialog
                                softwareKeyboardController?.hide()
                            }
                        }
                    }
                }

            },
            confirmButton = {
                Button(
                    onClick = { submitClick.invoke() }
                ) {
                    Text(text = stringResource(id = R.string.common_btn_confirm))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { openDialog.value = false },
                ) {
                    Text(text = stringResource(id = R.string.common_btn_cancel))
                }
            }
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview
@Composable
fun ChatListPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        ChatList()
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview
@Composable
fun ChatListPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        ChatList()
    }
}