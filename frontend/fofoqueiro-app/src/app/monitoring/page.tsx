'use client';

import { useQuery } from '@tanstack/react-query';
import { cameraService } from '@/services/camera-service';
import { CameraGrid } from '@/components/modules/monitoring/CameraGrid';
import { Camera } from '@/domain/types';
import { useEffect } from 'react';

export default function MonitoringPage() {
  const {
    data: cameras,
    isLoading,
    error,
    refetch
  } = useQuery<Camera[]>({
    queryKey: ['cameras'],
    queryFn: cameraService.getAll,
    refetchInterval: 30000, // Refetch cameras list every 30 seconds
  });

  useEffect(() => {
    refetch(); // Initial fetch
  }, [refetch]);

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-background p-4">
        <p className="text-lg text-destructive mb-4">Erro ao carregar câmeras.</p>
        <Button onClick={() => refetch()}>Tentar novamente</Button>
      </div>
    );
  }

  return (
    <div className="min-h-screen w-full p-6">
      <h1 className="text-3xl font-bold mb-6">Monitoramento ao Vivo</h1>
      {cameras && cameras.length > 0 ? (
        <div className="h-[calc(100vh-150px)] w-full"> {/* Adjust height to fit */}
          <CameraGrid cameras={cameras} />
        </div>
      ) : (
        <div className="flex items-center justify-center h-full">
          <p className="text-lg text-muted-foreground">Nenhuma câmera configurada.</p>
        </div>
      )}
    </div>
  );
}
