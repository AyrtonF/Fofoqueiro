'use client';

import { useEffect } from 'react';
import { useRouter, usePathname } from 'next/navigation';
import { useAuthStore } from '@/store/auth-store';
import { useTenantConfig } from '@/hooks/use-tenant-config';
import MainLayout from './MainLayout';

export default function AppWrapper({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, restoreToken } = useAuthStore();
  const { isLoading: isTenantLoading } = useTenantConfig();
  const router = useRouter();
  const pathname = usePathname();

  // Restore token from localStorage on mount
  useEffect(() => {
    restoreToken();
  }, [restoreToken]);

  // Redirect to login if not authenticated and not already on the login page
  useEffect(() => {
    if (!isTenantLoading && !isAuthenticated && pathname !== '/login') {
      router.push('/login');
    }
  }, [isAuthenticated, isTenantLoading, router, pathname]);

  // Show loading spinner if tenant config is still loading
  if (isTenantLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  // For login page, don't use MainLayout
  if (pathname === '/login') {
    return <>{children}</>;
  }

  // For authenticated pages, use MainLayout
  return (
    <MainLayout>
      {children}
    </MainLayout>
  );
}
