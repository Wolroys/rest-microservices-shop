--liquibase formatted sql

--changeset wolroys:1
create table if not exists category
(
    id          bigserial
        primary key,
    name        varchar(255) not null,
    created_at  timestamp,
    modified_at timestamp
);

--changeset wolroys:2
create table if not exists product
(
    id   bigserial primary key,
    name  char(255) not null,
    description text,
    price       numeric(10, 2)                                       not null,
    category_id bigint
            references category(id),
    seller_id   bigint                                               not null,
    quantity    integer default 1,
    created_at  timestamp,
    modified_at timestamp
);

