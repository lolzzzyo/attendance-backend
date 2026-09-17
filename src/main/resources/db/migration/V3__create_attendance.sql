CREATE TABLE attendance (
                            id BIGSERIAL PRIMARY KEY,

                            account_id BIGINT NOT NULL,
                            event_id BIGINT NOT NULL,

                            status VARCHAR(30) NOT NULL,

                            date_changed TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_attendance_account
                                FOREIGN KEY (account_id)
                                    REFERENCES account(id),

                            CONSTRAINT fk_attendance_event
                                FOREIGN KEY (event_id)
                                    REFERENCES event(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT uq_attendance_account_event
                                UNIQUE(account_id, event_id)
);