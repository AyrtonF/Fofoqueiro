'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuthStore } from '@/store/auth-store';
import { useTenantConfig } from '@/hooks/use-tenant-config';
import MainLayout from './MainLayout';

export default function AppWrapper({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuthStore();
  const { isLoading: isTenantLoading } = useTenantConfig();
  const router = useRouter();

  useEffect(() => {
    // Redirect to login if not authenticated and not already on the login page
    // Also consider if tenant config is still loading, as that's a prerequisite
    if (!isTenantLoading && !isAuthenticated && window.location.pathname !== '/login') {
      router.push('/login');
    }
  }, [isAuthenticated, isTenantLoading, router]);

  // Show loading spinner if tenant config is still loading, or if redirecting to login
  if (isTenantLoading || (!isAuthenticated && window.location.pathname !== '/login')) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  // Render children within the main layout only if authenticated or on the login page
  return (
    <MainLayout>
      {children}
    </MainLayout>
  );
}
