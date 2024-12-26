@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.messages.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    val isSaving by viewModel.isSavingEnabled.collectAsState()
    val showSaveDialog by viewModel.showSaveDialog.collectAsState()

    Scaffold(
        topBar = {
            ChatTopBar(
                connectionState = connectionState,
                onBackClick = onNavigateBack,
                onReconnectClick = { viewModel.reconnect() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MessageList(
                messages = messages,
                modifier = Modifier.weight(1f)
            )
            
            MessageInput(
                onMessageSent = { viewModel.sendMessage(it) },
                isConnected = connectionState is ConnectionState.Connected
            )
            
            if (showSaveDialog) {
                SaveDialog(
                    onConfirm = { viewModel.saveSession() },
                    onDismiss = { viewModel.dismissSaveDialog() }
                )
            }
        }
    }
}

@Composable
private fun MessageList(
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        items(messages) { message ->
            MessageItem(message = message)
        }
    }
}

@Composable
private fun MessageInput(
    onMessageSent: (String) -> Unit,
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            enabled = isConnected
        )
        
        Button(
            onClick = {
                onMessageSent(text)
                text = ""
            },
            enabled = text.isNotBlank() && isConnected
        ) {
            Text("Send")
        }
    }
}