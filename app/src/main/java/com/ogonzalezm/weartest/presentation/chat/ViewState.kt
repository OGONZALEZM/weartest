package com.ogonzalezm.weartest.presentation.chat

sealed interface ViewState {
    data object Loading: ViewState
    data object Form: ViewState
    data object Chat: ViewState
}