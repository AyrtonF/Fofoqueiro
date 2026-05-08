import { ReactNode } from 'react';
import { Sidebar } from '@/components/common/Sidebar';
import Providers from '@/components/common/Providers';
import { useTenantConfig } from '@/hooks/use-tenant-config';
import { useAuthStore } from '@/store/auth-store';
import { useRouter } from 'next/navigation';
import { useEffect } from 'react';

interface MainLayoutProps {
  children: ReactNode;
}

export default function MainLayout({ children }: MainLayoutProps) {
  const { config, isLoading: isTenantLoading } = useTenantConfig();
  const { isAuthenticated } = useAuthStore();
  const router = useRouter();

  useEffect(() => {
    // Apply tenant config to body styles if loaded
    if (config) {
      document.documentElement.style.setProperty('--primary', config.primaryColor);
      document.documentElement.style.setProperty('--secondary', config.secondaryColor);
      // Add other theme customizations if needed
    }
  }, [config]);

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

  return (
    <Providers> {/* Wrap with Providers to ensure context is available */}
      <div className="flex min-h-screen bg-background">
        <Sidebar />
        <main className="flex-1 ml-64 p-6 overflow-x-hidden"> {/* Adjust ml for sidebar width */}
          {children}
        </main>
      </div>
    </Providers>
  );
}
