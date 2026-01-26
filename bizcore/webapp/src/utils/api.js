const API_BASE = '/api';

class ApiClient {
  getToken() {
    return localStorage.getItem('bizcore_token');
  }

  setToken(token) {
    localStorage.setItem('bizcore_token', token);
  }

  removeToken() {
    localStorage.removeItem('bizcore_token');
  }

  getUser() {
    const user = localStorage.getItem('bizcore_user');
    return user ? JSON.parse(user) : null;
  }

  setUser(user) {
    localStorage.setItem('bizcore_user', JSON.stringify(user));
  }

  removeUser() {
    localStorage.removeItem('bizcore_user');
  }

  async request(endpoint, options = {}) {
    const token = this.getToken();

    const headers = {
      'Content-Type': 'application/json',
      ...options.headers,
    };

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE}${endpoint}`, {
      ...options,
      headers,
    });

    if (response.status === 401) {
      this.removeToken();
      this.removeUser();
      window.location.href = '/login.html';
      throw new Error('Unauthorized');
    }

    if (!response.ok) {
      const error = await response.json().catch(() => ({}));
      throw new Error(error.message || `HTTP error ${response.status}`);
    }

    if (response.status === 204) {
      return null;
    }

    return response.json();
  }

  get(endpoint) {
    return this.request(endpoint, { method: 'GET' });
  }

  post(endpoint, data) {
    return this.request(endpoint, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  put(endpoint, data) {
    return this.request(endpoint, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  delete(endpoint) {
    return this.request(endpoint, { method: 'DELETE' });
  }

  // Auth endpoints
  async login(username, password) {
    const response = await this.post('/auth/login', { username, password });
    if (response.token) {
      this.setToken(response.token);
      this.setUser(response.user);
    }
    return response;
  }

  logout() {
    this.removeToken();
    this.removeUser();
    window.location.href = '/login.html';
  }

  isAuthenticated() {
    return !!this.getToken();
  }
}

export const api = new ApiClient();
export default api;
