package com.example.chessgame.data

import kotlinx.coroutines.flow.StateFlow

interface ConnectionManager {
    val connectionState: StateFlow<ConnectionState>
    
    fun connect()
    fun disconnect()
    fun isConnected(): Boolean
    suspend fun sendMessage(content: String)
    fun reconnect()
}

sealed class ConnectionState {
    object Connected : ConnectionState()
    object Connecting : ConnectionState()
    data class Disconnected(val reason: String? = null) : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}
