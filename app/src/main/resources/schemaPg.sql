DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP
);


DROP TABLE IF EXISTS url_checks;

CREATE TABLE url_checks (
    id SERIAL PRIMARY KEY,
    status_code BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    h1 VARCHAR(200) NOT NULL,
    description VARCHAR(500) NOT NULL,
    url_id BIGINT REFERENCES urls(id),
    created_at TIMESTAMP
);