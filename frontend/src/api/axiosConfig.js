// src/api/axiosConfig.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', // Your backend API base URL
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor: Add JWT token to all requests
api.interceptors.request.use(
  (config) => {
    // Correctly get the token using the key you use in AuthContext
    const token = localStorage.getItem('electromart_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor: Handle 401 (Unauthorized) errors globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Token expired or invalid, log out the user
      console.warn('Authentication failed (401). Logging out...');
      localStorage.removeItem('electromart_token'); // Clear the invalid token using the correct key
      localStorage.removeItem('electromart_user'); // Also clear user data
      // Redirect to the login page or home page
      window.location.href = '/login'; // Or '/' depending on your app's entry point
    }
    return Promise.reject(error);
  }
);

export default api;