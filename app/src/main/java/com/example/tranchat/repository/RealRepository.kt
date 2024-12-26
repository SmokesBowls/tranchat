@Singleton
class RealRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val apiService: ApiService,
    private val connectionManager: ConnectionManager,
    @ApplicationContext private val context: Context
) : ChatRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val messageQueue = Channel<Message>(Channel.BUFFERED)

    init {
        scope.launch { processMessageQueue() }
    }

    override suspend fun getMessages(): Flow<List<Message>> = 
        messageDao.getAllMessages()
            .catch { e -> throw RepositoryException("Failed to fetch messages", e) }

    override suspend fun sendMessage(message: Message) {
        try {
            messageDao.insert(message.copy(status = MessageStatus.SENDING))
            messageQueue.send(message)
        } catch (e: Exception) {
            throw RepositoryException("Failed to send message", e)
        }
    }

    private suspend fun processMessageQueue() {
        for (message in messageQueue) {
            try {
                if (connectionManager.isConnected()) {
                    val response = apiService.sendMessage(message)
                    messageDao.update(message.copy(
                        status = MessageStatus.SENT,
                        serverTimestamp = response.timestamp
                    ))
                } else {
                    messageDao.update(message.copy(status = MessageStatus.QUEUED))
                }
            } catch (e: Exception) {
                messageDao.update(message.copy(status = MessageStatus.FAILED))
            }
        }
    }

    override fun onCleared() {
        scope.cancel()
    }

    companion object {
        private const val MAX_RETRY_ATTEMPTS = 3
        private const val BATCH_SIZE = 50
    }
}

sealed class MessageStatus {
    object SENDING : MessageStatus()
    object SENT : MessageStatus()
    object FAILED : MessageStatus()
    object QUEUED : MessageStatus()
}

class RepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)