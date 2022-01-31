--liquibase formatted sql
--changeset egor:1.0.1
create table if not exists users
(
    id
    bigserial
    primary
    key,
    username
    varchar
    unique,
    password
    varchar,
    role
    varchar,
    status
    varchar
    default
    'ACTIVE'
);



create table if not exists files
(
    id
    bigserial
    primary
    key,
    location
    varchar,
    name
    varchar
);

create table if not exists events
(
    id
    bigserial
    primary
    key,
    created
    timestamp
    default
    current_timestamp,
    operation
    varchar,
    id_file
    bigint,
    id_user
    bigint
);

--liquibase formatted sql
--changeset egor:1.0.2

INSERT INTO public.users (username, password, role)
VALUES ('theadmin', '$2a$04$h0DTZTJ67Y95z8FDpgZzZu6t2mhOEmD9M1aLSr5QFrr6ZEuX6Kvfu', 'ADMIN'),
       ('themoderator', '$2a$04$h0DTZTJ67Y95z8FDpgZzZu6t2mhOEmD9M1aLSr5QFrr6ZEuX6Kvfu', 'MODERATOR'),
       ('theuser', '$2a$04$h0DTZTJ67Y95z8FDpgZzZu6t2mhOEmD9M1aLSr5QFrr6ZEuX6Kvfu', 'USER');








