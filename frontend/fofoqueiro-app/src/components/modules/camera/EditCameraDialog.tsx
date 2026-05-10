import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter, DialogTrigger } from '@/components/ui/dialog';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Camera } from '@/domain/types';
import { useCameraMutation } from '@/hooks/use-camera-mutations';
import { toast } from 'sonner';
import { useQueryClient } from '@tanstack/react-query';

interface EditCameraDialogProps {
  camera: Camera;
  isOpen: boolean;
  onClose: () => void;
}

export function EditCameraDialog({ camera, isOpen, onClose }: EditCameraDialogProps) {
  const queryClient = useQueryClient();
  const { mutate: updateCameraMutation, isPending: isUpdating } = useCameraMutation().createCameraMutation; // Reusing create mutation logic for update for now
  const { mutateAsync: testConnectionAsync } = useCameraMutation().testConnectionMutation;

  const [isTestingConnection, setIsTestingConnection] = useState(false);

  const formValues = {
    name: camera.name,
    url: camera.url,
    gatewayId: camera.gatewayId,
    latitude: camera.latitude ?? undefined,
    longitude: camera.longitude ?? undefined,
  };
  type CameraFormValues = {
    name: string;
    url: string;
    gatewayId: string;
    latitude?: number;
    longitude?: number;
  };

  const {
    handleSubmit,
    register,
    formState: { errors },
    reset,
    setValue, // To potentially pre-fill form
  } = useForm<CameraFormValues>({
    defaultValues: formValues as CameraFormValues,
  });

  const onSubmit = async (values: CameraFormValues) => {
    setIsTestingConnection(true);
    try {
      const testResult = await testConnectionAsync(values.url);
      if (!testResult?.success) {
        toast.error(`Falha ao conectar: ${testResult?.message || 'Erro'}`);
        setIsTestingConnection(false);
        return;
      }
      toast.success('Conexão testada com sucesso!');
      setIsTestingConnection(false);

      // In a real scenario, this would be an update API call
      // For now, we'll simulate it by creating a new camera and invalidating the list
      const payload = { ...camera, ...values } as Camera;
      updateCameraMutation(payload, {
        onSuccess: () => {
          toast.success('Câmera atualizada com sucesso!');
          queryClient.invalidateQueries({ queryKey: ['cameras'] });
          reset();
          onClose();
        },
        onError: (error: any) => {
          toast.error(`Erro ao atualizar câmera: ${error.message || 'Erro desconhecido'}`);
        },
      });
    } catch (error: any) {
      toast.error(`Erro ao testar conexão: ${error.message}`);
      setIsTestingConnection(false);
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={(open: boolean) => !open && onClose()}>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Editar Câmera</DialogTitle>
          <DialogDescription>Modifique os detalhes da câmera.</DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="name">Nome da Câmera</Label>
            <Input id="name" {...register('name')} placeholder="Ex: Câmera Entrada Principal" />
            {errors.name && <p className="text-xs text-destructive">{errors.name.message}</p>}
          </div>
          <div className="space-y-2">
            <Label htmlFor="url">URL RTSP</Label>
            <Input id="url" {...register('url')} placeholder="rtsp://user:password@ip:port/path" />
            {errors.url && <p className="text-xs text-destructive">{errors.url.message}</p>}
          </div>
          <div className="space-y-2">
            <Label htmlFor="gatewayId">Gateway</Label>
            <Input id="gatewayId" {...register('gatewayId')} placeholder="Selecione o Gateway" />
            {errors.gatewayId && <p className="text-xs text-destructive">{errors.gatewayId.message}</p>}
          </div>
          <div className="flex gap-4">
            <div className="space-y-2 w-1/2">
              <Label htmlFor="latitude">Latitude (Opcional)</Label>
              <Input
                id="latitude"
                type="number"
                step="any"
                {...register('latitude', { valueAsNumber: true })}
                placeholder="Ex: -23.5505"
              />
              {errors.latitude && <p className="text-xs text-destructive">{errors.latitude.message}</p>}
            </div>
            <div className="space-y-2 w-1/2">
              <Label htmlFor="longitude">Longitude (Opcional)</Label>
              <Input
                id="longitude"
                type="number"
                step="any"
                {...register('longitude', { valueAsNumber: true })}
                placeholder="Ex: -46.6333"
              />
              {errors.longitude && <p className="text-xs text-destructive">{errors.longitude.message}</p>}
            </div>
          </div>

          <DialogFooter className="pt-4 p-0 sm:flex-row sm:justify-start">
            <Button type="submit" className="w-full sm:w-auto" disabled={isTestingConnection || isUpdating}>
              {isTestingConnection ? 'Testando...' : isUpdating ? 'Salvando...' : 'Salvar Alterações'}
            </Button>
            <Button type="button" variant="outline" onClick={onClose} className="w-full sm:w-auto">
              Cancelar
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
