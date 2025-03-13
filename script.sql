DROP TABLE IF EXISTS products CASCADE;
CREATE TABLE products (
                          product_id BIGSERIAL PRIMARY KEY,
                          product_name VARCHAR(255) NOT NULL,
                          product_price NUMERIC(10,2) NOT NULL CHECK (product_price >= 0),
                          description TEXT,
                          img_url TEXT,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);