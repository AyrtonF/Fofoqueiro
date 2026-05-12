"use client";

import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { toast } from 'sonner';
import { Camera } from '@/domain/types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { useCameraMutation } from '@/hooks/use-camera-mutations';
import { useAuthStore } from '@/store/auth-store';

const editCameraSchema = z.object({
  name: z.string().min(3, 'Informe um nome com pelo menos 3 caracteres'),
  gatewayId: z.coerce.number().int().positive('Informe um gateway válido'),
  url: z.string().regex(/^rtsp:\/\//, 'Informe uma URL RTSP válida'),
  latitude: z.coerce.number(),
  longitude: z.coerce.number(),
  recordingRetentionDays: z.coerce.number().int().min(1, 'A retenção mínima é 1 dia'),
});

type EditCameraFormValues = z.infer<typeof editCameraSchema>;

interface EditCameraDialogProps {
  camera: Camera | null;
  onClose: () => void;
}

export function EditCameraDialog({ camera, onClose }: EditCameraDialogProps) {
  const tenantId = useAuthStore((state) => state.user?.tenantId);
  const { updateCameraMutation, testConnectionMutation } = useCameraMutation();

  const { register, handleSubmit, formState: { errors }, reset } = useForm<EditCameraFormValues>({
    resolver: zodResolver(editCameraSchema) as any,
    defaultValues: {
      name: camera?.name ?? '',
      gatewayId: camera?.gatewayId ? Number(camera.gatewayId) : 1,
      url: camera?.url ?? '',
      latitude: camera?.latitude ?? 0,
      longitude: camera?.longitude ?? 0,
      recordingRetentionDays: 7,
    },
  });

  useEffect(() => {
    if (!camera) {
      return;
    }

    reset({
      name: camera.name,
      gatewayId: camera.gatewayId ? Number(camera.gatewayId) : 1,
      url: camera.url,
      latitude: camera.latitude ?? 0,
      longitude: camera.longitude ?? 0,
      recordingRetentionDays: camera.resolution ? 7 : 7,
    });
  }, [camera, reset]);

  if (!camera) {
    return null;
  }

  const onSubmit = async (values: EditCameraFormValues) => {
    if (!tenantId) {
      toast.error('Tenant não encontrado. Faça login novamente.');
      return;
    }

    const connection = await testConnectionMutation.mutateAsync({
      url: values.url,
      gatewayId: values.gatewayId,
    });

    if (!connection.success) {
      toast.error(connection.message);
      return;
    }

    updateCameraMutation.mutate(
      {
        id: String(camera.id),
        data: {
          gatewayId: values.gatewayId,
          name: values.name,
          url: values.url,
          latitude: values.latitude,
          longitude: values.longitude,
          recordingRetentionDays: values.recordingRetentionDays,
        },
      },
      {
        onSuccess: () => {
          toast.success('Câmera atualizada com sucesso.');
          onClose();
        },
        onError: (error: any) => {
          toast.error(error?.message || 'Não foi possível atualizar a câmera.');
        },
      }
    );
  };

  return (
    <Dialog open={!!camera} onOpenChange={(open) => !open && onClose()}>
      <DialogContent className="sm:max-w-[560px]">
        <DialogHeader>
          <DialogTitle>Editar câmera</DialogTitle>
          <DialogDescription>Ajuste os dados técnicos da câmera e teste a conexão antes de salvar.</DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit(onSubmit)} className="grid gap-4">
          <div className="grid gap-2">
            <Label htmlFor="edit-name">Nome da câmera</Label>
            <Input id="edit-name" {...register('name')} />
            {errors.name && <p className="text-sm text-destructive">{errors.name.message}</p>}
          </div>

          <div className="grid gap-2">
            <Label htmlFor="edit-gatewayId">Gateway ID</Label>
            <Input id="edit-gatewayId" type="number" min={1} {...register('gatewayId')} />
            {errors.gatewayId && <p className="text-sm text-destructive">{errors.gatewayId.message}</p>}
          </div>

          <div className="grid gap-2">
            <Label htmlFor="edit-url">URL RTSP</Label>
            <Input id="edit-url" {...register('url')} />
            {errors.url && <p className="text-sm text-destructive">{errors.url.message}</p>}
          </div>

          <div className="grid gap-4 sm:grid-cols-3">
            <div className="grid gap-2">
              <Label htmlFor="edit-latitude">Latitude</Label>
              <Input id="edit-latitude" type="number" step="any" {...register('latitude')} />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="edit-longitude">Longitude</Label>
              <Input id="edit-longitude" type="number" step="any" {...register('longitude')} />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="edit-retention">Retenção</Label>
              <Input id="edit-retention" type="number" min={1} {...register('recordingRetentionDays')} />
            </div>
          </div>

          <DialogFooter>
            <Button type="button" variant="outline" onClick={onClose}>
              Cancelar
            </Button>
            <Button type="submit" disabled={updateCameraMutation.isPending || testConnectionMutation.isPending}>
              {updateCameraMutation.isPending ? 'Salvando...' : 'Salvar alterações'}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

export default EditCameraDialog;
