'use client';

import { useQuery } from '@tanstack/react-query';
import { tenantService } from '@/services/tenant-service';
import { useTenantStore } from '@/store/tenant-store';
import { useEffect } from 'react';

export function useTenantConfig() {
  const { setConfig, setLoading } = useTenantStore();

  const { data, isLoading, error } = useQuery({
    queryKey: ['tenant-config'],
    queryFn: tenantService.getConfig,
    // Fetch config once on app load
    staleTime: Infinity, // Keep fetched config in cache indefinitely
    refetchOnWindowFocus: false, // Don't refetch on window focus
    refetchOnMount: false, // Don't refetch on mount if data is in cache
    refetchInterval: false, // No interval refetching
  });

  useEffect(() => {
    setLoading(true); // Set loading state when hook mounts
    if (data) {
      setConfig(data);
      document.documentElement.style.setProperty('--primary', data.primaryColor);
      document.documentElement.style.setProperty('--secondary', data.secondaryColor);
      setLoading(false); // Set loading to false once config is set
    } else if (error) {
      console.error("Error fetching tenant config:", error);
      setLoading(false); // Ensure loading is false even on error
    }
  }, [data, error, setConfig, setLoading]);

  return { config: data, isLoading: isLoading || (data === null && !error), error };
}
