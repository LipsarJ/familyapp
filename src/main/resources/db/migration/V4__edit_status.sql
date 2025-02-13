ALTER TABLE products DROP COLUMN status;

ALTER TABLE users_products ADD COLUMN status TEXT NOT NULL;

ALTER TABLE products_family ADD COLUMN status TEXT NOT NULL;
