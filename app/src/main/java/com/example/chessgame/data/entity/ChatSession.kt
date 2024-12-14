import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "chat_sessions")
data class ChatSession(
    @PrimaryKey val id: String,
    val title: String,
    val createdAt: Date,
    val lastModified: Date,
    val isPasswordProtected: Boolean,
    val passwordHash: String?,
    val lastMessage: String?
) 