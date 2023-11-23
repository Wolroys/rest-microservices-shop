--liquibase formatted sql

--changeset wolroys:1
create table if not exists cart
(
    id bigserial primary key,
    user_id     integer not null,
    total_price numeric(10, 2),
    created_at  timestamp,
    modified_at timestamp
);

--changeset wolroys:2
create table cart_product
(
    id          bigserial
        primary key,
    cart_id     integer not null
        references cart,
    product_id  integer not null,
    quantity    integer,
    created_at  timestamp,
    modified_at timestamp,
    price       numeric(10, 2)
);


