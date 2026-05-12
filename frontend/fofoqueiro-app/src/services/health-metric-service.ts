import api from './api-client';
import { HealthMetric } from '@/domain/types';

export interface HealthMetricRequest {
  tenantId: string | number;
  cameraId: string | number;
  online: boolean;
  fps?: number;
  bitrate?: number;
  recordingConfidence?: number;
  measuredAt?: string;
}

export const healthMetricService = {
  list: async (cameraId?: string): Promise<HealthMetric[]> => {
    const response = await api.get<HealthMetric[]>('/health-metrics', { params: cameraId ? { cameraId } : undefined });
    return response.data;
  },

  getById: async (id: string): Promise<HealthMetric> => {
    const response = await api.get<HealthMetric>(`/health-metrics/${id}`);
    return response.data;
  },

  create: async (payload: HealthMetricRequest): Promise<HealthMetric> => {
    const response = await api.post<HealthMetric>('/health-metrics', payload);
    return response.data;
  },

  update: async (id: string, payload: HealthMetricRequest): Promise<HealthMetric> => {
    const response = await api.put<HealthMetric>(`/health-metrics/${id}`, payload);
    return response.data;
  },

  delete: async (id: string): Promise<void> => {
    await api.delete(`/health-metrics/${id}`);
  },
};