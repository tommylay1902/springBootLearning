create table customer(
    id BIGSERIAL  PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    age INT NOT NULL,
    gender TEXT NOT NULL
);