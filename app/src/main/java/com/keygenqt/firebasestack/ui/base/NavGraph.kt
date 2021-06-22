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

package com.keygenqt.firebasestack.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.insets.ProvideWindowInsets
import com.keygenqt.firebasestack.ui.other.ChatList
import com.keygenqt.firebasestack.ui.other.Login
import com.keygenqt.firebasestack.ui.other.StartApp

@Composable
fun NavGraph(navController: NavHostController) {
    val actions = remember(navController) {
        Actions(navController)
    }
    ProvideWindowInsets {
        NavHost(navController = navController, startDestination = NavScreen.Welcome.route) {
            composable(NavScreen.Welcome.route) {
                StartApp(actions = actions)
            }
            composable(NavScreen.Login.route) {
                Login(actions.upPress, actions.navigateToChatList)
            }
            composable(NavScreen.ChatList.route) {
                ChatList()
            }
        }
    }
}


