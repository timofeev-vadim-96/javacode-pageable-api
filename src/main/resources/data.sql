INSERT INTO users (email, password, role)
VALUES ('testAdmin@gmail.com', 'password', 'ROLE_ADMIN'),
       ('testUser@gmail.com', 'password', 'ROLE_USER'),
       ('moderator@gmail.com', 'password', 'ROLE_MODERATOR'),
       ('user4@example.com', 'password', 'ROLE_USER'),
       ('user5@example.com', 'password', 'ROLE_USER'),
       ('user6@example.com', 'password', 'ROLE_USER'),
       ('user7@example.com', 'password', 'ROLE_USER'),
       ('user8@example.com', 'password', 'ROLE_USER'),
       ('user9@example.com', 'password', 'ROLE_USER'),
       ('user10@example.com', 'password', 'ROLE_USER');

insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);