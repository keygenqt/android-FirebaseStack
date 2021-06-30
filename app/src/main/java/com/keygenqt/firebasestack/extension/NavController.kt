package com.keygenqt.firebasestack.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import com.keygenqt.firebasestack.base.LocalBaseViewModel

@Composable
fun NavController.AddFirebaseAnalyticsPage(navGraph: String) {
    val localBaseViewModel = LocalBaseViewModel.current
    DisposableEffect(this) {
        val callback = NavController.OnDestinationChangedListener { controller, _, _ ->
            controller.currentDestination?.route?.let { name ->
                localBaseViewModel.analyticsCurrentRoute(navGraph, name)
            }
        }
        addOnDestinationChangedListener(callback)
        // remove the navController on dispose (i.e. when the composable is destroyed)
        onDispose {
            removeOnDestinationChangedListener(callback)
        }
    }
}