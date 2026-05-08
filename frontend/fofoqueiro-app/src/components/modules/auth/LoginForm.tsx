'use client';

import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { useAuthStore } from '@/store/auth-store';
import { useTenantStore } from '@/store/tenant-store';
import { useRouter } from 'next/navigation';

const loginSchema = z.object({
  email: z.string().email({ message: "E-mail inválido" }),
  password: z.string().min(6, { message: "Senha deve ter pelo menos 6 caracteres" }),
});

const mfaSchema = z.object({
  code: z.string().length(6, { message: "Código deve ter 6 dígitos" }),
});

type LoginFormValues = z.infer<typeof loginSchema>;
type MfaFormValues = z.infer<typeof mfaSchema>;

export function LoginForm() {
  const [step, setStep] = useState<'login' | 'mfa'>('login');
  const [isLoading, setIsLoading] = useState(false);
  const { config } = useTenantStore();
  const router = useRouter();

  const loginForm = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: '', password: '' },
  });

  const mfaForm = useForm<MfaFormValues>({
    resolver: zodResolver(mfaSchema),
    defaultValues: { code: '' },
  });

  const onLoginSubmit = async (data: LoginFormValues) => {
    setIsLoading(true);
    try {
      // Simulate API call
      console.log('Login data:', data);
      setStep('mfa');
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  const onMfaSubmit = async (data: MfaFormValues) => {
    setIsLoading(true);
    try {
      // Simulate API call
      console.log('MFA data:', data);
      router.push('/dashboard');
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Card className="w-full max-w-md mx-auto">
      <CardHeader className="text-center">
        {config?.logoUrl ? (
          <img src={config.logoUrl} alt={config.companyName} className="h-12 mx-auto mb-4" />
        ) : (
          <div className="h-12 w-12 bg-primary rounded-full mx-auto mb-4 flex items-center justify-center text-white font-bold text-xl">
            F
          </div>
        )}
        <CardTitle>{config?.companyName || 'Fofoqueiro'}</CardTitle>
        <CardDescription>
          {step === 'login' ? 'Entre com suas credenciais' : 'Digite o código de 6 dígitos enviado ao seu e-mail'}
        </CardDescription>
      </CardHeader>
      <CardContent>
        {step === 'login' ? (
          <form onSubmit={loginForm.handleSubmit(onLoginSubmit)} className="space-y-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">E-mail</label>
              <Input {...loginForm.register('email')} placeholder="exemplo@email.com" />
              {loginForm.formState.errors.email && (
                <p className="text-xs text-destructive">{loginForm.formState.errors.email.message}</p>
              )}
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">Senha</label>
              <Input {...loginForm.register('password')} type="password" placeholder="••••••" />
              {loginForm.formState.errors.password && (
                <p className="text-xs text-destructive">{loginForm.formState.errors.password.message}</p>
              )}
            </div>
            <Button type="submit" className="w-full" disabled={isLoading}>
              {isLoading ? 'Carregando...' : 'Entrar'}
            </Button>
          </form>
        ) : (
          <form onSubmit={mfaForm.handleSubmit(onMfaSubmit)} className="space-y-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">Código MFA</label>
              <Input {...mfaForm.register('code')} placeholder="000000" maxLength={6} className="text-center text-2xl tracking-widest" />
              {mfaForm.formState.errors.code && (
                <p className="text-xs text-destructive">{mfaForm.formState.errors.code.message}</p>
              )}
            </div>
            <Button type="submit" className="w-full" disabled={isLoading}>
              {isLoading ? 'Verificando...' : 'Confirmar'}
            </Button>
            <Button type="button" variant="ghost" className="w-full" onClick={() => setStep('login')}>
              Voltar para o login
            </Button>
          </form>
        )}
      </CardContent>
    </Card>
  );
}
