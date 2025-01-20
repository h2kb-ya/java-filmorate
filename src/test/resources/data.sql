DELETE
FROM film_likes;
DELETE
FROM friendships;
DELETE
FROM users;
DELETE
FROM films;
DELETE
FROM genres;
DELETE
FROM mpa_ratings;
DELETE
FROM directors;

INSERT INTO mpa_ratings (name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
VALUES ('Inception', 'A thief is given a chance to erase his criminal past.', '2010-07-16', 148, 3),
       ('The Matrix', 'A hacker learns the shocking truth about reality.', '1999-03-31', 136, 4),
       ('Interstellar', 'Explorers travel through a wormhole to find a new home.', '2014-11-07', 169, 3),
       ('The Shawshank Redemption', 'Two men bond and find solace in prison.', '1994-09-23', 142, 4);

INSERT INTO genres (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO film_genres (film_id, genre_id)
VALUES (1, 4),
       (1, 1),
       (2, 4),
       (3, 5),
       (4, 2);

INSERT INTO users (email, login, name, birthday)
VALUES ('TestUser1@example.com', 'TestUser1', 'Test User One', '1990-05-15'),
       ('TestUser2@example.com', 'TestUser2', 'Test User Two', '1990-05-15'),
       ('TestUser3@example.com', 'TestUser3', 'Test User Three', '1990-05-15'),
       ('TestUser4@example.com', 'TestUser4', 'Test User Four', '1990-05-15');


INSERT INTO film_likes (film_id, user_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 2),
       (2, 3),
       (3, 3),
       (4, 2);

INSERT INTO friendships (user_id, friend_id, status)
VALUES (1, 2, 'PENDING'),
       (1, 3, 'PENDING'),
       (2, 3, 'PENDING'),
       (2, 4, 'PENDING');

INSERT INTO directors (name)
VALUES ('Кристофер Нолан'),
       ('Лилли Вачовски'),
       ('Лана Вачовски'),
       ('Фрэнк Дарабонт');

INSERT INTO film_directors (film_id, director_id)
VALUES (1, 1),
       (2, 2),
       (2, 3),
       (3, 1),
       (4, 4);