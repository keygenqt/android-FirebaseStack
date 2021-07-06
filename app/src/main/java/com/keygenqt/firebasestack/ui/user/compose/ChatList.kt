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

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.base.FormFieldsState
import com.keygenqt.firebasestack.base.LocalBaseViewModel
import com.keygenqt.firebasestack.data.models.ModelChat
import com.keygenqt.firebasestack.data.models.ModelUser
import com.keygenqt.firebasestack.ui.common.form.fields.FieldSimpleEditText
import com.keygenqt.firebasestack.ui.common.other.CommonList
import com.keygenqt.firebasestack.ui.user.components.EventsChatList
import com.keygenqt.firebasestack.ui.user.components.FormStatesCreateChat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ChatList(
    chatIsExist: Boolean = false,
    user: ModelUser? = ModelUser.mock(),
    models: LazyPagingItems<ModelChat>,
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
        content = {
            ConstraintLayout {
                val (body, btn, snackBarInfo) = createRefs()

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(body) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    CommonList(
                        modifier = Modifier.constrainAs(body) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        models = models,
                        state = rememberSwipeRefreshState(models.loadState.refresh is LoadState.Loading)
                    ) { index, model ->
                        ItemChat(
                            index = index,
                            model = model,
                            onNavigationEvent = onNavigationEvent
                        )
                    }
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

                if (isShowSnackBar) {
                    Snackbar(
                        modifier = Modifier
                            .padding(16.dp)
                            .constrainAs(snackBarInfo) {
                                bottom.linkTo(body.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    ) {
                        Text(text = stringResource(id = R.string.common_exit))
                    }
                }
            }
        },
    )

    CreateNewChatDialog(
        chatIsExist = chatIsExist,
        onNavigationEvent = onNavigationEvent,
        openDialog = showDialogCreate,
        nameRequester = nameRequester,
    )
}

@Composable
fun ItemChat(
    index: Int = 0,
    model: ModelChat,
    onNavigationEvent: (EventsChatList) -> Unit = {},
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable(
                onClick = {
                    onNavigationEvent(EventsChatList.ToChatView(model.name))
                }
            ),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    brush = listOf(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFBE5B02),
                                Color(0xFFB8A81D),
                                Color(0xffFFF9C4)
                            )
                        ),
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF009E3F),
                                Color(0xFF24A5AA),
                                Color(0xffFFF9C4)
                            )
                        ),
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF900ACA),
                                Color(0xFFD33441),
                                Color(0xffFFF9C4)
                            )
                        ),
                    )[index % 3]
                )
        ) {
            Column {
                Text(
                    text = model.name,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 24.dp, start = 18.dp, end = 18.dp)
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun CreateNewChatDialog(
    chatIsExist: Boolean = false,
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
                onNavigationEvent(EventsChatList.CreateChat(formFields.get(FormStatesCreateChat.FieldChatName).getValue()))
            }
        }

        if (chatIsExist) {
            formFields.get(FormStatesCreateChat.FieldChatName).setError { context: Context ->
                context.getString(R.string.is_exist)
            }
        }

        AlertDialog(
            modifier = Modifier
                .padding(16.dp),
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