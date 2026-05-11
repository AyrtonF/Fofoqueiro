'use client';

import { useState, useEffect } from 'react';
import { useQueryClient, useQuery, useMutation } from '@tanstack/react-query';
import { Camera, Recording, PrivacyMask } from '@/domain/types';
import { cameraService } from '@/services/camera-service';
import { PlaybackTimeline } from '../PlaybackTimeline';
import { Button } from '@/components/ui/button';
import { Maximize2, Minimize2, Clock, AlertCircle, Video, Trash2 } from 'lucide-react';
import { HlsPlayer } from '../HlsPlayer';
import { PrivacyMaskEditor } from '@/components/modules/lgpd/PrivacyMaskEditor';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogTrigger, DialogFooter } from '@/components/ui/dialog';
import { toast } from 'sonner'; // Assuming toast is available
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';

dayjs.extend(utc);

// Assume RecordingInfo interface is defined elsewhere or here for clarity
interface RecordingInfo {
  id: string;
  url: string;
  startTime: string;
  endTime: string;
}

export default function PlaybackPage({ params }: { params: { cameraId: string } }) {
  const cameraId = params.cameraId;
  const queryClient = useQueryClient();
  const [isFullScreen, setIsFullScreen] = useState(false);
  const [selectedRecording, setSelectedRecording] = useState<RecordingInfo | null>(null);
  const [camera, setCamera] = useState<Camera | null>(null);
  const [isMaskEditorOpen, setIsMaskEditorOpen] = useState(false);
  const [currentMasks, setCurrentMasks] = useState<PrivacyMask[]>([]); // Use PrivacyMask[] type

  // Fetch camera details
  const {
    data: cameraData,
    isLoading: isCameraLoading,
    error: cameraError,
  } = useQuery<Camera>({
    queryKey: ['cameras', cameraId],
    queryFn: () => cameraService.getById(cameraId),
    enabled: !!cameraId,
  });

  // Fetch recordings for the camera
  const {
    data: recordings,
    isLoading: isRecordingsLoading,
    error: recordingsError,
    refetch: refetchRecordings,
  } = useQuery<Recording[]>({
    queryKey: ['camera-recordings', cameraId, dayjs.utc().format('YYYY-MM-DD')], // Include date in key
    queryFn: () => cameraService.getRecordings(cameraId, dayjs.utc().format('YYYY-MM-DD')), // Use service call
    enabled: !!cameraId,
  });

  // Fetch initial privacy masks for the camera
  const {
    data: initialMasks,
    isLoading: isMasksLoading,
    error: masksError,
    refetch: refetchMasks,
  } = useQuery<PrivacyMask[]>({
    queryKey: ['camera-privacy-masks', cameraId],
    queryFn: () => cameraService.getPrivacyMasks(cameraId), // Use service call
    enabled: !!cameraId,
  });

  useEffect(() => {
    if (cameraData) {
      setCamera(cameraData);
    }
  }, [cameraData]);

  useEffect(() => {
    if (recordings && recordings.length > 0) {
      const firstRecording = recordings[0];
      setSelectedRecording({
        id: firstRecording.id,
        url: firstRecording.s3Path,
        startTime: firstRecording.startTime,
        endTime: firstRecording.endTime,
      });
    } else {
      setSelectedRecording(null);
    }
  }, [recordings]);

  useEffect(() => {
    if (initialMasks) {
      setCurrentMasks(initialMasks);
    }
  }, [initialMasks]);

  const toggleFullScreen = () => {
    setIsFullScreen(!isFullScreen);
  };

  const handleRecordingSelect = (recording: RecordingInfo) => {
    setSelectedRecording(recording);
  };

  const handleMasksSave = async (newMasks: PrivacyMask[]) => {
    if (!camera) return;
    try {
      await cameraService.updatePrivacyMasks(camera.id, newMasks); // Use service call
      queryClient.invalidateQueries({ queryKey: ['camera-privacy-masks', cameraId] });
      toast.success('Máscaras de privacidade salvas com sucesso!');
      setIsMaskEditorOpen(false);
    } catch (error: any) {
      toast.error(`Erro ao salvar máscaras: ${error.message || 'Erro desconhecido'}`);
    }
  };

  if (isCameraLoading || isRecordingsLoading || isMasksLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  if (cameraError || recordingsError || masksError) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center p-4">
        <AlertCircle className="h-12 w-12 text-destructive mb-4" />
        <p className="text-lg text-destructive mb-4">
          {cameraError?.message || recordingsError?.message || masksError?.message || 'Erro ao carregar dados de playback.'}
        </p>
        <Button onClick={() => { refetchRecordings(); refetchMasks(); }}>Tentar novamente</Button>
      </div>
    );
  }

  if (!camera) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p>Câmera não encontrada.</p>
      </div>
    );
  }

  return (
    <div className={`flex flex-col h-full ${isFullScreen ? 'fixed inset-0 z-50 bg-black' : ''}`}>
        <div className={`flex justify-between items-center p-4 ${isFullScreen ? 'bg-black/70 text-white' : ''}`}>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Clock className="h-6 w-6" />
            Reprodução - {camera.name}
          </h1>
          <div className="flex items-center gap-2">
            <Button variant="ghost" size="icon" className="text-white hover:bg-white/20" onClick={toggleFullScreen}>
              {isFullScreen ? <Minimize2 className="h-6 w-6" /> : <Maximize2 className="h-6 w-6" />}
            </Button>
            <Dialog open={isMaskEditorOpen} onOpenChange={setIsMaskEditorOpen}>
              <DialogTrigger asChild>
                <Button variant="outline" className={`text-white hover:bg-white/20 ${isFullScreen ? '' : 'hidden'}`}>
                  <Trash2 className="h-5 w-5 mr-2" /> Editar Máscaras
                </Button>
              </DialogTrigger>
              <DialogContent className="sm:max-w-150 max-h-[80vh] overflow-auto">
                <DialogHeader>
                  <DialogTitle>Editar Máscaras de Privacidade</DialogTitle>
                  <DialogDescription>Desenhe ou edite as áreas a serem mascaradas.</DialogDescription>
                </DialogHeader>
                <PrivacyMaskEditor
                  initialMasks={currentMasks as any}
                  onMasksChange={(newMasks) => setCurrentMasks(newMasks as any)}
                  isEditing={true}
                />
                <DialogFooter>
                  <Button variant="outline" onClick={() => setIsMaskEditorOpen(false)}>Cancelar</Button>
                  <Button onClick={() => handleMasksSave(currentMasks)}>Salvar Alterações</Button>
                </DialogFooter>
              </DialogContent>
            </Dialog>
          </div>
        </div>
        <div className={`flex-1 relative ${isFullScreen ? 'p-4' : 'p-0'}`}>
          {selectedRecording ? (
            <HlsPlayer src={selectedRecording.url} />
          ) : (
            <div className="flex items-center justify-center h-full w-full bg-black rounded-lg text-white">
              {isRecordingsLoading ? 'Carregando gravações...' : 'Selecione uma gravação para visualizar'}
            </div>
          )}
        </div>
        <div className={`h-40 ${isFullScreen ? 'bg-black/70' : ''}`}>
          <PlaybackTimeline recordings={recordings || []} />
        </div>
      </div>
    );
  }
