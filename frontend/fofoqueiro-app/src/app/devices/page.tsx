'use client';

import { useState, useEffect } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { Camera } from '@/domain/types';
import { cameraService } from '@/services/camera-service';
import { InteractiveMap } from '@/components/common/InteractiveMap';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { CameraGrid } from '../monitoring/CameraGrid';
import { ExpandCameraDialog } from '../cameras/ExpandCameraDialog'; // Reuse shared stub from cameras folder
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogTrigger, DialogFooter } from '@/components/ui/dialog';
import { CameraOff, Camera as CameraIcon, AlertTriangle, MoreHorizontal, Video, Trash2, Map, Maximize2, Minimize2 } from 'lucide-react';
import Link from 'next/link';
import { toast } from 'sonner';

export default function DeviceManagementPage() {
  const queryClient = useQueryClient();
  const [isFullScreenMap, setIsFullScreenMap] = useState(false);
  const [selectedCameraId, setSelectedCameraId] = useState<string | null>(null);

  // Fetch cameras with location data
  const {
    data: cameras,
    isLoading,
    error,
    refetch
  } = useQuery<Camera[]>({
    queryKey: ['cameras-with-location'], // Cache key for cameras with location
    queryFn: async () => {
      const fetchedCameras = await cameraService.getAll();
      // Ensure latitude, longitude, and status are present for map display.
      // Fallback to default values if not provided by the backend.
      // In a production environment, these should ideally be returned by the API.
      return fetchedCameras.map(cam => ({
        ...cam,
        latitude: cam.latitude ?? -23.5505, // Default to São Paulo if not set
        longitude: cam.longitude ?? -46.6333,
        status: cam.status ?? 'OFFLINE', // Default status if not provided
      }));
    },
    staleTime: 30000, // Refresh data every 30 seconds
  });

  const toggleFullScreenMap = () => {
    setIsFullScreenMap(!isFullScreenMap);
  };

  const handleCameraSelect = (camera: Camera) => {
    setSelectedCameraId(camera.id);
  };

  const selectedCamera = cameras?.find(cam => cam.id === selectedCameraId);
  const mapCameras: Array<{
    id: string;
    name: string;
    latitude: number;
    longitude: number;
    status: 'ONLINE' | 'OFFLINE';
  }> = (cameras ?? []).map((camera) => ({
    ...camera,
    latitude: camera.latitude ?? -23.5505,
    longitude: camera.longitude ?? -46.6333,
    status: camera.status === 'ONLINE' ? 'ONLINE' : 'OFFLINE',
  }));

  return (
    <div className="w-full p-6">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold">Gerenciamento de Dispositivos</h1>
          <Button onClick={toggleFullScreenMap} variant="outline">
            {isFullScreenMap ? <Minimize2 className="h-4 w-4 mr-2" /> : <Maximize2 className="h-4 w-4 mr-2" />}
            {isFullScreenMap ? 'Sair do Modo Tela Cheia' : 'Modo Tela Cheia'}
          </Button>
        </div>

        <div className={`flex flex-col ${isFullScreenMap ? 'fixed inset-0 z-50 bg-background' : ''}`}>
          {/* Map Section */}
          <div className={`relative ${isFullScreenMap ? 'h-screen w-screen' : 'h-125 w-full mb-6'}`}>
            {isLoading && <p>Carregando câmeras...</p>}
            {error && <p className="text-destructive">Erro ao carregar câmeras: {error.message}</p>}

            {mapCameras.length > 0 ? (
              <InteractiveMap cameras={mapCameras} />
            ) : (
              !isLoading && !error && <p>Nenhuma câmera encontrada com dados de localização.</p>
            )}
          </div>

          {/* Camera List/Details Section */}
          <div className={`p-4 border rounded-md bg-card ${isFullScreenMap ? 'hidden' : ''}`}>
            <h2 className="text-2xl font-semibold mb-4">Câmeras</h2>
            {isLoading && <p>Carregando câmeras...</p>}
            {error && <p className="text-destructive">Erro ao carregar câmeras: {error.message}</p>}

            {cameras && cameras.length > 0 ? (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {cameras.map((camera) => (
                  <Card key={camera.id} className="flex flex-col justify-between">
                    <CardHeader>
                      <CardTitle>{camera.name}</CardTitle>
                      <CardDescription>
                        {camera.latitude !== undefined && camera.longitude !== undefined
                          ? `Lat: ${camera.latitude}, Lon: ${camera.longitude}`
                          : 'Localização não definida'}
                      </CardDescription>
                    </CardHeader>
                    <CardContent>
                      <div className={`flex items-center gap-2 p-2 rounded-md ${camera.status === 'ONLINE' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                        {camera.status === 'ONLINE' ? <CameraIcon /> : <CameraOff />}
                        {camera.status}
                      </div>
                    </CardContent>
                    <CardFooter className="flex justify-between">
                      <Button variant="outline" size="sm" onClick={() => handleCameraSelect(camera)}>
                        Ver Detalhes
                      </Button>
                      <Link href={`/playback/${camera.id}`} passHref>
                        <Button variant="secondary" size="sm">
                          <Video className="h-4 w-4 mr-2" /> Reproduzir
                        </Button>
                      </Link>
                    </CardFooter>
                  </Card>
                ))}
              </div>
            ) : (
              !isLoading && !error && <p>Nenhuma câmera configurada.</p>
            )}
          </div>
        </div>

        {/* Single Camera View Dialog */}
        {selectedCamera && (
          <Dialog open={!!selectedCameraId} onOpenChange={(isOpen: boolean) => !isOpen && setSelectedCameraId(null)}>
            <DialogContent className="sm:max-w-5xl max-h-[80vh] overflow-auto">
              <DialogHeader>
                <DialogTitle>Visualizando: {selectedCamera?.name}</DialogTitle>
                <DialogDescription>Detalhes e visualização ao vivo.</DialogDescription>
              </DialogHeader>
              <div className="w-full h-96"> {/* Fixed height for the camera grid */}
                {selectedCamera && <CameraGrid cameras={[selectedCamera]} />} {/* Reuse CameraGrid for single view */}
              </div>
              <DialogFooter>
                <Button variant="outline" onClick={() => setSelectedCameraId(null)}>Fechar</Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        )}
      </div>
    );
  }
