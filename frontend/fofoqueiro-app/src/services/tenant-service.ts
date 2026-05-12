import api from './api-client';
import { Tenant, WhiteLabelConfig } from '../domain/types';

export interface TenantRequest {
  name: string;
  domain: string;
  isActive: boolean;
}

export interface WhiteLabelConfigRequest {
  tenantId: string | number;
  logoUrl?: string;
  primaryColor?: string;
  secondaryColor?: string;
  faviconUrl?: string;
}

export const tenantService = {
  list: async (): Promise<Tenant[]> => {
    const response = await api.get<Tenant[]>('/tenants');
    return response.data;
  },

  getById: async (id: string): Promise<Tenant> => {
    const response = await api.get<Tenant>(`/tenants/${id}`);
    return response.data;
  },

  create: async (payload: TenantRequest): Promise<Tenant> => {
    const response = await api.post<Tenant>('/tenants', payload);
    return response.data;
  },

  update: async (id: string, payload: TenantRequest): Promise<Tenant> => {
    const response = await api.put<Tenant>(`/tenants/${id}`, payload);
    return response.data;
  },

  delete: async (id: string): Promise<void> => {
    await api.delete(`/tenants/${id}`);
  },

  getConfig: async (): Promise<WhiteLabelConfig> => {
    const response = await api.get<WhiteLabelConfig>('/tenant/config');

    if (!response.data) {
      throw new Error('Tenant config not found');
    }

    return response.data;
  },

  listWhiteLabelConfigs: async (): Promise<WhiteLabelConfig[]> => {
    const response = await api.get<WhiteLabelConfig[]>('/white-label-configs');
    return response.data;
  },

  getWhiteLabelConfigByTenant: async (tenantId: string): Promise<WhiteLabelConfig> => {
    const response = await api.get<WhiteLabelConfig>(`/white-label-configs/tenant/${tenantId}`);
    return response.data;
  },

  createWhiteLabelConfig: async (payload: WhiteLabelConfigRequest): Promise<WhiteLabelConfig> => {
    const response = await api.post<WhiteLabelConfig>('/white-label-configs', payload);
    return response.data;
  },

  updateWhiteLabelConfig: async (id: string, payload: WhiteLabelConfigRequest): Promise<WhiteLabelConfig> => {
    const response = await api.put<WhiteLabelConfig>(`/white-label-configs/${id}`, payload);
    return response.data;
  },

  deleteWhiteLabelConfig: async (id: string): Promise<void> => {
    await api.delete(`/white-label-configs/${id}`);
  },
};
