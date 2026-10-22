/**
 * SortMaster API Client
 * Manages HTTP communication with the Spring Boot backend
 */

const API = {
  baseUrl: '/api',

  async getHealth() {
    try {
      const response = await fetch(`${this.baseUrl}/health`);
      if (!response.ok) throw new Error('Health check failed');
      return await response.json();
    } catch (err) {
      console.error('API Error: getHealth', err);
      throw err;
    }
  },

  async getAllAlgorithms() {
    try {
      const response = await fetch(`${this.baseUrl}/algorithms`);
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || 'Failed to fetch algorithms');
      }
      return await response.json();
    } catch (err) {
      console.error('API Error: getAllAlgorithms', err);
      throw err;
    }
  },

  async getAlgorithm(id) {
    try {
      const response = await fetch(`${this.baseUrl}/algorithms/${encodeURIComponent(id)}`);
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `Failed to fetch algorithm ${id}`);
      }
      return await response.json();
    } catch (err) {
      console.error(`API Error: getAlgorithm(${id})`, err);
      throw err;
    }
  },

  async sort(algorithmId, array, detailLevel = 'NORMAL', order = 'ASCENDING') {
    try {
      const response = await fetch(`${this.baseUrl}/sort`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          algorithm: algorithmId,
          algorithmId,
          array,
          detailLevel,
          order
        })
      });
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || 'Sorting execution failed on server');
      }
      return await response.json();
    } catch (err) {
      console.error('API Error: sort', err);
      throw err;
    }
  },

  async compare(algorithmIds, array, order = 'ASCENDING') {
    try {
      const response = await fetch(`${this.baseUrl}/compare`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ algorithmIds, array, order })
      });
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || 'Comparison execution failed on server');
      }
      return await response.json();
    } catch (err) {
      console.error('API Error: compare', err);
      throw err;
    }
  }
};
