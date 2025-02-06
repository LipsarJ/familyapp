CREATE TABLE refresh_token (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGSERIAL,
                               token TEXT NOT NULL,
                               expiry_date TIMESTAMP NOT NULL,
                               FOREIGN KEY (user_id) REFERENCES users(id)
);