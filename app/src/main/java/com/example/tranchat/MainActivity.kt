package com.example.tranchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.tranchat.data.managers.RealConnectionManager
import com.example.tranchat.data.managers.RealSessionManager
import com.example.tranchat.repository.RealRepository
import com.example.tranchat.ui.screens.ChatScreen
import com.example.tranchat.ui.screens.ChatViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = ChatViewModel(
                        repository = RealRepository(),
                        connectionManager = RealConnectionManager(),
                        translationService = RealTranslationService(),
                        sessionManager = RealSessionManager(applicationContext)
                    )

                    ChatScreen(
                        viewModel = viewModel,
                        onNavigateBack = { finish() }
                    )
                }
            }
        }
    }
}
