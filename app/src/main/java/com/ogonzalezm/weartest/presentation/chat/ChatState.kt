package com.ogonzalezm.weartest.presentation.chat

import com.ogonzalezm.weartest.data.domain.Message

data class ChatState(
    val messages: List<Message> = listOf(),
    val viewState: ViewState = ViewState.Loading
)