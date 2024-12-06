package com.ogonzalezm.weartest.presentation.chat

import android.view.KeyEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.ogonzalezm.weartest.R
import com.ogonzalezm.weartest.common.ChatBotColor
import com.ogonzalezm.weartest.common.ChatBotTextStyle
import com.ogonzalezm.weartest.data.domain.Message
import kotlinx.coroutines.delay

@Composable
fun ChatScreen(
    viewModel: ChatViewModel
) {
    val state by viewModel.state.collectAsState()
    val onAction = viewModel::handleIntent

    when(state.viewState) {
        ViewState.Loading -> {
            Loading(onAction = onAction)
        }
        ViewState.Chat -> {
            MessageList(messages = state.messages,
                onAction = onAction)
        }
        ViewState.Form -> {
            Form(onAction = onAction)
        }
    }
}

@Composable
fun Loading(
    onAction: (ChatIntent) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val alpha: Float by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        label = "FadeInAnimation"
    )
    LaunchedEffect(Unit) {
        delay(1000L)
        isVisible = true
        delay(2000L)
        onAction(ChatIntent.ShowMessages)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(modifier = Modifier.graphicsLayer(alpha = alpha),
                text = stringResource(id = R.string.app_name),
                style = ChatBotTextStyle.title,
                textAlign = TextAlign.Center)
            Text(modifier = Modifier.graphicsLayer(alpha = alpha),
                text = stringResource(id = R.string.chat_bot_desc),
                style = ChatBotTextStyle.subTitle,
                textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun MessageList(
    messages: List<Message>,
    onAction: (ChatIntent) -> Unit
) {
    val chatListState = rememberScalingLazyListState()
    LaunchedEffect(key1 = messages.size) {
        chatListState.animateScrollToItem(chatListState.layoutInfo.totalItemsCount)
    }
    ScalingLazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = chatListState
    ) {
        messages.forEach { message->
            item {
                MessageItem(message = message)
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        if(messages.isEmpty()) {
            Text(text = stringResource(id = R.string.empty_questions),
                style = ChatBotTextStyle.body)
            Spacer(modifier = Modifier.weight(1f))
        }
        Button(onClick = { onAction(ChatIntent.ShowForm) }) {
            Icon(imageVector = Icons.Default.Add,
                tint = Color.White,
                contentDescription = stringResource(id = R.string.add_message))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MessageItem(
    message: Message
) {
    val isBot = message.author == "Bot"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(isBot) ChatBotColor.Verdigris else ChatBotColor.SandyBrown
        ),
        shape = RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(modifier = Modifier.fillMaxWidth(),
                text = message.text,
                style = ChatBotTextStyle.subTitle,
                textAlign = if(isBot) TextAlign.End else TextAlign.Start)
            Text(modifier = Modifier.fillMaxWidth(),
                text = message.author,
                style = ChatBotTextStyle.body,
                color = ChatBotColor.WhiteSmoke,
                textAlign = if(isBot) TextAlign.End else TextAlign.Start)
        }
    }
}

@Composable
fun Form(
    onAction: (ChatIntent) -> Unit
) {
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    onAction(ChatIntent.AddMessage(text))
                    true
                } else {
                    false
                }
            },
            value = text,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.DarkGray,
                focusedContainerColor = Color.DarkGray
            ),
            onValueChange = { newText ->
                text = newText
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAction(ChatIntent.AddMessage(text))
                }
            ))
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { onAction(ChatIntent.AddMessage(text)) }) {
            Text(text = stringResource(id = R.string.send_message),
                color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}