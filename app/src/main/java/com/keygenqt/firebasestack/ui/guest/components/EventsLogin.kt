package com.keygenqt.firebasestack.ui.guest.components

sealed class EventsLogin {

    data class LoginPassword(
        val email: String,
        val password: String
    ) : EventsLogin()

    data class LoginGoogle(
        val email: String,
        val idToken: String
    ) : EventsLogin()

    object NavigateBack : EventsLogin()
}