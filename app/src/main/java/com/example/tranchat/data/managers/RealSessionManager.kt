package com.example.tranchat.data.managers

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealSessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) : SessionManager {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Inactive)
    override val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    override fun startSession(userId: String) {
        val sessionId = UUID.randomUUID().toString()
        sharedPreferences.edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_SESSION_ID, sessionId)
            putLong(KEY_SESSION_START, System.currentTimeMillis())
            putBoolean(KEY_SESSION_ACTIVE, true)
        }.apply()
        _sessionState.value = SessionState.Active(sessionId, userId)
    }

    override fun endSession() {
        sharedPreferences.edit().apply {
            remove(KEY_USER_ID)
            remove(KEY_SESSION_ID)
            putBoolean(KEY_SESSION_ACTIVE, false)
        }.apply()
        _sessionState.value = SessionState.Inactive
    }

    override fun getCurrentUserId(): String? =
        sharedPreferences.getString(KEY_USER_ID, null)

    override fun isSessionValid(): Boolean {
        val startTime = sharedPreferences.getLong(KEY_SESSION_START, 0)
        val isActive = sharedPreferences.getBoolean(KEY_SESSION_ACTIVE, false)
        return isActive && (System.currentTimeMillis() - startTime) < SESSION_TIMEOUT
    }

    companion object {
        private const val PREFS_NAME = "session_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_SESSION_ID = "session_id"
        private const val KEY_SESSION_START = "session_start"
        private const val KEY_SESSION_ACTIVE = "session_active"
        private const val SESSION_TIMEOUT = 24 * 60 * 60 * 1000L // 24 hours
    }
}

sealed class SessionState {
    data class Active(val sessionId: String, val userId: String) : SessionState()
    object Inactive : SessionState()
}