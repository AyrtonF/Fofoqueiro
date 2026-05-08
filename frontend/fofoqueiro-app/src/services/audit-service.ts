import api from './api-client';
import { AuditLog } from '../domain/types';

export const auditService = {
  getAuditLogs: async (filters: any): Promise<AuditLog[]> => {
    const params = new URLSearchParams();
    if (filters.date) params.append('date', filters.date.format('YYYY-MM-DD'));
    if (filters.action) params.append('action', filters.action);
    if (filters.resource) params.append('resource', filters.resource);
    if (filters.userId) params.append('userId', filters.userId);
    if (filters.ipAddress) params.append('ipAddress', filters.ipAddress);

    const response = await api.get<AuditLog[]>(`/audit-logs?${params.toString()}`);
    return response.data;
  },

  exportAuditLogs: async (filters: any): Promise<Blob> => {
    const response = await api.get('/audit-logs/export', {
      params: {
        date: filters.date ? filters.date.format('YYYY-MM-DD') : undefined,
        action: filters.action,
        resource: filters.resource,
        userId: filters.userId,
        ipAddress: filters.ipAddress,
      },
      responseType: 'blob',
    });
    return response.data;
  }
};
