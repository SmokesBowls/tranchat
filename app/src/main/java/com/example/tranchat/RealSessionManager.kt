@Singleton
class RealSessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) : SessionManager {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Inactive)
    override val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    override suspend fun createSession(): String {
        val sessionId = generateSessionId()
        sharedPreferences.edit().apply {
            putString(KEY_SESSION_ID, sessionId)
            putLong(KEY_SESSION_START, System.currentTimeMillis())
            putBoolean(KEY_SESSION_ACTIVE, true)
        }.apply()
        _sessionState.value = SessionState.Active(sessionId)
        return sessionId
    }

    override fun getCurrentSession(): String? {
        if (!isSessionValid()) {
            invalidateSession()
            return null
        }
        return sharedPreferences.getString(KEY_SESSION_ID, null)
    }

    override fun invalidateSession() {
        sharedPreferences.edit().apply {
            remove(KEY_SESSION_ID)
            remove(KEY_SESSION_START)
            putBoolean(KEY_SESSION_ACTIVE, false)
        }.apply()
        _sessionState.value = SessionState.Inactive
    }

    private fun isSessionValid(): Boolean {
        val startTime = sharedPreferences.getLong(KEY_SESSION_START, 0)
        val isActive = sharedPreferences.getBoolean(KEY_SESSION_ACTIVE, false)
        return isActive && (System.currentTimeMillis() - startTime) < SESSION_TIMEOUT
    }

    private fun generateSessionId(): String = 
        UUID.randomUUID().toString()

    companion object {
        private const val PREFS_NAME = "session_prefs"
        private const val KEY_SESSION_ID = "session_id"
        private const val KEY_SESSION_START = "session_start"
        private const val KEY_SESSION_ACTIVE = "session_active"
        private const val SESSION_TIMEOUT = 24 * 60 * 60 * 1000L // 24 hours
    }
}

sealed class SessionState {
    data class Active(val sessionId: String) : SessionState()
    object Inactive : SessionState()
}