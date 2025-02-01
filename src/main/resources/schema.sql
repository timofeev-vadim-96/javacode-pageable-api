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