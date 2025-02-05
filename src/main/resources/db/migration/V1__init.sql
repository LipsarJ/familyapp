CREATE TABLE families
(
    id          BIGSERIAL PRIMARY KEY               NOT NULL,
    name        TEXT                                NOT NULL,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE users
(
    id          BIGSERIAL PRIMARY KEY               NOT NULL,
    username    TEXT                                NOT NULL,
    email       TEXT                                NOT NULL,
    lastname    TEXT                                NOT NULL,
    middlename  TEXT,
    firstname   TEXT                                NOT NULL,
    password    TEXT                                NOT NULL,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE products
(
    id          BIGSERIAL PRIMARY KEY               NOT NULL,
    name        TEXT                                NOT NULL,
    status      TEXT                                NOT NULL,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL

);
CREATE TABLE tasks
(
    id          BIGSERIAL PRIMARY KEY               NOT NULL,
    title       TEXT                                NOT NULL,
    description TEXT                                NOT NULL,
    status      TEXT                                NOT NULL,
    user_id     BIGINT,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE users_family
(
    user_id   BIGSERIAL NOT NULL,
    family_id BIGSERIAL NOT NULL,
    PRIMARY KEY (user_id, family_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (family_id) REFERENCES families (id)
);

CREATE TABLE products_family
(
    product_id BIGSERIAL NOT NULL,
    family_id  BIGSERIAL NOT NULL,
    PRIMARY KEY (product_id, family_id),
    FOREIGN KEY (product_id) REFERENCES products (id),
    FOREIGN KEY (family_id) REFERENCES families (id)
);

CREATE TABLE tasks_family
(
    task_id   BIGSERIAL NOT NULL,
    family_id BIGSERIAL NOT NULL,
    PRIMARY KEY (task_id, family_id),
    FOREIGN KEY (task_id) REFERENCES tasks (id),
    FOREIGN KEY (family_id) REFERENCES families (id)
);

CREATE TABLE users_tasks
(
    user_id BIGSERIAL NOT NULL,
    task_id BIGSERIAL NOT NULL,
    PRIMARY KEY (user_id, task_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (task_id) REFERENCES tasks (id)
);