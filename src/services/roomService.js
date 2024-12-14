// This service will handle all room-related API calls
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export const roomService = {
  async createRoom(password) {
    try {
      const response = await fetch(`${API_BASE_URL}/api/rooms`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ password }),
      });

      if (!response.ok) {
        throw new Error('Failed to create room');
      }

      return await response.json();
    } catch (error) {
      throw new Error('Failed to create room: ' + error.message);
    }
  },

  async joinRoom(password) {
    try {
      const response = await fetch(`${API_BASE_URL}/api/rooms/join`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ password }),
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Failed to join room');
      }

      return await response.json();
    } catch (error) {
      throw new Error(error.message || 'Failed to join room');
    }
  },

  async checkRoomStatus(roomId) {
    try {
      const response = await fetch(`${API_BASE_URL}/api/rooms/${roomId}`);
      if (!response.ok) {
        throw new Error('Failed to check room status');
      }
      return await response.json();
    } catch (error) {
      throw new Error('Failed to check room status: ' + error.message);
    }
  },
}; 