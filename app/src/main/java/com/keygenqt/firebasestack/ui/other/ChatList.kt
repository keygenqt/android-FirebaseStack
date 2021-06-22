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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme

@Composable
fun ChatList() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.chat_list_title),
                        color = LocalContentColor.current
                    )
                }
            )
        },
        content = { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            Row {
                Column(
                    modifier = modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .background(MaterialTheme.colors.background)
                ) {
                    Text(text = stringResource(id = R.string.chat_list_title))
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