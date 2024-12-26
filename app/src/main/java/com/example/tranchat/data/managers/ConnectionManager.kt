package com.example.tranchat.data.managers

import kotlinx.coroutines.flow.StateFlow

interface ConnectionManager {
    val connectionState: StateFlow<ConnectionState>
    
    fun connect()
    fun disconnect()
    fun isConnected(): Boolean
    fun sendMessage(message: String): Boolean
    fun addMessageListener(listener: MessageListener)
    fun removeMessageListener(listener: MessageListener)
    fun reconnect()
}

interface MessageListener {
    fun onMessageReceived(message: String)
    fun onConnectionStateChanged(state: ConnectionState)
    fun onError(error: ChatError)
}

sealed class ConnectionState {
    object Connected : ConnectionState()
    object Connecting : ConnectionState()
    data class Disconnected(val reason: String? = null) : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}

sealed class ChatError {
    data class ConnectionError(val message: String, val cause: Throwable? = null) : ChatError()
    data class MessageError(val message: String) : ChatError()
    object NetworkUnavailable : ChatError()
}