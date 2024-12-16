CREATE TABLE users(
        id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
        name VARCHAR(255),
        login VARCHAR(255),
        email VARCHAR(255),
        roles TEXT[]
);