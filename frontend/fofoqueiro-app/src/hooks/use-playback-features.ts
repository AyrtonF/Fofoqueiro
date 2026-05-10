import { Camera, PrivacyMask } from '@/domain/types';
import { cameraService } from '@/services/camera-service';
import { useQueryClient, useMutation } from '@tanstack/react-query';
import { toast } from 'sonner';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';

dayjs.extend(utc);

// Assume PrivacyMaskShape is defined in domain/types.ts
// interface PrivacyMaskShape { id: string; type: 'rectangle' | 'circle' | 'polygon'; points: { x: number; y: number }[]; }

interface RecordingInfo {
  id: string;
  url: string;
  startTime: string;
  endTime: string;
}

export function usePlaybackFeatures() {
  const queryClient = useQueryClient();

  // Mutation to fetch recordings for a specific camera and date
  const fetchRecordings = useMutation({
    mutationFn: ({ cameraId, date }: { cameraId: string; date: string }) =>
      cameraService.getRecordings(cameraId, date), // Assuming cameraService.getRecordings exists
    onSuccess: (data, variables) => {
      queryClient.setQueryData(['camera-recordings', variables.cameraId, variables.date], data);
    },
    onError: (error: any) => {
      console.error("Erro ao buscar gravações:", error);
    },
  });

  // Mutation to update privacy masks for a camera
  const updatePrivacyMasks = useMutation({
    mutationFn: ({ cameraId, masks }: { cameraId: string; masks: PrivacyMask[] }) =>
      cameraService.updatePrivacyMasks(cameraId, masks), // Assuming cameraService.updatePrivacyMasks exists
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ['camera-privacy-masks', variables.cameraId] });
      toast.success('Máscaras de privacidade salvas com sucesso!');
    },
    onError: (error: any) => {
      toast.error(`Erro ao salvar máscaras: ${error.message || 'Erro desconhecido'}`);
    },
  });

  return {
    fetchRecordings,
    updatePrivacyMasks,
  };
}
