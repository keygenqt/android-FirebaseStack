package com.keygenqt.firebasestack.ui.base

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.keygenqt.firebasestack.R

@Composable
fun Loader(modifier: Modifier = Modifier) {
    val animationSpec = remember { LottieAnimationSpec.RawRes(R.raw.loading) }
    val animationState = rememberLottieAnimationState(autoPlay = true, repeatCount = 99)
    LottieAnimation(
        spec = animationSpec,
        animationState = animationState,
        modifier = modifier.fillMaxHeight()
    )
}