DROP TABLE IF EXISTS compilations_events;
DROP TABLE IF EXISTS compilations;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events (
    id INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation VARCHAR NOT NULL,
    category INTEGER REFERENCES categories(id),
    description VARCHAR NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    lon FLOAT,
    lat FLOAT,
    paid BOOLEAN,
    participant_limit INTEGER,
    request_moderation BOOLEAN,
    title VARCHAR NOT NULL,
    initiator INTEGER REFERENCES users(id) ON delete CASCADE,
    views INTEGER,
    state VARCHAR,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    created_on TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS requests (
    created TIMESTAMP WITHOUT TIME ZONE,
    event INTEGER REFERENCES events(id) ON delete CASCADE,
    id INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    requester INTEGER REFERENCES users(id) ON delete CASCADE,
    status VARCHAR
);

CREATE TABLE IF NOT EXISTS compilations (
    id INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN,
    title VARCHAR
);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilation_id INTEGER REFERENCES compilations(id),
    event_id INTEGER REFERENCES events(id)
);