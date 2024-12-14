const HEARTBEAT_INTERVAL = 30000; // 30 seconds
const HEARTBEAT_TIMEOUT = 60000; // 60 seconds (2 missed heartbeats)
const RECONNECTION_WINDOW = 180000; // 3 minutes
const RECONNECTION_ATTEMPT_INTERVAL = 30000; // 30 seconds

class ConnectionManager {
  constructor() {
    this.socket = null;
    this.mode = 'p2p';
    this.messageQueue = [];
    this.connectionListeners = new Set();
    this.messageListeners = new Set();
    this.lastHeartbeat = Date.now();
    this.disconnectedAt = null;
    this.heartbeatInterval = null;
    this.reconnectionTimeout = null;
    this.connectionCheckInterval = null;
  }

  async connect(mode = 'p2p') {
    this.mode = mode;
    this.lastHeartbeat = Date.now();
    
    return new Promise((resolve, reject) => {
      try {
        const url = this.getWebSocketUrl();
        this.socket = new WebSocket(url);

        this.socket.onopen = () => {
          this.initializeConnection();
          this.startHeartbeat();
          resolve(true);
        };

        this.socket.onclose = () => {
          this.handleDisconnect();
        };

        this.socket.onerror = (error) => {
          reject(error);
        };

        this.socket.onmessage = (event) => {
          const message = JSON.parse(event.data);
          if (message.type === 'heartbeat') {
            this.handleHeartbeat();
          } else {
            this.handleMessage(message);
          }
        };

      } catch (error) {
        reject(error);
      }
    });
  }

  startHeartbeat() {
    this.heartbeatInterval = setInterval(() => {
      this.send({ type: 'heartbeat' });
    }, HEARTBEAT_INTERVAL);

    this.connectionCheckInterval = setInterval(() => {
      const timeSinceLastHeartbeat = Date.now() - this.lastHeartbeat;
      
      if (timeSinceLastHeartbeat > HEARTBEAT_TIMEOUT) {
        if (!this.disconnectedAt) {
          this.disconnectedAt = Date.now();
          this.notifyConnectionListeners('checking');
          this.handlePotentialDisconnect();
        }
      }
    }, 5000); // Check every 5 seconds
  }

  handlePotentialDisconnect() {
    const attemptReconnection = () => {
      const timeDisconnected = Date.now() - this.disconnectedAt;
      
      if (timeDisconnected > RECONNECTION_WINDOW) {
        this.notifyConnectionListeners('disconnected');
        clearInterval(this.reconnectionTimeout);
        return;
      }

      // After 60 seconds, show reconnecting status
      if (timeDisconnected > 60000) {
        this.notifyConnectionListeners('reconnecting');
      }

      this.tryReconnect();
    };

    this.reconnectionTimeout = setInterval(
      attemptReconnection,
      RECONNECTION_ATTEMPT_INTERVAL
    );
  }

  async tryReconnect() {
    try {
      await this.connect(this.mode);
      this.disconnectedAt = null;
      this.notifyConnectionListeners('connected');
      this.processQueuedMessages();
    } catch (error) {
      console.error('Reconnection attempt failed:', error);
    }
  }

  handleHeartbeat() {
    this.lastHeartbeat = Date.now();
    if (this.disconnectedAt) {
      this.disconnectedAt = null;
      this.notifyConnectionListeners('connected');
    }
  }

  async processQueuedMessages() {
    const messages = [...this.messageQueue];
    this.messageQueue = [];
    
    for (const message of messages) {
      try {
        await this.send(message);
      } catch (error) {
        this.messageQueue.push(message);
        break;
      }
    }
  }

  getWebSocketUrl() {
    if (this.mode === 'p2p') {
      return `ws://${window.location.hostname}:8080/p2p`;
    }
    return `wss://your-server.com/ws`; // Server fallback URL
  }

  async initializeConnection() {
    // Send initial authentication
    this.send({
      type: 'auth',
      userId: localStorage.getItem('userId') // You'll need to set this
    });

    // Process any queued messages
    while (this.messageQueue.length > 0) {
      const message = this.messageQueue.shift();
      this.send(message);
    }

    this.notifyConnectionListeners('connected');
  }

  handleDisconnect() {
    this.notifyConnectionListeners('disconnected');

    if (this.mode === 'p2p' && !this.isReconnecting) {
      this.attemptReconnect();
    }
  }

  async attemptReconnect() {
    if (this.reconnectAttempts >= MAX_RECONNECT_ATTEMPTS) {
      this.notifyConnectionListeners('failed');
      return;
    }

    this.isReconnecting = true;
    this.notifyConnectionListeners('reconnecting');

    try {
      await new Promise(resolve => setTimeout(resolve, RECONNECT_DELAY));
      await this.connect(this.mode);
      this.isReconnecting = false;
      this.reconnectAttempts = 0;
    } catch (error) {
      this.reconnectAttempts++;
      if (this.reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
        this.attemptReconnect();
      } else {
        this.isReconnecting = false;
        this.notifyConnectionListeners('failed');
      }
    }
  }

  send(message) {
    if (this.socket?.readyState === WebSocket.OPEN) {
      this.socket.send(JSON.stringify(message));
      return true;
    } else {
      this.messageQueue.push(message);
      return false;
    }
  }

  handleMessage(message) {
    this.messageListeners.forEach(listener => listener(message));
  }

  addConnectionListener(listener) {
    this.connectionListeners.add(listener);
    return () => this.connectionListeners.delete(listener);
  }

  addMessageListener(listener) {
    this.messageListeners.add(listener);
    return () => this.messageListeners.delete(listener);
  }

  notifyConnectionListeners(status) {
    this.connectionListeners.forEach(listener => listener(status));
  }

  disconnect() {
    this.socket?.close();
    this.socket = null;
    this.messageQueue = [];
    this.isReconnecting = false;
  }

  cleanup() {
    clearInterval(this.heartbeatInterval);
    clearInterval(this.connectionCheckInterval);
    clearInterval(this.reconnectionTimeout);
    this.disconnect();
  }
}

export const connectionService = new ConnectionManager(); 