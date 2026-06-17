DROP TABLE IF EXISTS inventory;

CREATE TABLE inventory (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price > 0)
);