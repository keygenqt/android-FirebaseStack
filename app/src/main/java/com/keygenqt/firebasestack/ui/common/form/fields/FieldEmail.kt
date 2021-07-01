package com.keygenqt.firebasestack.ui.common.form.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.base.FormFieldState
import com.keygenqt.firebasestack.ui.common.form.TextFieldError
import com.keygenqt.firebasestack.ui.common.form.states.EmailStateRequired
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme

@ExperimentalComposeUiApi
@Composable
fun FieldEmail(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    state: FormFieldState = remember { EmailStateRequired() },
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions(),
) {
    TextField(
        maxLines = 1,
        singleLine = true,
        enabled = enabled,
        value = state.text,
        onValueChange = { state.text = it },
        label = { Text(stringResource(id = R.string.form_email)) },
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.body2,
        isError = state.hasErrors,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = keyboardActions
    )

    state.getError(LocalContext.current)?.let { error ->
        TextFieldError(textError = error)
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun EmailPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        FieldEmail()
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun EmailPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        FieldEmail()
    }
}