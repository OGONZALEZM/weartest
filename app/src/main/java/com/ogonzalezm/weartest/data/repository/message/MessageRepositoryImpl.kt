package com.ogonzalezm.weartest.data.repository.message

import android.content.Context
import com.ogonzalezm.weartest.R
import com.ogonzalezm.weartest.data.domain.Message
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
): MessageRepository {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    override val messages = _messages.asStateFlow()

    override suspend fun getMessages(): StateFlow<List<Message>> {
        return withContext(Dispatchers.IO) {
            _messages
        }
    }

    override suspend fun addMessage(text: String, author: String) {
        withContext(Dispatchers.IO) {
            val updatedMessages = _messages.value.toMutableList()
            updatedMessages.add(Message(text, author))
            _messages.emit(updatedMessages)
        }
    }

    override suspend fun responseMessage() {
        withContext(Dispatchers.IO) {
            val dummyResponses = context.resources.getStringArray(R.array.responses)
            val updatedMessages = _messages.value.toMutableList()
            updatedMessages.add(Message(dummyResponses[dummyResponses.indices.random()], "Bot"))
            _messages.emit(updatedMessages)
        }
    }

}