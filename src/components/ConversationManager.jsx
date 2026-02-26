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
    try {
      const chats = await chatService.getSavedChats();
      setSavedChats(chats || []);
    } catch (error) {
      console.error('Failed to load chats:', error);
    }
  };

  const startNewChat = () => {
    navigate('/chat', { state: { saving: false } });
  };

  const handleCreateSavedChat = () => {
    setShowNewChatDialog(true);
  };

  const handleDialogClose = () => {
    setShowNewChatDialog(false);
    loadSavedChats(); // Refresh list after potentially creating a new chat
  };

  return (
    <div className="min-h-screen bg-gray-50 py-6 px-4">
      <div className="max-w-lg mx-auto">
        <h1 className="text-2xl font-bold mb-6">Conversations</h1>

        <div className="flex space-x-4 mb-6">
          <button
            onClick={startNewChat}
            className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded shadow-sm transition-colors"
          >
            New Chat
          </button>
          <button
            onClick={handleCreateSavedChat}
            className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded shadow-sm transition-colors"
          >
            New Saved Chat
          </button>
        </div>

        {/* List of saved chats */}
        {savedChats.length > 0 && (
          <div className="bg-white rounded-lg shadow overflow-hidden">
            <div className="px-4 py-3 border-b border-gray-200 bg-gray-50">
              <h2 className="text-lg font-semibold text-gray-700">Saved Conversations</h2>
            </div>
            <ul className="divide-y divide-gray-200">
              {savedChats.map(chat => (
                <li key={chat.id} className="p-4 hover:bg-gray-50 transition-colors flex justify-between items-center">
                  <div className="min-w-0 flex-1 mr-4">
                    <h3 className="text-sm font-medium text-gray-900 truncate">{chat.title}</h3>
                    <p className="text-sm text-gray-500 truncate">{chat.lastMessage || 'No messages yet'}</p>
                  </div>
                  <button
                    onClick={() => navigate('/chat', { state: { chatId: chat.id } })}
                    className="text-blue-600 hover:text-blue-800 text-sm font-medium"
                    aria-label={`Open chat ${chat.title}`}
                  >
                    Open
                  </button>
                </li>
              ))}
            </ul>
          </div>
        )}

        {/* New Chat Dialog */}
        {showNewChatDialog && (
          <NewChatDialog onClose={handleDialogClose} />
        )}
      </div>
    </div>
  );
}

function NewChatDialog({ onClose }) {
  const [title, setTitle] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleCreate = async (e) => {
    e.preventDefault();
    if (!title.trim()) return;

    setIsLoading(true);
    try {
      await chatService.createSavedChat(title, password);
      onClose();
    } catch (error) {
      console.error('Failed to create chat:', error);
      // In a real app, we'd show a toast error here
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 backdrop-blur-sm"
      role="dialog"
      aria-modal="true"
      aria-labelledby="dialog-title"
    >
      <div className="bg-white rounded-lg p-6 w-full max-w-md shadow-xl transform transition-all">
        <h2 id="dialog-title" className="text-xl font-bold mb-4 text-gray-900">New Saved Conversation</h2>

        <form onSubmit={handleCreate}>
          <div className="mb-4">
            <label htmlFor="chatTitle" className="block text-sm font-medium text-gray-700 mb-1">
              Chat Title <span className="text-red-500" aria-hidden="true">*</span>
            </label>
            <input
              id="chatTitle"
              type="text"
              value={title}
              onChange={e => setTitle(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              placeholder="Enter chat title"
              required
              autoFocus
            />
          </div>

          <div className="mb-6">
            <label htmlFor="chatPassword" className="block text-sm font-medium text-gray-700 mb-1">
              Password <span className="text-gray-400 font-normal">(Optional)</span>
            </label>
            <input
              id="chatPassword"
              type="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              placeholder="Enter password"
            />
          </div>

          <div className="flex justify-end space-x-3">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-md font-medium focus:outline-none focus:ring-2 focus:ring-gray-400"
              disabled={isLoading}
            >
              Cancel
            </button>
            <button
              type="submit"
              className={`px-4 py-2 bg-blue-600 text-white rounded-md font-medium hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
              disabled={isLoading || !title.trim()}
            >
              {isLoading ? (
                <span className="flex items-center">
                  <svg className="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  Creating...
                </span>
              ) : 'Create'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ConversationManager;