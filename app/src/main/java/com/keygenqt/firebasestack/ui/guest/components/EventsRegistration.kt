package com.keygenqt.firebasestack.ui.guest.components

sealed class EventsRegistration {

    data class Registration(
        val email: String,
        val password: String,
        val first_name: String,
        val last_name: String
    ) : EventsRegistration()

    object NavigateBack : EventsRegistration()
}