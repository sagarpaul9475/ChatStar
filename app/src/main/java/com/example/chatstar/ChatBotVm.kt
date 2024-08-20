package com.example.chatstar

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatstar.ui.theme.ChatData
import com.example.chatstar.ui.theme.ChatRoleEnum
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import android.util.Log
import com.google.ai.client.generativeai.type.content

class ChatBotVm : ViewModel() {

    val list by lazy {
        mutableStateListOf<ChatData>()
    }

    // Assuming ApiKey is defined somewhere
    private val apiKey = ApiKey

    private val genAi by lazy {
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey
        )
    }

    fun sendMessage(message: String) = viewModelScope.launch {
        try {
            val chat: Chat = genAi.startChat()
            list.add(ChatData(message, ChatRoleEnum.USER.role))
            val response = chat.sendMessage(
                content(ChatRoleEnum.USER.role) { text(message) }
            )
            response.text?.let {
                list.add(ChatData(it, ChatRoleEnum.MODEL.role))
            }
        } catch (e: Exception) {
            Log.e("ChatBotVm", "Error sending message", e)
        }
    }
}
