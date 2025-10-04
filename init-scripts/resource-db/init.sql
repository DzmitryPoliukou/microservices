-- Resource Service Database Schema
CREATE TABLE IF NOT EXISTS resources (
    id SERIAL PRIMARY KEY,
    file BYTEA NOT NULL
);

