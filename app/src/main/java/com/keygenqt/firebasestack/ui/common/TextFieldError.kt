package com.keygenqt.firebasestack.ui.form

import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme

/**
 * To be removed when [TextField]s support error
 */
@Composable
fun TextFieldError(
    textError: String = "Text Field Error Preview"
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            style = LocalTextStyle.current.copy(
                color = MaterialTheme.colors.error,
                fontSize = 12.sp
            )
        )
    }
}

@Preview
@Composable
fun TextFieldErrorPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        TextFieldError()
    }
}

@Preview
@Composable
fun TextFieldErrorPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        TextFieldError()
    }
}