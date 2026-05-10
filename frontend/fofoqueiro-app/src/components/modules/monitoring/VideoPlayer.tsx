'use client';

import { useEffect, useRef, useState } from 'react';
import { Camera, CameraStatus } from '@/domain/types';
import { cameraService } from '@/services/camera-service';
import { cn } from '@/lib/utils';
import { Maximize2, Minimize2, RefreshCw, AlertCircle } from 'lucide-react';
import { Button } from '@/components/ui/button';

interface VideoPlayerProps {
  camera: Camera;
  isMainStream?: boolean;
  onExpand?: () => void;
  isExpanded?: boolean;
}

export function VideoPlayer({ camera, isMainStream = false, onExpand, isExpanded = false }: VideoPlayerProps) {
  const videoRef = useRef<HTMLVideoElement>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [retryCount, setRetryCount] = useState(0);

  useEffect(() => {
    let peerConnection: any = null;

    const startStream = async () => {
      setIsLoading(true);
      setError(null);

      try {
        // In a real implementation, we would use WebRTC signaling here
        // cameraService.getStream(camera.id, offer) -> answer
        
        // Mocking WebRTC setup
        console.log(`Starting ${isMainStream ? 'Main' : 'Sub'} stream for camera ${camera.name}`);
        
        // Simulating delay
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        if (camera.status === CameraStatus.OFFLINE) {
          throw new Error('Câmera offline');
        }

        setIsLoading(false);
      } catch (err: any) {
        setError(err.message || 'Erro ao carregar stream');
        setIsLoading(false);
      }
    };

    startStream();

    return () => {
      if (peerConnection && typeof peerConnection.close === 'function') {
        peerConnection.close();
      }
    };
  }, [camera.id, camera.status, isMainStream, retryCount]);

  const handleRetry = () => {
    setRetryCount(prev => prev + 1);
  };

  return (
    <div className={cn(
      "relative group w-full h-full bg-black rounded-lg overflow-hidden border border-border",
      isExpanded ? "fixed inset-0 z-50 rounded-none" : ""
    )}>
      {isLoading && (
        <div className="absolute inset-0 flex items-center justify-center bg-black/50 z-10">
          <RefreshCw className="w-8 h-8 animate-spin text-primary" />
        </div>
      )}

      {error ? (
        <div className="absolute inset-0 flex flex-col items-center justify-center bg-slate-900 text-white p-4 text-center">
          <AlertCircle className="w-12 h-12 text-destructive mb-2" />
          <p className="text-sm font-medium">{error}</p>
          <Button variant="outline" size="sm" className="mt-4" onClick={handleRetry}>
            Tentar novamente
          </Button>
        </div>
      ) : (
        <video
          ref={videoRef}
          autoPlay
          muted
          playsInline
          className="w-full h-full object-cover"
        />
      )}

      {/* Overlay info */}
      <div className="absolute top-0 left-0 right-0 p-2 bg-gradient-to-b from-black/70 to-transparent opacity-0 group-hover:opacity-100 transition-opacity">
        <div className="flex justify-between items-center text-white">
          <span className="text-xs font-medium truncate">{camera.name}</span>
          <div className="flex gap-1">
            <Button variant="ghost" size="icon" className="h-6 w-6 text-white hover:bg-white/20" onClick={onExpand}>
              {isExpanded ? <Minimize2 className="h-4 w-4" /> : <Maximize2 className="h-4 w-4" />}
            </Button>
          </div>
        </div>
      </div>

      {/* Status indicator */}
      <div className="absolute bottom-2 left-2 flex items-center gap-1 bg-black/50 px-1.5 py-0.5 rounded text-[10px] text-white">
        <div className={cn(
          "w-2 h-2 rounded-full",
          camera.status === CameraStatus.ONLINE ? "bg-green-500" : "bg-red-500"
        )} />
        {camera.status} {isMainStream ? '(HQ)' : '(LQ)'}
      </div>
    </div>
  );
}
