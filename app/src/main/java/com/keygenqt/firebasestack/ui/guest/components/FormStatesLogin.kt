package com.keygenqt.firebasestack.ui.guest.components

import com.keygenqt.firebasestack.base.FormFieldState
import com.keygenqt.firebasestack.base.FormStates
import com.keygenqt.firebasestack.ui.common.form.states.EmailState
import com.keygenqt.firebasestack.ui.common.form.states.PasswordState

enum class FormStatesLogin(val state: FormFieldState) : FormStates {
    FieldEmail(EmailState()),
    FieldPassword(PasswordState()),
}