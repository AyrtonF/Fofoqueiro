import api from './api-client';
import { WhiteLabelConfig } from '../domain/types';

export const tenantService = {
  getConfig: async (): Promise<WhiteLabelConfig> => {
    const response = await api.get<WhiteLabelConfig>('/tenant/config');

    if (!response.data) {
      throw new Error('Tenant config not found');
    }

    return response.data;
  },
};
