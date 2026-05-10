import { Camera } from '@/domain/types';
import { cameraService } from '@/services/camera-service';
import { useQueryClient } from '@tanstack/react-query';
import { useMutation } from '@tanstack/react-query';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';

dayjs.extend(utc);

export function useRecordingMutations() {
  const queryClient = useQueryClient();

  const fetchRecordingsMutation = useMutation({
    mutationFn: (cameraId: string) => cameraService.getRecordings(cameraId, dayjs.utc().format('YYYY-MM-DD')), // Assuming a getRecordings service method
    onSuccess: (data, cameraId) => {
      queryClient.setQueryData(['camera-recordings', cameraId], data); // Update cache for recordings
    },
    onError: (error: any) => {
      console.error("Erro ao buscar gravações:", error);
    },
  });

  return {
    fetchRecordingsMutation,
  };
}
