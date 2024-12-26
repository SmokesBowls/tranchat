@Singleton
class RealConnectionManager @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @ApplicationContext private val context: Context
) : ConnectionManager {

    private val messageListeners = mutableSetOf<MessageListener>()
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected())
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private var webSocket: WebSocket? = null
    private val retryPolicy = ExponentialBackoff(maxAttempts = 3)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun connect() {
        if (isConnected()) return
        updateState(ConnectionState.Connecting)
        
        val request = Request.Builder()
            .url(WEBSOCKET_URL)
            .build()

        webSocket = okHttpClient.newWebSocket(request, createWebSocketListener())
    }

    override fun disconnect() {
        webSocket?.close(NORMAL_CLOSURE_STATUS, "Client disconnected")
        webSocket = null
        updateState(ConnectionState.Disconnected("User disconnected"))
    }

    override fun isConnected(): Boolean =
        connectionState.value is ConnectionState.Connected

    override fun sendMessage(message: String): Boolean {
        return try {
            webSocket?.send(message) ?: false
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send message: ${e.message}", e)
            false
        }
    }

    override fun addMessageListener(listener: MessageListener) {
        messageListeners.add(listener)
    }

    override fun removeMessageListener(listener: MessageListener) {
        messageListeners.remove(listener)
    }

    override fun reconnect() {
        scope.launch {
            retryPolicy.execute {
                disconnect()
                delay(it.delayMillis)
                connect()
            }
        }
    }

    private fun updateState(state: ConnectionState) {
        _connectionState.value = state
        messageListeners.forEach { it.onConnectionStateChanged(state) }
    }

    private fun createWebSocketListener() = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            updateState(ConnectionState.Connected)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            messageListeners.forEach { it.onMessageReceived(text) }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            val error = ChatError.ConnectionError(t.message ?: "Unknown error", t)
            messageListeners.forEach { it.onError(error) }
            updateState(ConnectionState.Error(t.message ?: "Connection failed"))
            reconnect()
        }
    }

    companion object {
        private const val TAG = "RealConnectionManager"
        private const val WEBSOCKET_URL = "ws://your-server-url/chat"
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}