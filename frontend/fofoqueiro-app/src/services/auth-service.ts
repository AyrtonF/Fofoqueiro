import api from './api-client';
import { User } from '@/domain/types';

interface LoginRequest {
  email: string;
  password: string;
}

interface LoginResponse {
  token: string;
  refreshToken?: string;
  user: User;
}

interface MfaRequest {
  code: string;
}

interface MfaResponse {
  token: string;
  refreshToken?: string;
  user: User;
}

export const authService = {
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await api.post<LoginResponse>('/auth/login', credentials);
    return response.data;
  },

  async verifyMfa(mfaCode: MfaRequest): Promise<MfaResponse> {
    const response = await api.post<MfaResponse>('/auth/mfa/verify', mfaCode);
    return response.data;
  },

  async logout(): Promise<void> {
    try {
      await api.post('/auth/logout');
    } catch (error) {
      console.error('Error during logout:', error);
    }
  },

  async refreshToken(refreshToken: string): Promise<LoginResponse> {
    const response = await api.post<LoginResponse>('/auth/refresh', {
      refreshToken,
    });
    return response.data;
  },
};
