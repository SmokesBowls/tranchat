import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatSession::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sessionId")]
)
data class ChatMessage(
    @PrimaryKey val id: String,
    val sessionId: String,
    val content: String,
    val timestamp: Date,
    val isSentByMe: Boolean,
    val status: MessageStatus,
    val isQueued: Boolean = false
)

enum class MessageStatus {
    SENDING, SENT, FAILED, QUEUED
} 