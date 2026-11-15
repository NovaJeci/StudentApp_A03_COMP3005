# Student Database CRUD App (PostgreSQL + Java/JDBC)

COMP 3005 – Assignment 3, Question 1.

A command-line Java app that connects to a PostgreSQL `students` table and performs CRUD operations.

**Demo video:** https://youtu.be/VyVU2-ws7VI

## Project structure
```
database/schema.sql   # creates the students table + initial data
src/StudentApp.java   # the application (4 CRUD functions + menu)
lib/                  # put the PostgreSQL JDBC driver jar here
```

## Prerequisites
- PostgreSQL and pgAdmin
- JDK 17 or newer
- PostgreSQL JDBC driver jar from https://jdbc.postgresql.org/download/ (place it in `lib/`)

## Setup
1. In pgAdmin, create a database named `student_db`
   (or run `CREATE DATABASE student_db;`).
2. Open the Query Tool on `student_db`, load `database/schema.sql`, and run it.
   This creates the table and inserts the 3 initial students.
3. Open `src/StudentApp.java` and edit `USER` and `PASSWORD` (and `URL` if your host/port/database differ).

## Compile and run
Replace `postgresql-42.7.4.jar` with the name of the jar you downloaded.

**macOS / Linux**
```
javac -d out src/StudentApp.java
java -cp "out:lib/postgresql-42.7.4.jar" StudentApp
```

**Windows**
```
javac -d out src/StudentApp.java
java -cp "out;lib/postgresql-42.7.4.jar" StudentApp
```

(In an IDE such as IntelliJ, Eclipse or VS Code: add the jar to the project's libraries, then run `StudentApp`.)

## Using the app
| Option | Function | Description |
|---|---|---|
| 1 | `getAllStudents()` | Prints all students |
| 2 | `addStudent(first, last, email, date)` | Inserts a new student |
| 3 | `updateStudentEmail(id, newEmail)` | Updates a student's email |
| 4 | `deleteStudent(id)` | Deletes a student |
| 5 | – | Quit |

After options 2–4, refresh the `students` table in pgAdmin (right-click table → View/Edit Data → All Rows) to see the change.