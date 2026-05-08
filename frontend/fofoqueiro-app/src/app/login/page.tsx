'use client';

import { useTenantConfig } from '@/hooks/use-tenant-config';
import { LoginForm } from '@/components/modules/auth/LoginForm';
import MainLayout from '@/app/MainLayout'; // Import the main layout
import { useEffect } from 'react';

export default function LoginPage() {
  const { config, isLoading } = useTenantConfig();

  // If tenant config is loaded, and we have config data, we can render the login form
  // Otherwise, show a loading spinner.

  return (
    <MainLayout> {/* Wrap with MainLayout to get the sidebar/header structure */}
      <div className="flex min-h-screen items-center justify-center bg-background p-4">
        {isLoading ? (
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        ) : (
          <LoginForm />
        )}
      </div>
    </MainLayout>
  );
}
