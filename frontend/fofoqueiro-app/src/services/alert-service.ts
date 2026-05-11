import api from './api-client';
import { AlertEvent } from '@/domain/types';

export const alertService = {
  getAlertEvents: async (): Promise<AlertEvent[]> => {
    const response = await api.get<AlertEvent[]>('/alert-events');
    return response.data;
  },

  acknowledge: async (id: string): Promise<AlertEvent> => {
    const response = await api.post<AlertEvent>(`/alert-events/${id}/acknowledge`);
    return response.data;
  },

  getHealthSummary: async (): Promise<Array<{ id: string; fps?: number; bitrate?: number; status?: string; online?: boolean }>> => {
    const response = await api.get<Array<{ id: string; fps?: number; bitrate?: number; status?: string; online?: boolean }>>('/alert-events/health-summary');
    return response.data;
  },
};