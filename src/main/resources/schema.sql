DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS mpa_ratings CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS film_likes CASCADE;
DROP TABLE IF EXISTS friendships CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS user_reactions_to_reviews CASCADE;
DROP TABLE IF EXISTS film_directors CASCADE;
DROP TABLE IF EXISTS directors CASCADE;

CREATE TABLE IF NOT EXISTS mpa_ratings (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(5) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description VARCHAR(200),
                       release_date DATE,
                       duration INTEGER,
                       mpa_rating_id INTEGER,
                       FOREIGN KEY (mpa_rating_id) REFERENCES mpa_ratings(id)
);

CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       login VARCHAR(50) UNIQUE NOT NULL,
                       name VARCHAR(50),
                       birthday DATE
);

CREATE TABLE IF NOT EXISTS genres (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (
                             film_id INTEGER,
                             genre_id INTEGER,
                             PRIMARY KEY (film_id, genre_id),
                             FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
                             FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_likes (
                       film_id INTEGER,
                       user_id INTEGER,
                       PRIMARY KEY (film_id, user_id),
                       FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendships (
                             user_id INTEGER,
                             friend_id INTEGER,
                             status VARCHAR(20) NOT NULL,
                             PRIMARY KEY (user_id, friend_id),
                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                             FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews (
    review_id INTEGER      AUTO_INCREMENT PRIMARY KEY,
    content   VARCHAR(255) NOT NULL,
    positive  BOOLEAN      NOT NULL,
    user_id   INTEGER      NOT NULL,
    film_id   INTEGER      NOT NULL,
    useful    INTEGER      NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_reactions_to_reviews (
    reaction_id INTEGER     AUTO_INCREMENT PRIMARY KEY,
    user_id     INTEGER     NOT NULL,
    review_id   INTEGER     NOT NULL,
    reaction    VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (review_id) REFERENCES reviews(review_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS directors (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS film_directors (
                           film_id INTEGER,
                           director_id INTEGER,
                           PRIMARY KEY (film_id, director_id),
                           FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
                           FOREIGN KEY (director_id) REFERENCES directors(id) ON DELETE CASCADE
);