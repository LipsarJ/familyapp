DROP TABLE users_tasks;
DROP TABLE tasks_family;

ALTER TABLE tasks ADD COLUMN family_id BIGINT, ADD CONSTRAINT fk_tasks_family FOREIGN KEY (family_id) REFERENCES families(id);