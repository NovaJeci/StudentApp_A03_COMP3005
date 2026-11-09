-- schema.sql
-- Creates the students table and inserts the initial data.
-- Run this inside the database you created (e.g. "student_db").

DROP TABLE IF EXISTS students;

CREATE TABLE students (
    student_id      SERIAL PRIMARY KEY,          -- auto-incrementing integer primary key
    first_name      TEXT NOT NULL,
    last_name       TEXT NOT NULL,
    email           TEXT NOT NULL UNIQUE,        -- no two students may share an email
    enrollment_date DATE                         -- nullable, as per the schema
);

INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES
('John', 'Doe',   'john.doe@example.com',   '2023-09-01'),
('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01'),
('Jim',  'Beam',  'jim.beam@example.com',   '2023-09-02');
