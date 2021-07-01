package com.keygenqt.firebasestack.ui.guest.components

import com.keygenqt.firebasestack.base.FormFieldState
import com.keygenqt.firebasestack.base.FormStates
import com.keygenqt.firebasestack.ui.common.form.states.EmailStateRequired
import com.keygenqt.firebasestack.ui.common.form.states.PasswordState
import com.keygenqt.firebasestack.ui.common.form.states.StateSimpleEditText

enum class FormStatesRegistration(val state: FormFieldState) : FormStates {
    FieldFirstName(StateSimpleEditText()),
    FieldLastName(StateSimpleEditText()),
    FieldEmail(EmailStateRequired()),
    FieldPassword(PasswordState()),
}