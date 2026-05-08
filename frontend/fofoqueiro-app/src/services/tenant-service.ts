import api from './api-client';
import { Tenant, WhiteLabelConfig } from '../domain/types';

export const tenantService = {
  getConfig: async (): Promise<WhiteLabelConfig> => {
    const response = await api.get<Tenant>('/tenant/config');
    if (!response.data || !response.data.whiteLabelConfig) {
      throw new Error('Tenant config not found');
    }
    return response.data.whiteLabelConfig;
  },
};
