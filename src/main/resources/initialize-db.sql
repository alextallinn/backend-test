INSERT INTO filter_column (id, column_name, column_type)
VALUES (1, 'Amount', 'Integer'),
       (2, 'Title', 'String'),
       (3, 'Date', 'Date');

ALTER TABLE filter_column
    ALTER COLUMN id RESTART WITH (SELECT MAX(id) FROM filter_column) +1;

INSERT INTO filter_type (id, type_name, column_type)
VALUES (1, 'More', 'Integer'),
       (2, 'Start with', 'String'),
       (3, 'From', 'Date');

ALTER TABLE filter_type
    ALTER COLUMN id RESTART WITH (SELECT MAX(id) FROM filter_type) +1;

