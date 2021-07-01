package com.keygenqt.firebasestack.ui.user.components

sealed class EventsChatList {
    object ToEditProfile : EventsChatList()
    object Logout : EventsChatList()
}