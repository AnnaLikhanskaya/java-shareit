-- Удаление таблиц, если они существуют
DROP TABLE IF EXISTS bookings, items, requests, users, comments;

-- Создание таблицы users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

-- Создание таблицы requests
CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(1024) NOT NULL,
    requestor_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_request PRIMARY KEY (id)
);

-- Создание таблицы items
CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1024) NOT NULL,
    available BOOL,
    owner_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    request_id BIGINT REFERENCES requests(id) ON DELETE CASCADE,
    CONSTRAINT pk_item PRIMARY KEY (id)
);

-- Создание таблицы bookings
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id BIGINT REFERENCES items(id) ON DELETE CASCADE,
    booker_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(128),
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

-- Создание таблицы comments
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(4096) NOT NULL,
    item_id BIGINT REFERENCES items(id) ON DELETE CASCADE,
    author_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);