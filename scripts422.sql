CREATE TABLE car
(
    id    INTEGER PRIMARY KEY,
    brand TEXT,
    model TEXT,
    price MONEY
);

CREATE TABLE person
(
    id         INTEGER PRIMARY KEY,
    name       TEXT,
    age        INTEGER,
    permission BOOLEAN,
    car_id     INTEGER REFERENCES car (id)
);


