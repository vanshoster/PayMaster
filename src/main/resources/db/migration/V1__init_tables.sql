create schema if not exists payroll;

CREATE TABLE payroll.roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO payroll.roles (name) VALUES ('ADMIN');

CREATE TABLE payroll.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INTEGER NOT NULL REFERENCES payroll.roles(id)
);
