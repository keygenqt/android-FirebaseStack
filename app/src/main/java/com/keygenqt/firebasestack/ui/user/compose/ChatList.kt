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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.base.LocalBaseViewModel
import com.keygenqt.firebasestack.extension.visible
import com.keygenqt.firebasestack.models.ModelUser
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme
import com.keygenqt.firebasestack.ui.user.components.EventsChatList


@Composable
fun ChatList(
    user: ModelUser? = ModelUser.mock(),
    onNavigationEvent: (EventsChatList) -> Unit = {},
) {

    var expanded by remember { mutableStateOf(false) }
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
                                Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
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
                val (body, snackBarInfo) = createRefs()
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(snackBarInfo) {
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
                        .constrainAs(body) {
                            bottom.linkTo(body.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Text(text = stringResource(id = R.string.common_exit))
                }
            }
        },
    )
}

@Preview
@Composable
fun ChatListPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        ChatList()
    }
}

@Preview
@Composable
fun ChatListPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        ChatList()
    }
}