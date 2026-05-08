import api from './api-client';
import { Camera, CameraStatus, Recording, AuditLog, PrivacyMask } from '../domain/types';

// Interfaces for specific endpoints
export interface CameraHealth {
  id: string;
  fps: number;
  bitrate: number;
  status: CameraStatus;
  online: boolean;
}

export const cameraService = {
  getAll: async (): Promise<Camera[]> => {
    const response = await api.get<Camera[]>('/cameras');
    return response.data;
  },
  getById: async (id: string): Promise<Camera> => {
    const response = await api.get<Camera>(`/cameras/${id}`);
    return response.data;
  },
  getStream: async (id: string, signalData: any): Promise<any> => {
    // This endpoint is for WebRTC signaling setup.
    // The actual stream handling (SDP negotiation, ICE candidates) would occur here.
    // Actual stream data (like Blob or MediaStream) is typically handled client-side
    // via WebRTC PeerConnection, not returned directly by this API call.
    // This method likely initiates the signaling process.
    // TODO: Implement actual WebRTC signaling logic.
    console.warn('WebRTC signaling for camera stream is not fully implemented.');
    const response = await api.post(`/cameras/stream/${id}`, signalData);
    return response.data; // Assuming it returns signaling data or a confirmation
  },
  getHealth: async (): Promise<CameraHealth[]> => {
    const response = await api.get<CameraHealth[]>('/cameras/health');
    return response.data;
  },
  create: async (data: Partial<Camera>): Promise<Camera> => {
    const response = await api.post<Camera>('/cameras', data);
    return response.data;
  },
  update: async (id: string, data: Partial<Camera>): Promise<Camera> => {
    const response = await api.put<Camera>(`/cameras/${id}`, data);
    return response.data;
  },
  delete: async (id: string): Promise<void> => {
    await api.delete(`/cameras/${id}`);
  },
  testConnection: async (url: string): Promise<{ success: boolean; message: string }> => {
    const response = await api.post('/cameras/test-connection', { url });
    return response.data;
  },
  getRecordings: async (cameraId: string, date: string): Promise<Recording[]> => {
    const response = await api.get<Recording[]>(`/cameras/${cameraId}/recordings`, { params: { date } });
    return response.data;
  },
  getPrivacyMasks: async (cameraId: string): Promise<PrivacyMask[]> => {
    const response = await api.get<PrivacyMask[]>(`/cameras/${cameraId}/privacy-masks`);
    return response.data;
  },
  updatePrivacyMasks: async (cameraId: string, masks: PrivacyMask[]): Promise<void> => {
    await api.put(`/cameras/${cameraId}/privacy-masks`, masks);
  },
};
