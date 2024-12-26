@Singleton
class RealRepository @Inject constructor(
    private val messageDao: ChatMessageDao,
    private val connectionManager: ConnectionManager,
    private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context
) : ChatRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val messageQueue = Channel<ChatMessage>(Channel.BUFFERED)

    init {
        scope.launch { processMessageQueue() }
    }

    override suspend fun getMessages(sessionId: String): Flow<List<ChatMessage>> =
        messageDao.getMessagesForSession(sessionId)

    override suspend fun sendMessage(message: ChatMessage) {
        messageDao.insertMessage(message.copy(status = MessageStatus.SENDING))
        messageQueue.send(message)
    }

    override suspend fun createSession(): String {
        return sessionManager.createSession().also { sessionId ->
            messageDao.insertMessage(
                ChatMessage(
                    sessionId = sessionId,
                    type = MessageType.SYSTEM,
                    content = "Session started"
                )
            )
        }
    }

    override suspend fun finalizeSession(sessionId: String) {
        messageDao.updateMessagesForSession(
            sessionId = sessionId,
            status = MessageStatus.ARCHIVED
        )
    }

    private suspend fun processMessageQueue() {
        for (message in messageQueue) {
            try {
                if (connectionManager.isConnected()) {
                    connectionManager.sendMessage(message.content)
                    messageDao.updateMessageStatus(
                        messageId = message.id,
                        status = MessageStatus.SENT
                    )
                } else {
                    messageDao.updateMessageStatus(
                        messageId = message.id,
                        status = MessageStatus.QUEUED
                    )
                }
            } catch (e: Exception) {
                messageDao.updateMessageStatus(
                    messageId = message.id,
                    status = MessageStatus.FAILED
                )
            }
        }
    }

    override fun onCleared() {
        scope.cancel()
    }

    companion object {
        private const val MESSAGE_RETRY_ATTEMPTS = 3
    }
}