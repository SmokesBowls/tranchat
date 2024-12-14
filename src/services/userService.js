const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export const userService = {
  async checkPartnerStatus() {
    try {
      const response = await fetch(`${API_BASE_URL}/api/status`);
      return await response.json();
    } catch (error) {
      console.error('Failed to check partner status:', error);
      return { isOnline: false, bothReady: false };
    }
  },

  async setReady(ready) {
    try {
      await fetch(`${API_BASE_URL}/api/ready`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ ready }),
      });
    } catch (error) {
      console.error('Failed to set ready status:', error);
    }
  },

  async initiateConnection(type = 'p2p') {
    try {
      const response = await fetch(`${API_BASE_URL}/api/connect`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ connectionType: type }),
      });

      if (!response.ok) {
        throw new Error('Connection failed');
      }

      const data = await response.json();
      return data.connected;
    } catch (error) {
      console.error(`Failed to establish ${type} connection:`, error);
      return false;
    }
  }
}; 