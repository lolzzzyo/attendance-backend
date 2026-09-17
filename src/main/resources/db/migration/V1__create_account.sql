CREATE TABLE account (
                         id BIGSERIAL PRIMARY KEY,

                         first_name VARCHAR(100) NOT NULL,
                         nickname VARCHAR(100),
                         last_name VARCHAR(100) NOT NULL,

                         email VARCHAR(255) NOT NULL UNIQUE,
                         password_hash VARCHAR(255) NOT NULL,

                         role VARCHAR(30) NOT NULL DEFAULT 'USER',
                         active BOOLEAN NOT NULL DEFAULT TRUE,

                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);