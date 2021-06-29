package com.keygenqt.firebasestack.ui.common.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme

/**
 * To be removed when [TextField]s support error
 */
@Composable
fun BoxTextFieldError(
    modifier: Modifier = Modifier,
    textError: String = "Box Text Field Error Preview"
) {
    Card(
        elevation = 4.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.error)
        ) {
            Text(
                text = textError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Preview
@Composable
fun BoxTextFieldErrorPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        BoxTextFieldError()
    }
}

@Preview
@Composable
fun BoxTextFieldErrorPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        BoxTextFieldError()
    }
}