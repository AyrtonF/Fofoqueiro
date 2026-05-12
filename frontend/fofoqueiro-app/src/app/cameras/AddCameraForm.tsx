"use client";

import { useMemo } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { toast } from 'sonner';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useCameraMutation } from '@/hooks/use-camera-mutations';
import { useAuthStore } from '@/store/auth-store';

const cameraSchema = z.object({
  name: z.string().min(3, 'Informe um nome com pelo menos 3 caracteres'),
  gatewayId: z.coerce.number().int().positive('Informe um gateway válido'),
  url: z.string().regex(/^rtsp:\/\//, 'Informe uma URL RTSP válida'),
  latitude: z.coerce.number(),
  longitude: z.coerce.number(),
  recordingRetentionDays: z.coerce.number().int().min(1, 'A retenção mínima é 1 dia'),
});

type CameraFormValues = z.infer<typeof cameraSchema>;

type AddCameraFormProps = {
  onSuccess?: () => void;
};

export function AddCameraForm({ onSuccess }: AddCameraFormProps) {
  const { createCameraMutation, testConnectionMutation } = useCameraMutation();
  const tenantId = useAuthStore((state) => state.user?.tenantId);

  const defaultValues = useMemo<CameraFormValues>(
    () => ({
      name: '',
      gatewayId: 1,
      url: 'rtsp://',
      latitude: 0,
      longitude: 0,
      recordingRetentionDays: 7,
    }),
    []
  );

  const { register, handleSubmit, formState: { errors } } = useForm<CameraFormValues>({
    resolver: zodResolver(cameraSchema) as any,
    defaultValues,
  });

  const onSubmit = async (values: CameraFormValues) => {
    if (!tenantId) {
      toast.error('Tenant não encontrado. Faça login novamente.');
      return;
    }

    const testResult = await testConnectionMutation.mutateAsync({
      url: values.url,
      gatewayId: values.gatewayId,
    });

    if (!testResult.success) {
      toast.error(testResult.message);
      return;
    }

    createCameraMutation.mutate(
      {
        tenantId,
        gatewayId: values.gatewayId,
        name: values.name,
        url: values.url,
        latitude: values.latitude,
        longitude: values.longitude,
        recordingRetentionDays: values.recordingRetentionDays,
      },
      {
        onSuccess: () => {
          toast.success('Câmera criada com sucesso.');
          onSuccess?.();
        },
        onError: (error: any) => {
          toast.error(error?.message || 'Não foi possível criar a câmera.');
        },
      }
    );
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="grid gap-4">
      <div className="grid gap-2">
        <Label htmlFor="name">Nome da câmera</Label>
        <Input id="name" placeholder="Câmera portaria" {...register('name')} />
        {errors.name && <p className="text-sm text-destructive">{errors.name.message}</p>}
      </div>

      <div className="grid gap-2">
        <Label htmlFor="gatewayId">Gateway ID</Label>
        <Input id="gatewayId" type="number" min={1} placeholder="1" {...register('gatewayId')} />
        {errors.gatewayId && <p className="text-sm text-destructive">{errors.gatewayId.message}</p>}
      </div>

      <div className="grid gap-2">
        <Label htmlFor="url">URL RTSP</Label>
        <Input id="url" placeholder="rtsp://usuario:senha@ip:554/stream" {...register('url')} />
        {errors.url && <p className="text-sm text-destructive">{errors.url.message}</p>}
      </div>

      <div className="grid gap-4 sm:grid-cols-3">
        <div className="grid gap-2">
          <Label htmlFor="latitude">Latitude</Label>
          <Input id="latitude" type="number" step="any" {...register('latitude')} />
          {errors.latitude && <p className="text-sm text-destructive">{errors.latitude.message}</p>}
        </div>
        <div className="grid gap-2">
          <Label htmlFor="longitude">Longitude</Label>
          <Input id="longitude" type="number" step="any" {...register('longitude')} />
          {errors.longitude && <p className="text-sm text-destructive">{errors.longitude.message}</p>}
        </div>
        <div className="grid gap-2">
          <Label htmlFor="recordingRetentionDays">Retenção</Label>
          <Input id="recordingRetentionDays" type="number" min={1} {...register('recordingRetentionDays')} />
          {errors.recordingRetentionDays && <p className="text-sm text-destructive">{errors.recordingRetentionDays.message}</p>}
        </div>
      </div>

      <div className="flex items-center justify-end gap-2 pt-2">
        <Button type="submit" disabled={createCameraMutation.isPending || testConnectionMutation.isPending}>
          {createCameraMutation.isPending ? 'Salvando...' : 'Adicionar câmera'}
        </Button>
      </div>
    </form>
  );
}

export default AddCameraForm;
