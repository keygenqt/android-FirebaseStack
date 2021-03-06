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
 
package com.keygenqt.firebasestack.ui.user.components

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navDeepLink
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.insets.ProvideWindowInsets
import com.keygenqt.firebasestack.base.FirebaseMessaging
import com.keygenqt.firebasestack.base.LocalBaseViewModel
import com.keygenqt.firebasestack.data.models.ModelChat
import com.keygenqt.firebasestack.data.models.ModelUser
import com.keygenqt.firebasestack.extension.AddFirebaseAnalyticsPage
import com.keygenqt.firebasestack.ui.user.compose.ChatList
import com.keygenqt.firebasestack.ui.user.compose.ChatView
import com.keygenqt.firebasestack.ui.user.compose.EditProfile
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@Composable
fun NavGraphUser(navController: NavHostController) {

    val localBaseViewModel = LocalBaseViewModel.current

    val actionsUser = remember(navController) {
        ActionsUser(navController)
    }

    navController.AddFirebaseAnalyticsPage("NavGraphUser")

    ProvideWindowInsets {
        NavHost(navController = navController, startDestination = NavScreenUser.ChatList.route) {
            composable(
                route = NavScreenUser.ChatList.route,
                deepLinks = listOf(navDeepLink { uriPattern = "${FirebaseMessaging.DEEP_LINK_URI}/${NavScreenUser.ChatList.route}" })
            ) {
                val viewModel: ViewModelUser = hiltViewModel()
                val user: ModelUser? by viewModel.user.collectAsState()
                val chatIsExist: Boolean by viewModel.chatIsExist.collectAsState()
                val lazyChats: LazyPagingItems<ModelChat> = viewModel.chats.collectAsLazyPagingItems()

                ChatList(
                    user = user,
                    models = lazyChats,
                    chatIsExist = chatIsExist
                ) { event ->
                    when (event) {
                        is EventsChatList.ToEditProfile -> actionsUser.navigateToEditProfile.invoke()
                        is EventsChatList.ToChatView -> actionsUser.navigateToChatView.invoke(event.name)
                        is EventsChatList.CreateChat -> viewModel.createChat(
                            name = event.name,
                            success = {
                                lazyChats.refresh()
                                actionsUser.navigateToChatView.invoke(event.name)
                            }
                        )
                        is EventsChatList.Logout -> localBaseViewModel.logout()
                    }
                }
            }
            composable(
                route = NavScreenUser.EditProfile.route,
                deepLinks = listOf(navDeepLink { uriPattern = "${FirebaseMessaging.DEEP_LINK_URI}/${NavScreenUser.EditProfile.route}" })
            ) {

                val viewModel: ViewModelUser = hiltViewModel()

                val commonError: String? by viewModel.commonError.collectAsState()
                val commonSuccess: Boolean by viewModel.commonSuccess.collectAsState()
                val loading: Boolean by viewModel.loading.collectAsState()
                val user: ModelUser? by viewModel.user.collectAsState()

                EditProfile(user, loading, commonError, commonSuccess) { event ->
                    when (event) {
                        is EventsEditProfile.Update -> viewModel.updateProfile(event.email, event.first_name, event.last_name)
                        is EventsEditProfile.NavigateBack -> actionsUser.upPress.invoke()
                    }
                }
            }
            composable(
                route = NavScreenUser.ChatView.routeWithArgument,
                arguments = listOf(navArgument(NavScreenUser.ChatView.argument0) { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.let {

                    val viewModel: ViewModelChatView = hiltViewModel()

                    viewModel.setChatName(it.getString(NavScreenUser.ChatView.argument0))

                    val chat: ModelChat? by viewModel.chat.collectAsState()
                    val user: ModelUser? by viewModel.user.collectAsState()

                    ChatView(
                        chat = chat,
                        user = user,
                        upPress = actionsUser.upPress
                    )
                }
            }
        }
    }
}


