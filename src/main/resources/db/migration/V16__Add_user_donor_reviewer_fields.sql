ALTER TABLE users
ADD COLUMN donor TINYINT NOT NULL DEFAULT 0
ADD COLUMN reviewer TINYINT NOT NULL DEFAULT 0;