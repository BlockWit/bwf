CREATE TABLE roles (
  id                       SERIAL PRIMARY KEY,
  `name`                   VARCHAR(150) NOT NULL UNIQUE,
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE permissions (
  id                       SERIAL PRIMARY KEY,
  name                     VARCHAR(150) NOT NULL UNIQUE,
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE roles_to_accounts (
  role_id                  BIGINT UNSIGNED NOT NULL,
  target_id                BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (role_id, target_id)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE permissions_to_roles (
  permission_id            BIGINT UNSIGNED NOT NULL,
  role_id                  BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (permission_id, role_id)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE permissions_to_accounts (
  permission_id            BIGINT UNSIGNED NOT NULL,
  account_id               BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (permission_id, account_id)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE accounts (
  id                        SERIAL PRIMARY KEY,
  login                     VARCHAR(100) NOT NULL UNIQUE,
  email                     VARCHAR(100) NOT NULL UNIQUE,
  hash                      VARCHAR(60),
  confirmation_status       ENUM('CONFIRMED', 'WAIT_CONFIRMATION') NOT NULL,
  confirm_code              VARCHAR(100)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;