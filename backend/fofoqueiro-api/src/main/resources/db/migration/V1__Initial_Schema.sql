-- V1__Initial_Schema.sql

-- Table for Tenants
CREATE TABLE tenants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    domain VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

-- Table for Roles
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

-- Table for Users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    mfa_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- Table for Cameras
CREATE TABLE cameras (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    rtsp_url VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    fps INTEGER,
    bitrate BIGINT,
    recording_retention_days INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- Table for Gateways
CREATE TABLE gateways (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    ip_address VARCHAR(255) NOT NULL UNIQUE,
    location TEXT,
    status VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- Table for Recordings
CREATE TABLE recordings (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    camera_id BIGINT NOT NULL,
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    s3_path VARCHAR(255) NOT NULL,
    duration_seconds BIGINT NOT NULL,
    file_size_mb DOUBLE PRECISION NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    FOREIGN KEY (camera_id) REFERENCES cameras(id)
);

-- Table for Audit Logs
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    action VARCHAR(255) NOT NULL,
    entity_name VARCHAR(255) NOT NULL,
    entity_id BIGINT NOT NULL,
    "timestamp" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ip_address VARCHAR(255) NOT NULL,
    details TEXT,
    hash VARCHAR(255) NOT NULL UNIQUE,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- Table for Privacy Masks
CREATE TABLE privacy_masks (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    camera_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    coordinates TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    FOREIGN KEY (camera_id) REFERENCES cameras(id)
);

-- Table for White Label Configurations
CREATE TABLE white_label_configs (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL UNIQUE,
    logo_url VARCHAR(255),
    primary_color VARCHAR(7),
    secondary_color VARCHAR(7),
    favicon_url VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);
