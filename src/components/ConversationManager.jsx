import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { chatService } from '../services/chatService';

function ConversationManager() {
  const [savedChats, setSavedChats] = useState([]);
  const [showNewChatDialog, setShowNewChatDialog] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    loadSavedChats();
  }, []);

  const loadSavedChats = async () => {
    const chats = await chatService.getSavedChats();
    setSavedChats(chats);
  };

  const startNewChat = () => {
    navigate('/chat', { state: { saving: false } });
  };

  const handleCreateSavedChat = () => {
    setShowNewChatDialog(true);
  };

  return (
    <div className="min-h-screen bg-gray-50 py-6 px-4">
      <div className="max-w-lg mx-auto">
        <h1 className="text-2xl font-bold mb-6">Conversations</h1>

        <button onClick={startNewChat} className="btn btn-primary">New Chat</button>
        <button onClick={handleCreateSavedChat} className="btn btn-secondary">New Saved Chat</button>

        {/* List of saved chats */}
        {savedChats.length > 0 && (
          <div>
            <h2 className="text-lg font-semibold">Saved Conversations</h2>
            {savedChats.map(chat => (
              <div key={chat.id}>
                <h3>{chat.title}</h3>
                <p>{chat.lastMessage}</p>
                <button onClick={() => navigate('/chat', { state: { chatId: chat.id } })}>Open</button>
              </div>
            ))}
          </div>
        )}

        {/* New Chat Dialog */}
        {showNewChatDialog && (
          <NewChatDialog onClose={() => setShowNewChatDialog(false)} />
        )}
      </div>
    </div>
  );
}

function NewChatDialog({ onClose }) {
  const [title, setTitle] = useState('');
  const [password, setPassword] = useState('');

  const handleCreate = async () => {
    await chatService.createSavedChat(title, password);
    onClose();
  };

  return (
    <div>
      {/* Dialog UI for creating a new chat */}
      <input value={title} onChange={e => setTitle(e.target.value)} placeholder="Chat Title" />
      <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password (optional)" />
      <button onClick={handleCreate}>Create</button>
      <button onClick={onClose}>Cancel</button>
    </div>
  );
}

export default ConversationManager; 