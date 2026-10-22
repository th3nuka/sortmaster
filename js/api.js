/**
 * SortMaster API Client & Hybrid Engine Dispatcher
 * Seamlessly talks to Spring Boot backend when present,
 * or runs 100% client-side via SortingEngine for static hosting (GitHub Pages, Netlify, Vercel, offline).
 */

const API = {
  baseUrl: '/api',

  async getHealth() {
    try {
      const response = await fetch(`${this.baseUrl}/health`);
      if (!response.ok) throw new Error('Health check failed');
      return await response.json();
    } catch (err) {
      return { status: 'UP', mode: 'CLIENT_SIDE_STATIC' };
    }
  },

  async getAllAlgorithms() {
    try {
      if (window.location.protocol !== 'file:' && window.location.host) {
        const response = await fetch(`${this.baseUrl}/algorithms`);
        if (response.ok) {
          return await response.json();
        }
      }
    } catch (err) {
      // Fallback to local client engine
    }

    if (window.SortingEngine) {
      return window.SortingEngine.getAllMetadata();
    }
    throw new Error('Unable to load algorithms.');
  },

  async getAlgorithm(id) {
    try {
      if (window.location.protocol !== 'file:' && window.location.host) {
        const response = await fetch(`${this.baseUrl}/algorithms/${encodeURIComponent(id)}`);
        if (response.ok) {
          return await response.json();
        }
      }
    } catch (err) {
      // Fallback to local client engine
    }

    if (window.SortingEngine) {
      const meta = window.SortingEngine.getAlgorithmMetadata(id);
      if (meta) return meta;
    }
    throw new Error(`Algorithm ${id} not found.`);
  },

  async sort(algorithmId, array, detailLevel = 'NORMAL', order = 'ASCENDING') {
    try {
      if (window.location.protocol !== 'file:' && window.location.host) {
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
        if (response.ok) {
          return await response.json();
        }
      }
    } catch (err) {
      // Fallback to client-side
    }

    if (window.SortingEngine) {
      return window.SortingEngine.sort(algorithmId, array, detailLevel, order);
    }
    throw new Error('Sorting execution failed.');
  },

  async compare(algorithmIds, array, order = 'ASCENDING') {
    try {
      if (window.location.protocol !== 'file:' && window.location.host) {
        const response = await fetch(`${this.baseUrl}/compare`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ algorithmIds, array, order })
        });
        if (response.ok) {
          return await response.json();
        }
      }
    } catch (err) {
      // Fallback to client-side
    }

    if (window.SortingEngine) {
      return window.SortingEngine.compare(algorithmIds, array, order);
    }
    throw new Error('Comparison execution failed.');
  }
};
