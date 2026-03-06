import { useState, useEffect, useCallback } from 'react';
import { useLocation, Navigate, useNavigate } from 'react-router-dom';
import { roomService } from '../services/roomService';

function Room() {
  const location = useLocation();
  const navigate = useNavigate();
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [roomError, setRoomError] = useState(null);
  const [showExitDialog, setShowExitDialog] = useState(false);

  // If there's no password or roomId in the state, redirect to home
  if (!location.state?.roomPassword || !location.state?.roomId) {
    return <Navigate to="/" replace />;
  }

  const isHost = location.state.isHost;
  const roomId = location.state.roomId;

  const handleExit = useCallback(() => {
    setShowExitDialog(true);
  }, []);

  const confirmExit = useCallback(() => {
    // Here you would typically notify the backend about user leaving
    navigate('/', { replace: true });
  }, [navigate]);

  // Handle phone's back button
  useEffect(() => {
    const handleBackButton = (event) => {
      event.preventDefault();
      handleExit();
    };

    window.history.pushState(null, null, window.location.pathname);
    window.addEventListener('popstate', handleBackButton);

    return () => {
      window.removeEventListener('popstate', handleBackButton);
    };
  }, [handleExit]);

  useEffect(() => {
    const checkRoomStatus = async () => {
      try {
        const status = await roomService.checkRoomStatus(roomId);
        if (!status.active) {
          setRoomError('This room is no longer active');
        }
      } catch (error) {
        setRoomError('Failed to connect to room');
      }
    };

    const interval = setInterval(checkRoomStatus, 5000); // Check every 5 seconds
    checkRoomStatus(); // Initial check

    return () => clearInterval(interval);
  }, [roomId]);

  if (roomError) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-gray-100">
        <div className="bg-white p-8 rounded-lg shadow-md">
          <h2 className="text-xl font-bold text-red-500 mb-4">{roomError}</h2>
          <button
            onClick={() => navigate('/')}
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          >
            Return to Home
          </button>
        </div>
      </div>
    );
  }

  const handleSendMessage = () => {
    if (newMessage.trim()) {
      setMessages([...messages, {
        text: newMessage.trim(),
        sender: isHost ? 'Host' : 'Guest'
      }]);
      setNewMessage('');
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSendMessage();
    }
  };

  return (
    <div className="relative h-screen">
      {/* Exit button */}
      <button
        onClick={handleExit}
        className="absolute top-4 right-4 z-10 text-white bg-black bg-opacity-50 rounded-full p-2 hover:bg-opacity-70 focus-visible:ring-2 focus:outline-none focus:ring-blue-500"
        aria-label="Leave room"
        title="Leave room"
      >
        <svg aria-hidden="true" xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>

      {/* Video placeholder */}
      <div className="absolute inset-0 bg-gray-800">
        {/* Video feed will go here */}
        <div className="absolute top-4 left-4 text-white bg-black bg-opacity-50 px-3 py-1 rounded">
          {isHost ? 'Host' : 'Guest'}
        </div>
      </div>

      {/* Chat overlay */}
      <div className="absolute right-0 top-0 bottom-0 w-80 bg-black bg-opacity-50 text-white p-4 flex flex-col">
        {/* Messages area */}
        <div className="flex-1 overflow-y-auto mb-4">
          {messages.map((message, index) => (
            <div key={index} className="mb-2 p-2 bg-gray-800 bg-opacity-50 rounded">
              <span className="font-bold">{message.sender}: </span>
              {message.text}
            </div>
          ))}
        </div>

        {/* Message input area */}
        <div className="flex gap-2">
          <input
            type="text"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder="Type a message..."
            className="flex-1 bg-gray-700 text-white px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button
            onClick={handleSendMessage}
            className="bg-blue-500 hover:bg-blue-600 px-4 py-2 rounded"
          >
            Send
          </button>
        </div>
      </div>

      {/* Exit confirmation dialog */}
      {showExitDialog && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 max-w-sm mx-4">
            <h3 className="text-xl font-semibold text-gray-900 mb-4">
              Leave Room?
            </h3>
            <p className="text-gray-600 mb-6">
              Are you sure you want to leave the room? This action cannot be undone.
            </p>
            <div className="flex justify-end space-x-4">
              <button
                onClick={() => setShowExitDialog(false)}
                className="px-4 py-2 text-gray-600 hover:text-gray-800"
              >
                Cancel
              </button>
              <button
                onClick={confirmExit}
                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
              >
                Leave Room
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Room; 