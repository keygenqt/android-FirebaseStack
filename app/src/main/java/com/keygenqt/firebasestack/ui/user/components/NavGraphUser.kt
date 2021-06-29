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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.keygenqt.firebasestack.base.LocalBaseViewModel
import com.keygenqt.firebasestack.ui.user.compose.ChatList
import com.keygenqt.firebasestack.ui.user.compose.ChatView

@Composable
fun NavGraphUser(navController: NavHostController) {
    val localBaseViewModel = LocalBaseViewModel.current
    val actionsUser = remember(navController) {
        ActionsUser(navController)
    }
    ProvideWindowInsets {
        NavHost(navController = navController, startDestination = NavScreenUser.ChatList.route) {
            composable(NavScreenUser.ChatList.route) {
                ChatList { localBaseViewModel.logout() }
            }
            composable(
                route = NavScreenUser.ChatView.routeWithArgument,
                arguments = listOf(navArgument(NavScreenUser.ChatView.argument0) { type = NavType.LongType })
            ) { backStackEntry ->
                backStackEntry.arguments?.let {
                    ChatView(
                        it.getLong(NavScreenUser.ChatView.argument0),
                        upPress = actionsUser.upPress
                    )
                }
            }
        }
    }
}


