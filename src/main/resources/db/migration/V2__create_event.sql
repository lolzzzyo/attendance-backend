CREATE TABLE event (
                       id BIGSERIAL PRIMARY KEY,

                       external_id VARCHAR(100) UNIQUE,

                       start_datetime TIMESTAMP NOT NULL,
                       end_datetime TIMESTAMP NOT NULL,

                       location VARCHAR(500),
                       description TEXT,

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);