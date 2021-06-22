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

package com.keygenqt.firebasestack.ui.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme
import com.keygenqt.firebasestack.ui.theme.Purple700
import timber.log.Timber

@Composable
fun Login(
    upPress: () -> Unit = {},
    navigateToChatList: () -> Unit = {},
) {
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
                            contentDescription = stringResource(R.string.cd_navigate_up)
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
                        value = "",
                        onValueChange = {
                            Timber.e(it) // @todo
                        },
                        label = { Text("Label") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(Modifier.size(padding))
                    TextField(
                        value = "",
                        onValueChange = {
                            Timber.e(it) // @todo
                        },
                        label = { Text("Label") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(Modifier.size(padding))
                    Button(
                        onClick = navigateToChatList,
                        colors = ButtonDefaults.textButtonColors(backgroundColor = Purple700),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.welcome_btn_login),
                            color = Color.White,
                        )
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