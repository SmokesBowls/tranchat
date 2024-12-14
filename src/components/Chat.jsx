import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { chatService } from '../services/chatService';

function Chat() {
  const navigate = useNavigate();
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [sessionId, setSessionId] = useState(null);

  useEffect(() => {
    const loadMessages = async () => {
      const loadedMessages = await chatService.getMessagesForSession(sessionId);
      setMessages(loadedMessages);
    };

    if (sessionId) {
      loadMessages();
    }
  }, [sessionId]);

  const sendMessage = async () => {
    if (newMessage.trim()) {
      await chatService.sendMessage(sessionId, newMessage);
      setNewMessage('');
    }
  };

  return (
    <div>
      {/* Chat UI */}
      <div>
        {messages.map(msg => (
          <div key={msg.id}>{msg.content}</div>
        ))}
      </div>
      <input value={newMessage} onChange={e => setNewMessage(e.target.value)} />
      <button onClick={sendMessage}>Send</button>
    </div>
  );
}

export default Chat; 