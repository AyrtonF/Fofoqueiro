-- V4__Add_Operational_Modules.sql

CREATE TABLE IF NOT EXISTS group_locations (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    parent_group_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    FOREIGN KEY (parent_group_id) REFERENCES group_locations(id)
);

CREATE TABLE IF NOT EXISTS alert_events (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    camera_id BIGINT NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    event_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    snapshot_url VARCHAR(500),
    description TEXT,
    acknowledged BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    FOREIGN KEY (camera_id) REFERENCES cameras(id)
);

CREATE TABLE IF NOT EXISTS user_sessions (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    token_id VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_activity_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS health_metrics (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    camera_id BIGINT NOT NULL,
    online BOOLEAN NOT NULL DEFAULT FALSE,
    fps INTEGER,
    bitrate BIGINT,
    recording_confidence DOUBLE PRECISION,
    measured_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    FOREIGN KEY (camera_id) REFERENCES cameras(id)
);

CREATE INDEX IF NOT EXISTS idx_group_locations_tenant_id ON group_locations (tenant_id);
CREATE INDEX IF NOT EXISTS idx_alert_events_tenant_camera_time ON alert_events (tenant_id, camera_id, event_time);
CREATE INDEX IF NOT EXISTS idx_user_sessions_tenant_user ON user_sessions (tenant_id, user_id);
CREATE INDEX IF NOT EXISTS idx_health_metrics_tenant_camera_time ON health_metrics (tenant_id, camera_id, measured_at);
