CREATE DATABASE product_market;
create table product
(
    id          SERIAL PRIMARY KEY,
    name        varchar(25),
    price        double precision,
    is_promotional boolean
);
------------------------------------------------------------
create table discount_card
(
    id   SERIAL PRIMARY KEY,
    name        varchar(25),
    discount int,
    number  int
);

-- DROP DATABASE product_market;
-- drop table discount_card;