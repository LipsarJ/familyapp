CREATE TABLE users_products
(
    user_id BIGSERIAL NOT NULL,
    product_id BIGSERIAL NOT NULL,
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);