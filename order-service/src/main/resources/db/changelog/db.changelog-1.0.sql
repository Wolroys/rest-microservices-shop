--liquibase formatted sql

--changeset wolroys:1
create table if not exists orders
(
    id               bigserial
        primary key,
    user_id          bigint,
    order_date       timestamp,
    total_amount     numeric(10, 2),
    status           varchar(20),
    delivery_address varchar(255),
    created_at       timestamp,
    modified_at      timestamp
);

--changeset wolroys:2
create table if not exists order_product
(
    id         bigserial
        primary key,
    order_id   bigint
        references orders,
    product_id bigint,
    quantity   bigint,
    price      numeric(10, 2),
    name       varchar(64)
);


