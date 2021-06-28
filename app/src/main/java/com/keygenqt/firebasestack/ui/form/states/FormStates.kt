package com.keygenqt.firebasestack.ui.form.states

import com.keygenqt.firebasestack.base.FormFieldState

enum class FormStates(val state: FormFieldState) {
    FieldEmail(EmailState()),
    FieldPassword(PasswordState()),
}