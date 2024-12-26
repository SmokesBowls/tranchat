@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM messages ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun getRecentMessages(
        limit: Int = 30, 
        offset: Int = 0
    ): Flow<List<ChatMessage>>

    @Query("SELECT * FROM messages WHERE status = :status")
    suspend fun getMessagesByStatus(status: MessageStatus): List<ChatMessage>

    @Query("SELECT * FROM messages WHERE sessionId = :sessionId AND status = :status")
    suspend fun getMessagesBySessionAndStatus(sessionId: String, status: MessageStatus): List<ChatMessage>

    @Query("SELECT * FROM messages WHERE isQueued = 1")
    suspend fun getQueuedMessages(): List<ChatMessage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessage>)

    @Query("UPDATE messages SET status = :status WHERE id = :messageId")
    suspend fun updateMessageStatus(messageId: String, status: MessageStatus)

    @Query("DELETE FROM messages WHERE sessionId = :sessionId")
    suspend fun deleteMessagesForSession(sessionId: String)

    @Query("DELETE FROM messages WHERE timestamp < :timestamp")
    suspend fun deleteMessagesOlderThan(timestamp: Long)

    @Query("SELECT COUNT(*) FROM messages WHERE sessionId = :sessionId")
    suspend fun getMessageCountForSession(sessionId: String): Int
}