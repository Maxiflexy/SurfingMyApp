
INSERT INTO back_office_user (id, user_id, email, first_name, last_name, password, status, deleted, created_by, created_date, last_modified_by, last_modified_date, version)
VALUES
(1, 'AH12345678', 'existing.user@example.com', 'Existing', 'User', '$2a$10$encrypted.password.hash', 'ACTIVE', false, 'system', '2025-01-01 10:00:00', 'system', '2025-01-01 10:00:00', 0),
(2, 'AH87654321', 'inactive.user@example.com', 'Inactive', 'User', '$2a$10$another.encrypted.password', 'INACTIVE', false, 'system', '2025-01-02 10:00:00', 'system', '2025-01-02 10:00:00', 0),
(3, 'AH11111111', 'pending.user@example.com', null, null, null, 'INACTIVE', false, 'system', '2025-01-03 10:00:00', 'system', '2025-01-03 10:00:00', 0);