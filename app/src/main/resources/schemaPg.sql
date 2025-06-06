DROP TABLE IF EXISTS urls CASCADE;

CREATE TABLE urls (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP (0)
);


DROP TABLE IF EXISTS url_checks;

CREATE TABLE url_checks (
    id SERIAL PRIMARY KEY,
    status_code BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    h1 VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    url_id BIGINT REFERENCES urls(id) NOT NULL,
    created_at TIMESTAMP (0)
);