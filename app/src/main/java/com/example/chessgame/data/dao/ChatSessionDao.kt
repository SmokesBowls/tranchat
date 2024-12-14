import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatSessionDao {
    @Query("SELECT * FROM chat_sessions ORDER BY lastModified DESC")
    fun getAllSessions(): Flow<List<ChatSession>>

    @Query("SELECT * FROM chat_sessions WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: String): ChatSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ChatSession)

    @Delete
    suspend fun deleteSession(session: ChatSession)

    @Query("DELETE FROM chat_sessions")
    suspend fun deleteAllSessions()

    @Query("UPDATE chat_sessions SET lastMessage = :lastMessage, lastModified = :timestamp WHERE id = :sessionId")
    suspend fun updateLastMessage(sessionId: String, lastMessage: String, timestamp: Date)
} 