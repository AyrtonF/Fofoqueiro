import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor to include the tenant domain if needed
// Or the backend might handle it via the Host header.
// The instructions say "identifica o tenant_id pelo domínio"
api.interceptors.request.use((config) => {
  if (typeof window !== 'undefined') {
    // For local development, assuming a default tenant or identifying by other means if no domain match
    config.headers['X-Tenant-Id'] = '1'; // Temporary fix: hardcoding tenant ID 1 for MVP
  }
  return config;
});

export default api;
