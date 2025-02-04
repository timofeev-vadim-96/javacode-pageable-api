create table if not exists authors (
    id bigserial primary key,
    full_name varchar(255) not null unique
);

create table if not exists genres (
    id bigserial primary key,
    name varchar(255) not null unique
);

create table if not exists books (
    id bigserial primary key,
    title varchar(255) UNIQUE NOT NULL,
    author_id bigint references authors (id) on delete cascade
);

create table if not exists books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

create table if not exists users
(
    id       bigserial primary key,
    email    varchar(255) not null unique,
    password varchar(255) not null,
    role     varchar(255) not null
    constraint users_role_check
    check (role IN ('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')),
    is_blocked boolean default false
    );