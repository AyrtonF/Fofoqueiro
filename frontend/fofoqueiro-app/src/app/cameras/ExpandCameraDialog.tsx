"use client";

import { Camera } from '@/domain/types';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Camera as CameraIcon, MapPin, Video } from 'lucide-react';

interface ExpandCameraDialogProps {
  camera: Camera | null;
  onClose: () => void;
}

export function ExpandCameraDialog({ camera, onClose }: ExpandCameraDialogProps) {
  if (!camera) {
    return null;
  }

  return (
    <Dialog open={!!camera} onOpenChange={(open) => !open && onClose()}>
      <DialogContent className="sm:max-w-5xl">
        <DialogHeader>
          <DialogTitle>{camera.name}</DialogTitle>
          <DialogDescription>
            Visualização detalhada da câmera, com informações técnicas e área reservada para o stream ao vivo.
          </DialogDescription>
        </DialogHeader>

        <div className="grid gap-4 lg:grid-cols-[2fr_1fr]">
          <div className="flex min-h-[320px] items-center justify-center rounded-xl border border-dashed border-border bg-slate-950/30">
            <div className="flex flex-col items-center gap-3 text-center text-muted-foreground">
              <Video className="h-12 w-12" />
              <p className="max-w-sm text-sm">
                O backend já expõe o endpoint de stream. Quando o gateway estiver conectado, este espaço poderá receber o player WebRTC/HLS.
              </p>
            </div>
          </div>

          <div className="space-y-4 rounded-xl border border-border bg-card p-4">
            <div className="flex items-center gap-2 text-sm text-muted-foreground">
              <CameraIcon className="h-4 w-4" />
              <span>Gateway {camera.gatewayId}</span>
            </div>
            <div className="flex items-center gap-2 text-sm text-muted-foreground">
              <MapPin className="h-4 w-4" />
              <span>
                {camera.latitude ?? 'Sem latitude'} / {camera.longitude ?? 'Sem longitude'}
              </span>
            </div>
            <div className="text-sm text-muted-foreground">
              Status: <span className="font-medium text-foreground">{camera.status}</span>
            </div>
            <div className="text-sm text-muted-foreground">
              Retenção: <span className="font-medium text-foreground">{camera.recordingRetentionDays ?? '-'} dias</span>
            </div>

            <Button variant="outline" className="w-full" onClick={onClose}>
              Fechar
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}

export default ExpandCameraDialog;
