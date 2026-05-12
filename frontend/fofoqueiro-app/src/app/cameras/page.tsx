'use client';

import { useState } from 'react';
import { useQuery, useQueryClient, useMutation } from '@tanstack/react-query';
import { Camera } from '@/domain/types';
import { cameraService } from '@/services/camera-service';
import { AddCameraForm } from './AddCameraForm';
import { createCameraColumns } from './cameraColumns';
import { DataTable } from '@/components/ui/data-table';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogTrigger, DialogFooter } from '@/components/ui/dialog';
import { ExpandCameraDialog } from './ExpandCameraDialog';
import { EditCameraDialog } from './EditCameraDialog';
import { toast } from 'sonner';

export default function CameraManagementPage() {
  const queryClient = useQueryClient();
  const [isAddCameraDialogOpen, setIsAddCameraDialogOpen] = useState(false);
  const [expandedCamera, setExpandedCamera] = useState<Camera | null>(null);
  const [editingCamera, setEditingCamera] = useState<Camera | null>(null);
  const [deletingCamera, setDeletingCamera] = useState<Camera | null>(null);

  const {
    data: cameras,
    isLoading,
    error,
  } = useQuery<Camera[]>({
    queryKey: ['cameras'],
    queryFn: cameraService.getAll,
    staleTime: 30000,
  });

  const { mutate: deleteCameraMutation, isPending: isDeleting } = useMutation({
    mutationFn: (id: string) => cameraService.delete(id),
    onSuccess: () => {
      toast.success('Câmera deletada com sucesso!');
      queryClient.invalidateQueries({ queryKey: ['cameras'] });
      setDeletingCamera(null);
    },
    onError: (error: any) => {
      toast.error(`Erro ao deletar câmera: ${error.message || 'Erro desconhecido'}`);
    },
  });

  const handleDeleteCamera = (camera: Camera) => {
    setDeletingCamera(camera);
  };

  const confirmDelete = () => {
    if (deletingCamera) {
      deleteCameraMutation(deletingCamera.id);
    }
  };

  const columns = createCameraColumns({
    onExpand: setExpandedCamera,
    onEdit: setEditingCamera,
    onDelete: handleDeleteCamera,
  });

  return (
    <div className="w-full p-6">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold">Gerenciamento de Câmeras</h1>
          <Dialog open={isAddCameraDialogOpen} onOpenChange={setIsAddCameraDialogOpen}>
            <DialogTrigger asChild>
              <Button>Adicionar Nova Câmera</Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-106.25">
              <DialogHeader>
                <DialogTitle>Adicionar Câmera</DialogTitle>
                <DialogDescription>Preencha os detalhes para adicionar uma nova câmera.</DialogDescription>
              </DialogHeader>
              <AddCameraForm onSuccess={() => setIsAddCameraDialogOpen(false)} />
            </DialogContent>
          </Dialog>
        </div>

        {isLoading && <p>Carregando câmeras...</p>}
        {error && <p className="text-destructive">Erro ao carregar câmeras: {error.message}</p>}

        {cameras && (
          <DataTable columns={columns} data={cameras} />
        )}

        <ExpandCameraDialog camera={expandedCamera} onClose={() => setExpandedCamera(null)} />
        <EditCameraDialog camera={editingCamera} onClose={() => setEditingCamera(null)} />

        <Dialog open={!!deletingCamera} onOpenChange={(open: boolean) => !open && setDeletingCamera(null)}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Confirmar Exclusão</DialogTitle>
              <DialogDescription>
                Você tem certeza que deseja excluir a câmera "{deletingCamera?.name}"? Esta ação não pode ser desfeita.
              </DialogDescription>
            </DialogHeader>
            <DialogFooter>
              <Button variant="outline" onClick={() => setDeletingCamera(null)}>Cancelar</Button>
              <Button variant="secondary" onClick={confirmDelete} disabled={isDeleting}>
                {isDeleting ? 'Excluindo...' : 'Excluir'}
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>
    );
  }
