CREATE TABLE expense(
    id BINARY(16) NOT NULL,
    date TIMESTAMP NOT NULL,
    amount FLOAT NOT NULL,
    vat FLOAT NOT NULL,
    reason VARCHAR(128),
    PRIMARY KEY(id)
);