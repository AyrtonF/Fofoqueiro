-- V3__Add_Camera_Gateway_And_Indexes.sql

-- Add gateway association to cameras so the frontend can bind devices to media gateways.
ALTER TABLE cameras
    ADD COLUMN IF NOT EXISTS gateway_id BIGINT;

ALTER TABLE cameras
    ADD CONSTRAINT fk_cameras_gateway
        FOREIGN KEY (gateway_id) REFERENCES gateways(id);

CREATE INDEX IF NOT EXISTS idx_cameras_tenant_id ON cameras (tenant_id);
CREATE INDEX IF NOT EXISTS idx_cameras_gateway_id ON cameras (gateway_id);
CREATE INDEX IF NOT EXISTS idx_recordings_tenant_camera_start_time ON recordings (tenant_id, camera_id, start_time);
CREATE INDEX IF NOT EXISTS idx_audit_logs_tenant_timestamp ON audit_logs (tenant_id, "timestamp");
CREATE INDEX IF NOT EXISTS idx_privacy_masks_tenant_camera ON privacy_masks (tenant_id, camera_id);
