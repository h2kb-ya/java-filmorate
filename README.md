# **java-filmorate**

### Database schema
![Database Schema](src/main/resources/static/images/dbDiagram.png)
```dbml
// Docs: https://dbml.dbdiagram.io/docs

Table films {
  id SERIAL [primary key]
  name varchar(255) [not null]
  description varchar(200) 
  release_date date
  duration integer
  mpa_rating_id integer [ref: > mpa_ratings.id]
}

Table users {
  id SERIAL [primary key]
  email varchar(255) [unique, not null]
  login varchar(50) [unique, not null]
  name varchar(50)
  birthday date
}

Table mpa_ratings {
  id SERIAL [primary key]
  rating varchar(5) [unique, not null]
}

Table genres {
  id SERIAL [primary key]
  name varchar(100) [unique, not null]
}

Table film_genres {
  film_id integer
  genre_id integer
  indexes {
    (film_id, genre_id) [pk]
  }
}

Table likes {
  film_id integer
  user_id integer
  indexes {
    (film_id, user_id) [pk]
  }
}

Table friendships {
  user_id integer
  friend_id integer
  status varchar(20) [not null]
  indexes {
    (user_id, friend_id) [pk]
  }
}

Ref: film_genres.film_id > films.id [on delete cascade]
Ref: film_genres.genre_id > genres.id [on delete cascade]
Ref: likes.film_id > films.id [on delete cascade]
Ref: likes.user_id > users.id [on delete cascade]
Ref: friendships.user_id > users.id [on delete cascade]
Ref: friendships.friend_id > users.id [on delete cascade]
```
