--liquibase formatted sql

--changeset wolroys:1
create table if not exists users
(
    id          bigserial
        primary key,
    username    varchar(50)  not null,
    password    varchar(255) not null,
    email       varchar(50)
        unique,
    created_at  timestamp default CURRENT_TIMESTAMP,
    modified_at timestamp default CURRENT_TIMESTAMP,
    role        varchar(32)
);


