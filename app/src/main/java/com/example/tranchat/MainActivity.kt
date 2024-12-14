class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Create a simple test ViewModel
                    val viewModel = remember {
                        object : ChatViewModel(
                            repository = FakeRepository(),
                            connectionManager = FakeConnectionManager(),
                            translationService = FakeTranslationService(),
                            sessionManager = FakeSessionManager()
                        ) {
                            // Override necessary methods for testing
                        }
                    }

                    ChatScreen(
                        viewModel = viewModel,
                        onNavigateBack = { finish() }
                    )
                }
            }
        }
    }
}

// Simple fake implementations for testing
class FakeRepository : ChatRepository {
    // Implement with test data
}

class FakeConnectionManager : ConnectionManager {
    // Implement with test behavior
}

class FakeTranslationService : TranslationService {
    // Implement with test translations
}

class FakeSessionManager : SessionManager {
    // Implement with test session
}