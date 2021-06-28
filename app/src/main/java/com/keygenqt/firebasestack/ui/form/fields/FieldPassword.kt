package com.keygenqt.firebasestack.ui.form.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.keygenqt.firebasestack.R
import com.keygenqt.firebasestack.base.FormFieldState
import com.keygenqt.firebasestack.ui.form.TextFieldError
import com.keygenqt.firebasestack.ui.form.states.PasswordState
import com.keygenqt.firebasestack.ui.theme.FirebaseStackTheme

@Composable
fun FieldPassword(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    state: FormFieldState = remember { PasswordState() },
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    var visibility: Boolean by remember { mutableStateOf(false) }
    TextField(
        enabled = !loading,
        value = state.text,
        onValueChange = { state.text = it },
        label = { Text(stringResource(id = R.string.form_password)) },
        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visibility = !visibility }) {
                Icon(
                    imageVector = if (visibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = ""
                )
            }
        },
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

@Preview
@Composable
fun PasswordPreviewLight() {
    FirebaseStackTheme(darkTheme = false) {
        FieldPassword()
    }
}

@Preview
@Composable
fun PasswordPreviewDark() {
    FirebaseStackTheme(darkTheme = true) {
        FieldPassword()
    }
}