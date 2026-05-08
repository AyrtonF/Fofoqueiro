'use client';

import { useState, useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Camera, CameraStatus } from '@/domain/types';
import { cameraService } from '@/services/camera-service';
import { VideoPlayer } from './VideoPlayer';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';
import { Grip, LayoutGrid, Maximize2, Minimize2, RefreshCw, AlertCircle } from 'lucide-react'; // Import icons

// Define possible grid layouts
type GridSize = '1x1' | '2x2' | '3x3' | '4x4';

interface CameraGridProps {
  cameras: Camera[];
}

export function CameraGrid({ cameras }: CameraGridProps) {
  const [currentLayout, setCurrentLayout] = useState<GridSize>('2x2');
  const [expandedCameraId, setExpandedCameraId] = useState<string | null>(null);

  // Fetch camera health status periodically
  const { data: cameraHealths, isLoading: isHealthLoading, error: healthError, refetch: refetchHealth } = useQuery<any[]>({
    queryKey: ['camera-healths'],
    queryFn: cameraService.getHealth,
    refetchInterval: 5000, // Refetch every 5 seconds
  });

  useEffect(() => {
    refetchHealth(); // Initial fetch
  }, [refetchHealth]);

  const getCameraStatus = (cameraId: string): CameraStatus | undefined => {
    if (!cameraHealths) return undefined;
    const health = cameraHealths.find(health => health.id === cameraId);
    return health?.status;
  };

  const toggleExpand = (cameraId: string | null) => {
    setExpandedCameraId(cameraId);
  };

  const getGridCols = (layout: GridSize) => {
    switch (layout) {
      case '1x1': return 'grid-cols-1';
      case '2x2': return 'grid-cols-2';
      case '3x3': return 'grid-cols-3';
      case '4x4': return 'grid-cols-4';
      default: return 'grid-cols-2';
    }
  };

  const activeCameras = cameras.filter(cam => getCameraStatus(cam.id) !== CameraStatus.OFFLINE);
  const camerasToDisplay = expandedCameraId ? cameras.filter(cam => cam.id === expandedCameraId) : activeCameras;

  // Determine the number of columns based on the layout
  const cols = parseInt(currentLayout.split('x')[0]);
  const rows = Math.ceil(camerasToDisplay.length / cols);

  return (
    <div className="relative w-full h-full p-4">
      {/* Layout Selector */}
      <div className="absolute top-4 right-4 z-10 flex gap-2">
        <Button variant="outline" size="sm" onClick={() => setCurrentLayout('1x1')}>1x1</Button>
        <Button variant="outline" size="sm" onClick={() => setCurrentLayout('2x2')}>2x2</Button>
        <Button variant="outline" size="sm" onClick={() => setCurrentLayout('3x3')}>3x3</Button>
        <Button variant="outline" size="sm" onClick={() => setCurrentLayout('4x4')}>4x4</Button>
      </div>

      {/* Grid Container */}
      <div className={`grid ${getGridCols(currentLayout)} gap-4 w-full h-full`}>
        {camerasToDisplay.map((camera, index) => {
          const status = getCameraStatus(camera.id) || camera.status; // Fallback to camera's last known status
          const isExpandedView = expandedCameraId === camera.id;

          return (
            <div
              key={camera.id}
              className={cn(
                "relative aspect-video rounded-lg overflow-hidden border border-border bg-black transition-all duration-300",
                isExpandedView ? "col-span-full row-span-full z-50" : "", // Expanded view takes full space
                (currentLayout === '1x1' || expandedCameraId) && 'col-span-full row-span-full',
                currentLayout === '2x2' && 'col-span-1 row-span-1',
                currentLayout === '3x3' && 'col-span-1 row-span-1',
                currentLayout === '4x4' && 'col-span-1 row-span-1'
              )}
              style={
                isExpandedView
                  ? { width: '100%', height: '100%' }
                  : {}
              }
            >
              <VideoPlayer
                camera={{ ...camera, status }} // Pass updated status
                isMainStream={isExpandedView}
                isExpanded={isExpandedView}
                onExpand={() => toggleExpand(isExpandedView ? null : camera.id)}
              />
            </div>
          );
        })}
      </div>

      {/* Handling empty state or errors */}
      {activeCameras.length === 0 && !isHealthLoading && !healthError && (
        <div className="absolute inset-0 flex items-center justify-center">
          <p className="text-lg text-muted-foreground">Nenhuma câmera ativa encontrada.</p>
        </div>
      )}
      {healthError && (
        <div className="absolute inset-0 flex items-center justify-center">
          <p className="text-lg text-destructive">Erro ao carregar status das câmeras.</p>
        </div>
      )}
    </div>
  );
}
