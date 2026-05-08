import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { useState } from 'react';
import { Camera } from '@/domain/types';
import { CameraGrid } from '../monitoring/CameraGrid';

interface ExpandCameraDialogProps {
  camera: Camera | null;
  onClose: () => void;
}

export function ExpandCameraDialog({ camera, onClose }: ExpandCameraDialogProps) {
  const isOpen = !!camera;

  return (
    <Dialog open={isOpen} onOpenChange={(open) => !open && onClose()}>
      <DialogContent className="sm:max-w-5xl max-h-[80vh] overflow-auto">
        <DialogHeader>
          <DialogTitle>Visualizando: {camera?.name}</DialogTitle>
          <DialogDescription>Visualização ao vivo da câmera.</DialogDescription>
        </DialogHeader>
        {camera && (
          <div className="w-full h-full aspect-video">
            <CameraGrid cameras={[camera]} />
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
}
