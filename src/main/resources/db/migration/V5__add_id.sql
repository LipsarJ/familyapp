ALTER TABLE users_products
DROP CONSTRAINT users_products_pkey,
    ADD COLUMN id BIGSERIAL PRIMARY KEY;

ALTER TABLE products_family
DROP CONSTRAINT products_family_pkey,
    ADD COLUMN id BIGSERIAL PRIMARY KEY;