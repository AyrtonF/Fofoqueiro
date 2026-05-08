import { create } from 'zustand';
import { WhiteLabelConfig } from '../domain/types';

interface TenantState {
  config: WhiteLabelConfig | null;
  isLoading: boolean;
  setConfig: (config: WhiteLabelConfig) => void;
  setLoading: (loading: boolean) => void;
}

export const useTenantStore = create<TenantState>((set) => ({
  config: null,
  isLoading: true,
  setConfig: (config) => set({ config, isLoading: false }),
  setLoading: (loading) => set({ isLoading: loading }),
}));
