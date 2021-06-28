package com.keygenqt.firebasestack.ui.guest.components

sealed class EventsLogin {
    data class LoginPassword(val email: String, val password: String) : EventsLogin()
    object LoginGoogle : EventsLogin()
    object LoginGitHub : EventsLogin()
    object LoginFacebook : EventsLogin()
    object NavigateBack : EventsLogin()
}