@Singleton
class RealConnectionManager @Inject constructor(
    private val webSocket: OkHttpClient
) : ConnectionManager {
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected())
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private var webSocketClient: WebSocket? = null
    private var isReconnecting = false
    private val retryPolicy = ExponentialBackoff(maxAttempts = 3)

    override fun connect() {
        if (isConnected()) return
        _connectionState.value = ConnectionState.Connecting
        
        val request = Request.Builder()
            .url(WEBSOCKET_URL)
            .build()

        webSocketClient = webSocket.newWebSocket(request, createWebSocketListener())
    }

    override fun disconnect() {
        webSocketClient?.close(NORMAL_CLOSURE_CODE, "User initiated disconnect")
        webSocketClient = null
        _connectionState.value = ConnectionState.Disconnected("User disconnected")
    }

    override fun isConnected(): Boolean = 
        _connectionState.value is ConnectionState.Connected

    override suspend fun sendMessage(content: String) {
        if (!isConnected()) throw IllegalStateException("Not connected")
        webSocketClient?.send(content) ?: throw IllegalStateException("WebSocket is null")
    }

    override fun reconnect() {
        if (isReconnecting) return
        isReconnecting = true
        viewModelScope.launch {
            retryPolicy.execute {
                disconnect()
                delay(it.delayMillis)
                connect()
            }
        }
    }

    private fun createWebSocketListener() = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            _connectionState.value = ConnectionState.Connected
            isReconnecting = false
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            _connectionState.value = ConnectionState.Error(t.message ?: "Unknown error")
            if (!isReconnecting) reconnect()
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            _connectionState.value = ConnectionState.Disconnected(reason)
        }
    }

    companion object {
        private const val WEBSOCKET_URL = "ws://your-server-url/chat"
        private const val NORMAL_CLOSURE_CODE = 1000
    }
}