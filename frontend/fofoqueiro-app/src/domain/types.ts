export type ID = string;

export interface Tenant {
  id: ID;
  name: string;
  domain: string;
  whiteLabelConfig?: WhiteLabelConfig;
  createdAt: string;
  updatedAt: string;
}

export interface WhiteLabelConfig {
  id?: ID;
  tenantId?: ID;
  logoUrl?: string;
  faviconUrl?: string;
  primaryColor: string;
  secondaryColor: string;
  companyName?: string;
}

export interface User {
  id: ID;
  tenantId: ID;
  name: string;
  email: string;
  role: Role;
  mfaEnabled: boolean;
  active: boolean;
  createdAt: string;
}

export enum Role {
  SUPER_ADMIN = 'SUPER_ADMIN',
  ADMIN_TENANT = 'ADMIN_TENANT',
  OPERATOR = 'OPERATOR',
  USER_FINAL = 'USER_FINAL',
}

export interface Camera {
  id: ID;
  tenantId: ID;
  name: string;
  url: string; // RTSP URL
  gatewayId: ID;
  latitude?: number;
  longitude?: number;
  status: CameraStatus;
  resolution?: string;
  fps?: number;
  bitrate?: number;
  lastHeartbeat?: string;
  privacyMasks: PrivacyMask[];
}

export enum CameraStatus {
  ONLINE = 'ONLINE',
  OFFLINE = 'OFFLINE',
  ERROR = 'ERROR',
}

export interface Gateway {
  id: ID;
  tenantId: ID;
  name: string;
  ipAddress: string;
  status: 'ONLINE' | 'OFFLINE';
}

export interface Recording {
  id: ID;
  cameraId: ID;
  startTime: string;
  endTime: string;
  s3Path: string;
  size: number;
}

export interface AuditLog {
  id: ID;
  tenantId: ID;
  userId: ID;
  action: string;
  resource: string;
  resourceId: string;
  timestamp: string;
  ipAddress: string;
  details: any; // JSONB in backend
}

export interface AlertEvent {
  id: ID;
  tenantId: ID;
  cameraId: ID;
  eventType: string;
  eventTime: string;
  snapshotUrl?: string;
  description?: string;
  acknowledged: boolean;
  createdAt: string;
}

export interface PrivacyMask {
  id: ID;
  cameraId: ID;
  points: { x: number; y: number }[]; // Coordinates for SVG/Canvas
}
