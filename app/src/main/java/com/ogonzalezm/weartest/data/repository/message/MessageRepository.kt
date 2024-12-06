package com.ogonzalezm.weartest.data.repository.message

import com.ogonzalezm.weartest.data.domain.Message
import kotlinx.coroutines.flow.StateFlow

interface MessageRepository {
    val messages: StateFlow<List<Message>>
    suspend fun getMessages(): StateFlow<List<Message>>
    suspend fun addMessage(text: String, author: String)
    suspend fun responseMessage()
}