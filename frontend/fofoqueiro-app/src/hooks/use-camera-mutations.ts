import { Camera } from '@/domain/types';
import { cameraService } from '@/services/camera-service';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import type { CameraCreatePayload, CameraTestConnectionPayload, CameraUpdatePayload } from '@/services/camera-service';

export function useCameraMutation() {
  const queryClient = useQueryClient();

  const createCameraMutation = useMutation({
    mutationFn: (data: CameraCreatePayload) => cameraService.create(data),
    onSuccess: (newCamera) => {
      queryClient.invalidateQueries({ queryKey: ['cameras'] });
      queryClient.setQueryData(['cameras', newCamera.id], newCamera); // Update cache for single camera if needed
    },
    onError: (error) => {
      console.error("Erro ao criar câmera:", error);
    },
  });

  const testConnectionMutation = useMutation({
    mutationFn: (payload: CameraTestConnectionPayload) => cameraService.testConnection(payload),
    onError: (error) => {
      console.error("Erro ao testar conexão:", error);
    },
  });

  const updateCameraMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: CameraUpdatePayload }) => cameraService.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cameras'] });
    },
    onError: (error) => {
      console.error('Erro ao atualizar câmera:', error);
    },
  });

  return {
    createCameraMutation,
    testConnectionMutation,
    updateCameraMutation,
  };
}
