package com.ogonzalezm.weartest.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogonzalezm.weartest.data.repository.message.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository
): ViewModel() {

    private val messages = messageRepository.messages

    private val _state = MutableStateFlow(ChatState())
    val state = combine(_state, messages) {state, messages ->
        state.copy(
            messages = messages
        )
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ChatState())

    fun handleIntent(intent: ChatIntent) {
        viewModelScope.launch {
            when (intent) {
                ChatIntent.ShowForm -> { showForm() }
                ChatIntent.ShowMessages -> { showMessages() }
                is ChatIntent.AddMessage -> addMessage(intent.text)
            }
        }
    }

    private fun showForm() {
        _state.update { it.copy(
            viewState = ViewState.Form
        ) }
    }

    private fun showMessages() {
        _state.update { it.copy(
            viewState = ViewState.Chat
        ) }
    }

    private fun addMessage(text: String) {
        viewModelScope.launch {
            messageRepository.addMessage(text, "User")
            delay(1200L)
            botResponse()
        }
        _state.update { it.copy(
            viewState = ViewState.Chat
        ) }
    }

    private fun botResponse() {
        viewModelScope.launch {
            messageRepository.responseMessage()
        }
    }

}