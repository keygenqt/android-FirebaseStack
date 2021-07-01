package com.keygenqt.firebasestack.ui.user.components

sealed class EventsEditProfile {

    data class Update(
        val email: String,
        val first_name: String,
        val last_name: String
    ) : EventsEditProfile()

    object NavigateBack : EventsEditProfile()
}