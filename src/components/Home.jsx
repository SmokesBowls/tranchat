import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { userService } from '../services/userService';

const CONNECTION_TIMEOUT = 12000; // 12 seconds for P2P attempt

function Home() {
  const [partnerOnline, setPartnerOnline] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [connectionType, setConnectionType] = useState('p2p');
  const [showConnectionDialog, setShowConnectionDialog] = useState(false);
  const [connectionAttempt, setConnectionAttempt] = useState(0); // To track retry attempts
  const navigate = useNavigate();

  const attemptConnection = useCallback(async (type = 'p2p') => {
    setIsLoading(true);
    setConnectionType(type);
    
    try {
      const connected = await userService.initiateConnection(type);
      if (connected) {
        setPartnerOnline(true);
        setIsLoading(false);
        return true;
      }
    } catch (error) {
      console.error('Connection error:', error);
    }
    
    return false;
  }, []);

  // Handle initial connection and timeout
  useEffect(() => {
    let timeoutId;
    let mounted = true;

    const connect = async () => {
      // Start with P2P
      const success = await attemptConnection('p2p');
      
      if (!success && mounted) {
        // If P2P fails, show dialog after timeout
        timeoutId = setTimeout(() => {
          if (mounted) {
            setShowConnectionDialog(true);
            setIsLoading(false);
          }
        }, CONNECTION_TIMEOUT);
      }
    };

    connect();

    return () => {
      mounted = false;
      if (timeoutId) clearTimeout(timeoutId);
    };
  }, [attemptConnection, connectionAttempt]); // Include connectionAttempt to handle retries

  const handleRetryP2P = () => {
    setShowConnectionDialog(false);
    setConnectionAttempt(prev => prev + 1); // This will trigger the useEffect
  };

  const handleTryServer = async () => {
    setShowConnectionDialog(false);
    setIsLoading(true);
    await attemptConnection('server');
  };

  const handleReadyClick = async () => {
    await userService.setReady(true);
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <h1 className="text-4xl font-bold mb-8">Video Chat</h1>
      
      {isLoading ? (
        <div className="text-center space-y-4">
          <div className="text-gray-600">
            {connectionType === 'p2p' ? 'Attempting P2P connection...' : 'Trying server connection...'}
          </div>
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div>
        </div>
      ) : (
        <div className="space-y-4 text-center">
          <div className={`text-lg ${partnerOnline ? 'text-green-500' : 'text-red-500'}`}>
            Partner is {partnerOnline ? 'online' : 'offline'}
          </div>
          
          {partnerOnline && (
            <button
              onClick={handleReadyClick}
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-3 px-6 rounded-lg"
            >
              Ready to Chat
            </button>
          )}
        </div>
      )}

      {/* Connection Troubleshooting Dialog */}
      {showConnectionDialog && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
          <div className="bg-white rounded-lg p-6 max-w-sm mx-4">
            <h3 className="text-xl font-semibold text-gray-900 mb-4">
              Connection Failed
            </h3>
            <p className="text-gray-600 mb-6">
              P2P connection failed. Would you like to try connecting through our server?
            </p>
            <div className="flex justify-end space-x-4">
              <button
                onClick={handleRetryP2P}
                className="px-4 py-2 text-blue-600 hover:text-blue-800"
              >
                Retry P2P
              </button>
              <button
                onClick={handleTryServer}
                className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
              >
                Try Server
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Home; 