import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Query("""
        SELECT * FROM messages 
        WHERE sessionId = :sessionId 
        ORDER BY timestamp DESC 
        LIMIT :limit OFFSET :offset
    """)
    fun getMessagesForSession(
        sessionId: String, 
        limit: Int = 30, 
        offset: Int = 0
    ): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessage>)

    @Query("UPDATE messages SET status = :status WHERE id = :messageId")
    suspend fun updateMessageStatus(messageId: String, status: MessageStatus)

    @Query("SELECT * FROM messages WHERE status = :status")
    suspend fun getMessagesByStatus(status: MessageStatus): List<ChatMessage>

    @Query("SELECT * FROM messages WHERE isQueued = 1")
    suspend fun getQueuedMessages(): List<ChatMessage>

    @Query("DELETE FROM messages WHERE sessionId = :sessionId")
    suspend fun deleteMessagesForSession(sessionId: String)
} 