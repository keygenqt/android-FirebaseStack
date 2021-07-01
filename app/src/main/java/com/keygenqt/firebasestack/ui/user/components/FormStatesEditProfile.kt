package com.keygenqt.firebasestack.ui.user.components

import com.keygenqt.firebasestack.base.FormFieldState
import com.keygenqt.firebasestack.base.FormStates
import com.keygenqt.firebasestack.ui.common.form.states.EmailStateRequired
import com.keygenqt.firebasestack.ui.common.form.states.StateSimpleEditText

enum class FormStatesEditProfile(val state: FormFieldState) : FormStates {
    FieldFirstName(StateSimpleEditText()),
    FieldLastName(StateSimpleEditText()),
    FieldEmail(EmailStateRequired()),
}