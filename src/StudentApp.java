import java.sql.*;
import java.util.Scanner;

/**
 * StudentApp.java
 * CRUD application for the PostgreSQL "students" table (COMP 3005, Assignment 3 Q1).
 *
 * Functions:
 *   getAllStudents()                                   -> READ
 *   addStudent(first, last, email, enrollmentDate)     -> CREATE
 *   updateStudentEmail(studentId, newEmail)            -> UPDATE
 *   deleteStudent(studentId)                           -> DELETE
 */
public class StudentApp {

    // ------------------------------------------------------------------
    // Connection settings -- change these to match your PostgreSQL setup.
    // ------------------------------------------------------------------
    private static final String URL = "jdbc:postgresql://localhost:5432/student_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "PrishaJecintho!17";

    /** Opens and returns a new connection to the database. */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Retrieves every row from the students table and prints it. */
    public static void getAllStudents() {
        String sql = "SELECT student_id, first_name, last_name, email, enrollment_date "
                   + "FROM students ORDER BY student_id";
        // try-with-resources closes the connection, statement and result set automatically
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%n%-4s%-10s%-10s%-28s%s%n", "ID", "First", "Last", "Email", "Enrolled");
            System.out.println("-".repeat(64));
            int count = 0;
            while (rs.next()) {
                System.out.printf("%-4d%-10s%-10s%-28s%s%n",
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getDate("enrollment_date"));
                count++;
            }
            System.out.println("(" + count + " rows)\n");
        } catch (SQLException e) {
            System.out.println("Error retrieving students: " + e.getMessage());
        }
    }

    /** Inserts a new student. enrollmentDate format: "YYYY-MM-DD" (may be empty/null). */
    public static void addStudent(String firstName, String lastName, String email, String enrollmentDate) {
        String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // PreparedStatement parameters (?) protect against SQL injection
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            if (enrollmentDate == null || enrollmentDate.isBlank()) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(enrollmentDate));
            }
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    System.out.println("Added " + firstName + " " + lastName
                            + " with student_id " + keys.getInt("student_id") + ".");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: date must be in YYYY-MM-DD format.");
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {   // unique_violation
                System.out.println("Error: email '" + email + "' already exists.");
            } else {
                System.out.println("Error adding student: " + e.getMessage());
            }
        }
    }

    /** Changes the email of the student with the given student_id. */
    public static void updateStudentEmail(int studentId, String newEmail) {
        String sql = "UPDATE students SET email = ? WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newEmail);
            ps.setInt(2, studentId);
            int rows = ps.executeUpdate();     // number of rows changed

            if (rows == 0) {
                System.out.println("No student found with student_id " + studentId + ".");
            } else {
                System.out.println("Updated email for student_id " + studentId + " to " + newEmail + ".");
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Error: email '" + newEmail + "' is already used by another student.");
            } else {
                System.out.println("Error updating email: " + e.getMessage());
            }
        }
    }

    /** Deletes the student with the given student_id. */
    public static void deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                System.out.println("No student found with student_id " + studentId + ".");
            } else {
                System.out.println("Deleted student with student_id " + studentId + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    /** Reads an integer from the user; returns null if the input is not a number. */
    private static Integer readInt(Scanner in, String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(in.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("student_id must be a number.");
            return null;
        }
    }

    /** Simple text menu so each function can be demonstrated live. */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("\n=== Student Database ===\n"
                    + "1. Show all students\n"
                    + "2. Add a student\n"
                    + "3. Update a student's email\n"
                    + "4. Delete a student\n"
                    + "5. Quit\n"
                    + "Choose: ");
            String choice = in.nextLine().trim();

            switch (choice) {
                case "1" -> getAllStudents();
                case "2" -> {
                    System.out.print("First name: ");
                    String first = in.nextLine().trim();
                    System.out.print("Last name: ");
                    String last = in.nextLine().trim();
                    System.out.print("Email: ");
                    String email = in.nextLine().trim();
                    System.out.print("Enrollment date (YYYY-MM-DD): ");
                    String date = in.nextLine().trim();
                    addStudent(first, last, email, date);
                }
                case "3" -> {
                    Integer id = readInt(in, "student_id: ");
                    if (id != null) {
                        System.out.print("New email: ");
                        updateStudentEmail(id, in.nextLine().trim());
                    }
                }
                case "4" -> {
                    Integer id = readInt(in, "student_id: ");
                    if (id != null) deleteStudent(id);
                }
                case "5" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
