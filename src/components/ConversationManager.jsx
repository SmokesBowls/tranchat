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

  const handleCreate = async (e) => {
    e.preventDefault();
    await chatService.createSavedChat(title, password);
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50" role="dialog" aria-modal="true" aria-labelledby="dialog-title">
      <div className="bg-white rounded-lg p-6 w-full max-w-sm">
        <h2 id="dialog-title" className="text-xl font-bold mb-4">New Saved Chat</h2>
        <form onSubmit={handleCreate} className="space-y-4">
          <div>
            <label htmlFor="chatTitle" className="block text-sm font-medium text-gray-700 mb-1">
              Chat Title
            </label>
            <input
              id="chatTitle"
              value={title}
              onChange={e => setTitle(e.target.value)}
              placeholder="Enter title..."
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500"
            />
          </div>
          <div>
            <label htmlFor="chatPassword" className="block text-sm font-medium text-gray-700 mb-1">
              Password (optional)
            </label>
            <input
              id="chatPassword"
              type="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              placeholder="Enter password..."
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500"
            />
          </div>
          <div className="flex justify-end space-x-2 pt-4">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500"
            >
              Create
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ConversationManager; 