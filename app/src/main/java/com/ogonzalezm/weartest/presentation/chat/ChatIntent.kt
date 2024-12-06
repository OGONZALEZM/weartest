package com.ogonzalezm.weartest.presentation.chat

sealed interface ChatIntent {
    data object ShowForm : ChatIntent
    data object ShowMessages: ChatIntent
    data class AddMessage(val text: String): ChatIntent
}