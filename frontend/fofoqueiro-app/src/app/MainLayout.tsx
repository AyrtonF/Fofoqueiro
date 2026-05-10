"use client";

import { ReactNode } from 'react';
import { Sidebar } from '@/components/common/Sidebar';
import { useTenantConfig } from '@/hooks/use-tenant-config';
import { useEffect } from 'react';

interface MainLayoutProps {
  children: ReactNode;
}

export default function MainLayout({ children }: MainLayoutProps) {
  const { config, isLoading: isTenantLoading } = useTenantConfig();

  useEffect(() => {
    // Apply tenant config to body styles if loaded
    if (config) {
      document.documentElement.style.setProperty('--primary', config.primaryColor);
      document.documentElement.style.setProperty('--secondary', config.secondaryColor);
      // Add other theme customizations if needed
    }
  }, [config]);

  return (
    <div className="flex min-h-screen bg-background">
      <Sidebar />
      <main className="flex-1 ml-64 p-6 overflow-x-hidden">
        {children}
      </main>
    </div>
  );
}
