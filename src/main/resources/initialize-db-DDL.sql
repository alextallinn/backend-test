DROP TABLE IF EXISTS filter_column;
CREATE TABLE IF NOT EXISTS filter_column
(
    id          IDENTITY     NOT NULL PRIMARY KEY,
    column_name varchar(255) NOT NULL UNIQUE,
    column_type varchar(255) NOT NULL
);

DROP TABLE IF EXISTS filter_type;
CREATE TABLE IF NOT EXISTS filter_type
(
    id          IDENTITY     NOT NULL PRIMARY KEY,
    type_name   VARCHAR(255) NOT NULL UNIQUE,
    column_type VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS filter;
CREATE TABLE IF NOT EXISTS filter
(
    id   IDENTITY     NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS filter_element;
CREATE TABLE IF NOT EXISTS filter_element
(
    id               IDENTITY     NOT NULL PRIMARY KEY,
    filter_id        INT          NOT NULL,
    filter_column_id INT          NOT NULL,
    filter_type_id   INT          NOT NULL,
    value_text       VARCHAR(255) NOT NULL
);

