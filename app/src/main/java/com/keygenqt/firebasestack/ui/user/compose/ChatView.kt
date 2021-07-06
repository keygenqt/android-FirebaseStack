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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.data.models.ModelChat
import com.keygenqt.firebasestack.data.models.ModelUser
import com.keygenqt.firebasestack.ui.common.other.CommonLoading
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme

@Composable
fun ChatView(
    chat: ModelChat?,
    user: ModelUser?,
    upPress: () -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = chat?.name?.let { stringResource(id = R.string.chat_view_title_name, it) }
                            ?: stringResource(id = R.string.chat_view_title),
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
        content = {
            ConstraintLayout {
                val (body) = createRefs()

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .constrainAs(body) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    if (chat == null || user == null) {
                        CommonLoading(visibility = true)
                    } else {

                        val (bodyView) = createRefs()

                        Row(
                            modifier = Modifier
                                .constrainAs(bodyView) {
                                    bottom.linkTo(parent.bottom)
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.chat_view_creator),
                                    modifier = Modifier
                                )
                                ClickableText(
                                    style = TextStyle(textDecoration = TextDecoration.Underline),
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                color = MaterialTheme.colors.primary,
                                            )
                                        ) {
                                            append(user.email)
                                        }
                                    },
                                    modifier = Modifier
                                ) {
                                    uriHandler.openUri("mailto:${user.email}")
                                }
                                Text(
                                    text = stringResource(id = R.string.chat_view_text),
                                    color = MaterialTheme.colors.secondary,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .padding(top = 20.dp)
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
fun ChatViewPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        ChatView(ModelChat.mock(), ModelUser.mock())
    }
}

@Preview
@Composable
fun ChatViewPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        ChatView(ModelChat.mock(), ModelUser.mock())
    }
}