package com.keygenqt.firebasestack.ui.guest.components

sealed class EventsWelcome {
    object ToLogin : EventsWelcome()
    object ToRegistration : EventsWelcome()
}