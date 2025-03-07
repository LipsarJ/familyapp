CREATE TABLE invitations
(
    id          BIGSERIAL PRIMARY KEY,
    family_id   BIGINT    NOT NULL,
    user_id     BIGINT    NOT NULL,
    status      TEXT      NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_invitations_family FOREIGN KEY (family_id) REFERENCES families (id) ON DELETE CASCADE,
    CONSTRAINT fk_invitations_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
