-- V2__Insert_Default_Data.sql

-- Insert Default Roles
INSERT INTO roles (id, name, description, created_at, updated_at)
VALUES (1, 'ADMIN', 'Administrador do Sistema', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert Default Tenant
INSERT INTO tenants (id, name, domain, is_active, created_at, updated_at)
VALUES (1, 'Fofoqueiro', 'localhost', TRUE, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert Default White Label Config
INSERT INTO white_label_configs (id, tenant_id, logo_url, primary_color, secondary_color, favicon_url, created_at, updated_at)
VALUES (1, 1, '/images/logo.png', '#0f172a', '#3b82f6', '/images/favicon.ico', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert Default Admin User
-- Password is 'admin123' hashed with BCrypt
INSERT INTO users (id, tenant_id, email, password, first_name, last_name, is_active, mfa_enabled, created_at, updated_at)
VALUES (1, 1, 'ayrtonleonardo14@gmail.com', '$2a$10$78/8yX.G2/U.M4iPzVv9Ou4nO8kL9u5J5oWf3j1o8p7q6r5s4t3u2', 'Admin', 'Admin', TRUE, FALSE, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;
