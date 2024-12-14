class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {
    fun getMessages(sessionId: String, page: Int = 0) = 
        repository.getMessagesForSession(sessionId, page).asLiveData()

    fun getSessions() = repository.getAllSessions().asLiveData()

    fun sendMessage(sessionId: String, content: String) = viewModelScope.launch {
        repository.sendMessage(sessionId, content)
    }

    // Add other methods as needed
} 