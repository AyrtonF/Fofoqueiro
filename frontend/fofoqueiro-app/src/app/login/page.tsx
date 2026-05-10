'use client';

import { useTenantConfig } from '@/hooks/use-tenant-config';
import { LoginForm } from '@/components/modules/auth/LoginForm';

export default function LoginPage() {
  const { config, isLoading } = useTenantConfig();

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-4">
      {isLoading ? (
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      ) : (
        <LoginForm />
      )}
    </div>
  );
}
