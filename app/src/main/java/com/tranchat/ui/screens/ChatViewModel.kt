@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val connectionManager: ConnectionManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    init {
        initializeSession()
        observeConnection()
        observeMessages()
    }

    private fun initializeSession() = viewModelScope.launch {
        try {
            val sessionId = sessionManager.getCurrentSession() ?: sessionManager.createSession()
            loadMessages(sessionId)
        } catch (e: Exception) {
            handleError(ChatError.SessionError("Failed to initialize session", e))
        }
    }

    fun sendMessage(content: String) = viewModelScope.launch {
        try {
            val message = createMessage(content)
            repository.sendMessage(message)
            _messages.update { it + message }
        } catch (e: Exception) {
            handleError(ChatError.MessageError("Failed to send message", e))
        }
    }

    private fun handleError(error: ChatError) {
        _uiState.value = ChatUiState.Error(error)
        if (error is ChatError.ConnectionError) {
            scheduleReconnect()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            sessionManager.getCurrentSession()?.let { sessionId ->
                repository.finalizeSession(sessionId)
            }
        }
    }

    companion object {
        private const val MAX_RETRY_ATTEMPTS = 3
        private const val RETRY_DELAY_MS = 1000L
    }
}

sealed class ChatUiState {
    object Loading : ChatUiState()
    object Connected : ChatUiState()
    data class Error(val error: ChatError) : ChatUiState()
    data class Success(val messages: List<ChatMessage>) : ChatUiState()
}

sealed class ChatError {
    data class ConnectionError(val message: String, val cause: Throwable? = null) : ChatError()
    data class MessageError(val message: String, val cause: Throwable? = null) : ChatError()
    data class SessionError(val message: String, val cause: Throwable? = null) : ChatError()
}