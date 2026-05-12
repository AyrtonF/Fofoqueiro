import api from './api-client';
import { UserSession } from '@/domain/types';

export interface UserSessionRequest {
  tenantId: string | number;
  userId: string | number;
  tokenId: string;
  expiresAt: string;
  lastActivityAt?: string;
}

export const userSessionService = {
  list: async (userId?: string): Promise<UserSession[]> => {
    const response = await api.get<UserSession[]>('/user-sessions', { params: userId ? { userId } : undefined });
    return response.data;
  },

  getById: async (id: string): Promise<UserSession> => {
    const response = await api.get<UserSession>(`/user-sessions/${id}`);
    return response.data;
  },

  create: async (payload: UserSessionRequest): Promise<UserSession> => {
    const response = await api.post<UserSession>('/user-sessions', payload);
    return response.data;
  },

  update: async (id: string, payload: UserSessionRequest): Promise<UserSession> => {
    const response = await api.put<UserSession>(`/user-sessions/${id}`, payload);
    return response.data;
  },

  delete: async (id: string): Promise<void> => {
    await api.delete(`/user-sessions/${id}`);
  },
};