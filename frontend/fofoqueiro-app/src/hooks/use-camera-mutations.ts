import { Camera } from '@/domain/types';
import { cameraService } from '@/services/camera-service';
import { useMutation, useQueryClient } from '@tanstack/react-query';

export function useCameraMutation() {
  const queryClient = useQueryClient();

  const createCameraMutation = useMutation({
    mutationFn: (data: Partial<Camera>) => cameraService.create(data),
    onSuccess: (newCamera) => {
      queryClient.invalidateQueries({ queryKey: ['cameras'] });
      queryClient.setQueryData(['cameras', newCamera.id], newCamera); // Update cache for single camera if needed
    },
    onError: (error) => {
      console.error("Erro ao criar câmera:", error);
    },
  });

  const testConnectionMutation = useMutation({
    mutationFn: (url: string) => cameraService.testConnection(url),
    onError: (error) => {
      console.error("Erro ao testar conexão:", error);
    },
  });

  return {
    createCameraMutation,
    testConnectionMutation,
  };
}
