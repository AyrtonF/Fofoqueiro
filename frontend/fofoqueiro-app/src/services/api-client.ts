import axios from 'axios';
import { useAuthStore } from '@/store/auth-store';
import { useTenantStore } from '@/store/tenant-store';

const FALLBACK_API_URL = 'http://localhost:8080/api/v1';

function resolveTenantId(): string | undefined {
  if (typeof window === 'undefined') {
    return undefined;
  }

  const tenantFromState = useAuthStore.getState().user?.tenantId || useTenantStore.getState().config?.tenantId;
  if (tenantFromState) {
    return String(tenantFromState);
  }

  const hostname = window.location.hostname;
  if (hostname === 'localhost' || hostname === '127.0.0.1') {
    return process.env.NEXT_PUBLIC_DEV_TENANT_ID || '1';
  }

  return undefined;
}

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || FALLBACK_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor: Add auth token and tenant ID
api.interceptors.request.use((config) => {
  const tenantId = resolveTenantId();
  const { token } = useAuthStore.getState();

  if (tenantId) {
    config.headers['X-Tenant-Id'] = tenantId;
  }

  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }

  return config;
});

// Response interceptor: Handle 401 errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const requestUrl = String(error.config?.url || '');
    const isAuthEndpoint = requestUrl.includes('/auth/');

    if (!isAuthEndpoint && (error.response?.status === 401 || error.response?.status === 403)) {
      // Token is invalid or expired
      const { logout } = useAuthStore.getState();
      logout();
      
      // Redirect to login page
      if (typeof window !== 'undefined') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
