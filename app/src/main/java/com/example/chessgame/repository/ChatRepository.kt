import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date

class ChatRepository(
    private val chatSessionDao: ChatSessionDao,
    private val chatMessageDao: ChatMessageDao
) {
    fun getAllSessions(): Flow<List<ChatSession>> = chatSessionDao.getAllSessions()

    fun getMessagesForSession(sessionId: String, page: Int = 0): Flow<List<ChatMessage>> =
        chatMessageDao.getMessagesForSession(sessionId, limit = 30, offset = page * 30)

    suspend fun createSession(title: String, password: String? = null) = withContext(Dispatchers.IO) {
        val session = ChatSession(
            id = generateSessionId(),
            title = title,
            createdAt = Date(),
            lastModified = Date(),
            isPasswordProtected = password != null,
            passwordHash = password?.let { hashPassword(it) },
            lastMessage = null
        )
        chatSessionDao.insertSession(session)
        return@withContext session
    }

    suspend fun sendMessage(sessionId: String, content: String) = withContext(Dispatchers.IO) {
        val message = ChatMessage(
            id = generateMessageId(),
            sessionId = sessionId,
            content = content,
            timestamp = Date(),
            isSentByMe = true,
            status = MessageStatus.SENDING
        )
        chatMessageDao.insertMessage(message)
        chatSessionDao.updateLastMessage(sessionId, content, Date())
        return@withContext message
    }

    suspend fun handleQueuedMessages() = withContext(Dispatchers.IO) {
        chatMessageDao.getQueuedMessages()
    }

    suspend fun updateMessageStatus(messageId: String, status: MessageStatus) {
        chatMessageDao.updateMessageStatus(messageId, status)
    }

    suspend fun deleteSession(sessionId: String) = withContext(Dispatchers.IO) {
        chatSessionDao.getSessionById(sessionId)?.let {
            chatSessionDao.deleteSession(it)
        }
    }

    private fun generateSessionId(): String = UUID.randomUUID().toString()
    private fun generateMessageId(): String = UUID.randomUUID().toString()
    private fun hashPassword(password: String): String = // Implement secure hashing
} 