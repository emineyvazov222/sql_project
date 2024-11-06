CREATE TABLE IF NOT EXISTS users
(
    id           BIGSERIAL PRIMARY KEY,
    user_name    VARCHAR(255) NOT NULL,
    user_surname VARCHAR(255) NOT NULL,
    age          INT CHECK (age > 20),
    pin          VARCHAR(7)   NOT NULL UNIQUE CHECK (LENGTH(pin) BETWEEN 5 AND 7),
    created_at   TIMESTAMP(3) NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP(3) NOT NULL DEFAULT NOW(),
    created_by   BIGINT REFERENCES users (id),
    updated_by   BIGINT REFERENCES users (id)
    );

INSERT INTO users (user_name, user_surname, age, pin)
VALUES ('John', 'Doe', 25, '12345');


INSERT INTO users (user_name, user_surname, age, pin, created_by, updated_by)
VALUES ('Jane', 'Smith', 30, '54321', 1, 1);



CREATE TABLE IF NOT EXISTS posts
(
    post_id          BIGSERIAL PRIMARY KEY,
    post_description VARCHAR(255) NOT NULL,
    history_date     DATE,
    created_at       TIMESTAMP(5) NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP(5) NOT NULL DEFAULT NOW(),
    created_by       BIGINT REFERENCES users (id),
    updated_by       BIGINT REFERENCES users (id),
    user_id          BIGINT REFERENCES users (id) ON DELETE CASCADE
    );

INSERT INTO posts (post_description, history_date, created_by, updated_by, user_id)
VALUES ('This is the first post', '2024-11-06', 1, 1, 1);

INSERT INTO posts (post_description, created_by, updated_by, user_id)
VALUES ('Another interesting post', 2, 2, 2);



CREATE TABLE IF NOT EXISTS comments
(
    comment_id   BIGSERIAL PRIMARY KEY,
    comment_text VARCHAR(255) NOT NULL,
    status       VARCHAR(20)           DEFAULT 'active',
    created_at   TIMESTAMP(5) NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP(5) NOT NULL DEFAULT NOW(),
    created_by   BIGINT REFERENCES users (id),
    updated_by   BIGINT REFERENCES users (id),
    post_id      BIGINT REFERENCES posts (post_id) ON DELETE CASCADE
    );

INSERT INTO comments (comment_text, created_by, updated_by, post_id)
VALUES ('Great post!', 2, 2, 1);

INSERT INTO comments (comment_text, status, created_by, updated_by, post_id)
VALUES ('I completely agree!', 'active', 1, 1, 2);



CREATE TABLE IF NOT EXISTS likes
(
    like_id    BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    post_id    BIGINT REFERENCES posts (post_id) ON DELETE CASCADE,
    comment_id BIGINT REFERENCES comments (comment_id) ON DELETE CASCADE,
    created_at TIMESTAMP(3) NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP(3) NOT NULL DEFAULT NOW(),
    created_by BIGINT REFERENCES users(id) ON DELETE CASCADE,
    updated_by BIGINT REFERENCES users(id) ON DELETE CASCADE
    );

INSERT INTO likes (user_id, post_id,comment_id,created_by,updated_by)
VALUES (1, 1, NULL,1,1);
INSERT INTO likes (user_id, post_id, comment_id, created_by, updated_by)
VALUES (2, NULL, 1, 2, 2);


--User-ların hər bir Postu ilə birlikdə göstərilməsi

SELECT u.id         AS user_id,
       u.user_name,
       u.user_surname,
       p.post_id,
       p.post_description,
       p.created_at AS post_created_at
FROM users u
         INNER JOIN
     posts p ON u.id = p.user_id;


--User-ların hər bir Comment -i ilə birlikdə göstərilməsi

SELECT u.id         AS user_id,
       u.user_name,
       u,
       user_surname,
       c.comment_id,
       c.comment_text,
       c.created_at AS comment_created_at
FROM users u
         INNER JOIN comments c on u.id = c.created_by;


--User-lar, onların Postları və postların Comment-lerinin göstərilməsi

SELECT
    u.id AS user_id,
    u.user_name,
    u.user_surname,
    p.post_id,
    p.post_description,
    c.comment_id,
    c.comment_text,
    c.created_at AS comment_created_at
FROM
    users u
        INNER JOIN
    posts p ON u.id = p.user_id
        LEFT JOIN
    comments c ON p.post_id = c.post_id;



--Hər Bir Postun Comment-leri ilə birlikdə göstərilməsi

SELECT
    p.post_id,
    p.post_description,
    c.comment_id,
    c.comment_text,
    c.created_at AS comment_created_at
FROM
    posts p
        INNER JOIN
    comments c ON p.post_id = c.post_id;










