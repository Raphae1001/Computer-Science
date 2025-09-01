package smarticulous;

import smarticulous.db.Exercise;
import smarticulous.db.Submission;
import smarticulous.db.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Smarticulous class, implementing a grading system.
 */
public class Smarticulous {

    /**
     * The connection to the underlying DB.
     * <p>
     * null if the db has not yet been opened.
     */
    Connection db;

    /**
     * Open the {@link Smarticulous} SQLite database.
     * <p>
     * This should open the database, creating a new one if necessary, and set the {@link #db} field
     * to the new connection.
     * <p>
     * The open method should make sure the database contains the following tables, creating them if necessary:
     *
     * <table>
     *   <caption><em>Table name: <strong>User</strong></em></caption>
     *   <tr><th>Column</th><th>Type</th></tr>
     *   <tr><td>UserId</td><td>Integer (Primary Key)</td></tr>
     *   <tr><td>Username</td><td>Text</td></tr>
     *   <tr><td>Firstname</td><td>Text</td></tr>
     *   <tr><td>Lastname</td><td>Text</td></tr>
     *   <tr><td>Password</td><td>Text</td></tr>
     * </table>
     *
     * <p>
     * <table>
     *   <caption><em>Table name: <strong>Exercise</strong></em></caption>
     *   <tr><th>Column</th><th>Type</th></tr>
     *   <tr><td>ExerciseId</td><td>Integer (Primary Key)</td></tr>
     *   <tr><td>Name</td><td>Text</td></tr>
     *   <tr><td>DueDate</td><td>Integer</td></tr>
     * </table>
     *
     * <p>
     * <table>
     *   <caption><em>Table name: <strong>Question</strong></em></caption>
     *   <tr><th>Column</th><th>Type</th></tr>
     *   <tr><td>ExerciseId</td><td>Integer</td></tr>
     *   <tr><td>QuestionId</td><td>Integer</td></tr>
     *   <tr><td>Name</td><td>Text</td></tr>
     *   <tr><td>Desc</td><td>Text</td></tr>
     *   <tr><td>Points</td><td>Integer</td></tr>
     * </table>
     * In this table the combination of ExerciseId and QuestionId together comprise the primary key.
     *
     * <p>
     * <table>
     *   <caption><em>Table name: <strong>Submission</strong></em></caption>
     *   <tr><th>Column</th><th>Type</th></tr>
     *   <tr><td>SubmissionId</td><td>Integer (Primary Key)</td></tr>
     *   <tr><td>UserId</td><td>Integer</td></tr>
     *   <tr><td>ExerciseId</td><td>Integer</td></tr>
     *   <tr><td>SubmissionTime</td><td>Integer</td></tr>
     * </table>
     *
     * <p>
     * <table>
     *   <caption><em>Table name: <strong>QuestionGrade</strong></em></caption>
     *   <tr><th>Column</th><th>Type</th></tr>
     *   <tr><td>SubmissionId</td><td>Integer</td></tr>
     *   <tr><td>QuestionId</td><td>Integer</td></tr>
     *   <tr><td>Grade</td><td>Real</td></tr>
     * </table>
     * In this table the combination of SubmissionId and QuestionId together comprise the primary key.
     *
     * @param dburl The JDBC url of the database to open (will be of the form "jdbc:sqlite:...")
     * @return the new connection
     * @throws SQLException
     */

public Connection openDB(String dburl) throws SQLException {
    // Establish the connection to the SQLite database
    Connection connection = DriverManager.getConnection(dburl);
    
    // Create the tables if they don't exist
    try (Statement stmt = connection.createStatement()) {
        // Create User table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS User (" +
            "UserId INTEGER PRIMARY KEY, " +
            "Username TEXT UNIQUE, " +
            "Firstname TEXT, " +
            "Lastname TEXT, " +
            "Password TEXT)");

        // Create Exercise table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS Exercise (" +
            "ExerciseId INTEGER PRIMARY KEY, " +
            "Name TEXT, " +
            "DueDate INTEGER)");

        // Create Question table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS Question (" +
            "ExerciseId INTEGER, " +
            "QuestionId INTEGER, " +
            "Name TEXT, " +
            "Desc TEXT, " +
            "Points INTEGER, " +
            "PRIMARY KEY (ExerciseId, QuestionId))");

        // Create Submission table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS Submission (" +
            "SubmissionId INTEGER PRIMARY KEY, " +
            "UserId INTEGER, " +
            "ExerciseId INTEGER, " +
            "SubmissionTime INTEGER)");

        // Create QuestionGrade table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS QuestionGrade (" +
            "SubmissionId INTEGER, " +
            "QuestionId INTEGER, " +
            "Grade REAL, " +
            "PRIMARY KEY (SubmissionId, QuestionId))");
    }

    // Set the db field to the new connection and return it
    this.db = connection;
    return connection;
}


    /**
     * Close the DB if it is open.
     *
     * @throws SQLException
     */
    public void closeDB() throws SQLException {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    // =========== User Management =============

    /**
     * Add a user to the database / modify an existing user.
     * <p>
     * Add the user to the database if they don't exist. If a user with user.username does exist,
     * update their password and firstname/lastname in the database.
     *
     * @param user
     * @param password
     * @return the userid.
     * @throws SQLException
     */
    public int addOrUpdateUser(User user, String password) throws SQLException {
    // Check if the user already exists
    String selectQuery = "SELECT UserId FROM User WHERE Username = ?";
    try (PreparedStatement selectStmt = db.prepareStatement(selectQuery)) {
        selectStmt.setString(1, user.getUsername());
        ResultSet rs = selectStmt.executeQuery();
        
        if (rs.next()) {
            // User exists, update their details
            int userId = rs.getInt("UserId");
            String updateQuery = "UPDATE User SET Password = ?, Firstname = ?, Lastname = ? WHERE UserId = ?";
            try (PreparedStatement updateStmt = db.prepareStatement(updateQuery)) {
                updateStmt.setString(1, password);
                updateStmt.setString(2, user.getFirstname());
                updateStmt.setString(3, user.getLastname());
                updateStmt.setInt(4, userId);
                updateStmt.executeUpdate();
            }
            user.id = userId; // Affecter l'ID à l'objet User
            return userId;
        } else {
            // User does not exist, insert new user
            String insertQuery = "INSERT INTO User (Username, Password, Firstname, Lastname) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStmt = db.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, user.getUsername());
                insertStmt.setString(2, password);
                insertStmt.setString(3, user.getFirstname());
                insertStmt.setString(4, user.getLastname());
                insertStmt.executeUpdate();
                
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    user.id = userId; // Affecter l'ID généré à l'objet User
                    return userId;
                }
            }
        }
    }
    return -1; // Return -1 if something went wrong
}


    /**
     * Verify a user's login credentials.
     *
     * @param username
     * @param password
     * @return true if the user exists in the database and the password matches; false otherwise.
     * @throws SQLException
     * <p>
     * Note: this is totally insecure. For real-life password checking, it's important to store only
     * a password hash
     * @see <a href="https://crackstation.net/hashing-security.htm">How to Hash Passwords Properly</a>
     */
    public boolean verifyLogin(String username, String password) throws SQLException {
    String query = "SELECT Password FROM User WHERE Username = ?";
    try (PreparedStatement stmt = db.prepareStatement(query)) {
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            // Check if the provided password matches the stored password
            String storedPassword = rs.getString("Password");
            return storedPassword.equals(password);
        }
    }
    return false; // Return false if the user doesn't exist or password doesn't match
}

    // =========== Exercise Management =============

    /**
     * Add an exercise to the database.
     *
     * @param exercise
     * @return the new exercise id, or -1 if an exercise with this id already existed in the database.
     * @throws SQLException
     */
    public int addExercise(Exercise exercise) throws SQLException {
    // Check if the exercise already exists
    String selectQuery = "SELECT ExerciseId FROM Exercise WHERE ExerciseId = ?";
    try (PreparedStatement selectStmt = db.prepareStatement(selectQuery)) {
        selectStmt.setInt(1, exercise.getId());
        ResultSet rs = selectStmt.executeQuery();
        
        if (rs.next()) {
            // Exercise already exists, return -1
            return -1;
        }
    }
    
    // Insert new exercise
    String insertQuery = "INSERT INTO Exercise (ExerciseId, Name, DueDate) VALUES (?, ?, ?)";
    try (PreparedStatement insertStmt = db.prepareStatement(insertQuery)) {
        insertStmt.setInt(1, exercise.getId());
        insertStmt.setString(2, exercise.getName());
        insertStmt.setLong(3, exercise.getDueDate().getTime()); 
        insertStmt.executeUpdate();
    }

    // Insert questions for the exercise
    String insertQuestionQuery = "INSERT INTO Question (ExerciseId, QuestionId, Name, Desc, Points) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement insertQuestionStmt = db.prepareStatement(insertQuestionQuery)) {
        int questionId = 1;
        for (Exercise.Question question : exercise.questions) {
            insertQuestionStmt.setInt(1, exercise.getId());
            insertQuestionStmt.setInt(2, questionId++);
            insertQuestionStmt.setString(3, question.name);
            insertQuestionStmt.setString(4, question.desc);
            insertQuestionStmt.setInt(5, question.points);
            insertQuestionStmt.executeUpdate();
        }
    }

    return exercise.getId(); // Return the new exercise ID
}

public List<Exercise> loadExercises() throws SQLException {
    List<Exercise> exercises = new ArrayList<>();
    
    String query = "SELECT ExerciseId, Name, DueDate FROM Exercise ORDER BY ExerciseId";
    try (PreparedStatement stmt = db.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            int exerciseId = rs.getInt("ExerciseId");
            String name = rs.getString("Name");
            long dueDate = rs.getLong("DueDate");
            Exercise exercise = new Exercise(exerciseId, name, new Date(dueDate));
            exercises.add(exercise);
        }
    }
    
    // Load questions for each exercise
    String queryQuestions = "SELECT QuestionId, Name, Desc, Points FROM Question WHERE ExerciseId = ?";
    try (PreparedStatement stmtQuestions = db.prepareStatement(queryQuestions)) {
        for (Exercise exercise : exercises) {
            stmtQuestions.setInt(1, exercise.getId());
            ResultSet rsQuestions = stmtQuestions.executeQuery();
            while (rsQuestions.next()) {
                int questionId = rsQuestions.getInt("QuestionId");
                String name = rsQuestions.getString("Name");
                String desc = rsQuestions.getString("Desc");
                int points = rsQuestions.getInt("Points");
                exercise.addQuestion(name, desc, points);
            }
        }
    }
    
    return exercises;
}

    // ========== Submission Storage ===============

    /**
     * Store a submission in the database.
     * The id field of the submission will be ignored if it is -1.
     * <p>
     * Return -1 if the corresponding user doesn't exist in the database.
     *
     * @param submission
     * @return the submission id.
     * @throws SQLException
     */
   public int storeSubmission(Submission submission) throws SQLException {
    // Assurer que l'utilisateur est dans la base de données et récupérer son ID
    int userId = addOrUpdateUser(submission.user, "default_password"); // Remplace "default_password" par le mot de passe approprié
    if (userId == -1) {
        throw new SQLException("Failed to add or update user");
    }
    submission.user.id = userId; // Met à jour l'ID de l'utilisateur dans l'objet Submission
    
    int submissionId = submission.id; // Utiliser l'ID existant s'il y en a un
    
    // Si c'est une nouvelle soumission ou si l'ID est -1
    if (submissionId == -1) {
        String insertQuery = "INSERT INTO Submission (UserId, ExerciseId, SubmissionTime) VALUES (?, ?, ?)";
        try (PreparedStatement insertStmt = db.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setInt(1, submission.user.id);
            insertStmt.setInt(2, submission.exercise.id);
            insertStmt.setLong(3, submission.submissionTime.getTime());
            insertStmt.executeUpdate();
            
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Failed to retrieve generated ID");
            }
            submissionId = generatedKeys.getInt(1);
        }
    } else {
        // Mettre à jour une soumission existante
        String updateQuery = "UPDATE Submission SET UserId = ?, ExerciseId = ?, SubmissionTime = ? WHERE SubmissionId = ?";
        try (PreparedStatement updateStmt = db.prepareStatement(updateQuery)) {
            updateStmt.setInt(1, submission.user.id);
            updateStmt.setInt(2, submission.exercise.id);
            updateStmt.setLong(3, submission.submissionTime.getTime());
            updateStmt.setInt(4, submissionId);
            updateStmt.executeUpdate();
        }
    }

    // Supprimer les anciennes notes si elles existent
    String deleteGradesQuery = "DELETE FROM QuestionGrade WHERE SubmissionId = ?";
    try (PreparedStatement deleteStmt = db.prepareStatement(deleteGradesQuery)) {
        deleteStmt.setInt(1, submissionId);
        deleteStmt.executeUpdate();
    }

    // Insérer les notes
    String insertGradeQuery = "INSERT INTO QuestionGrade (SubmissionId, QuestionId, Grade) VALUES (?, ?, ?)";
    try (PreparedStatement insertGradeStmt = db.prepareStatement(insertGradeQuery)) {
        for (int i = 0; i < submission.questionGrades.length; i++) {
            insertGradeStmt.setInt(1, submissionId);
            insertGradeStmt.setInt(2, i + 1);
            insertGradeStmt.setFloat(3, submission.questionGrades[i]);
            insertGradeStmt.executeUpdate();
        }
    }

    return submissionId;
}

    // ============= Submission Query ===============


    /**
     * Return a prepared SQL statement that, when executed, will
     * return one row for every question of the latest submission for the given exercise by the given user.
     * <p>
     * The rows should be sorted by QuestionId, and each row should contain:
     * - A column named "SubmissionId" with the submission id.
     * - A column named "QuestionId" with the question id,
     * - A column named "Grade" with the grade for that question.
     * - A column named "SubmissionTime" with the time of submission.
     * <p>
     * Parameter 1 of the prepared statement will be set to the User's username, Parameter 2 to the Exercise Id, and
     * Parameter 3 to the number of questions in the given exercise.
     * <p>
     * This will be used by {@link #getLastSubmission(User, Exercise)}
     *
     * @return
     */
    PreparedStatement getLastSubmissionGradesStatement() throws SQLException {
    // SQL query to get the latest submission for a given exercise by a given user
    String sql = "SELECT " +
             "    s.SubmissionId, " +
             "    q.QuestionId, " +
             "    g.Grade, " +
             "    s.SubmissionTime " +
             "FROM " +
             "    Submission s " +
             "INNER JOIN " +
             "    QuestionGrade g ON s.SubmissionId = g.SubmissionId " +
             "INNER JOIN " +
             "    Question q ON g.QuestionId = q.QuestionId AND q.ExerciseId = s.ExerciseId " +
             "WHERE " +
             "    s.UserId = (SELECT UserId FROM User WHERE Username = ?) " +
             "    AND s.ExerciseId = ? " +
             "ORDER BY " +
             "    s.SubmissionTime DESC " +
             "LIMIT ?;";

    // Prepare the statement and return it
    return db.prepareStatement(sql);
}

    /**
     * Return a prepared SQL statement that, when executed, will
     * return one row for every question of the <i>best</i> submission for the given exercise by the given user.
     * The best submission is the one whose point total is maximal.
     * <p>
     * The rows should be sorted by QuestionId, and each row should contain:
     * - A column named "SubmissionId" with the submission id.
     * - A column named "QuestionId" with the question id,
     * - A column named "Grade" with the grade for that question.
     * - A column named "SubmissionTime" with the time of submission.
     * <p>
     * Parameter 1 of the prepared statement will be set to the User's username, Parameter 2 to the Exercise Id, and
     * Parameter 3 to the number of questions in the given exercise.
     * <p>
     * This will be used by {@link #getBestSubmission(User, Exercise)}
     *
     */
    PreparedStatement getBestSubmissionGradesStatement() throws SQLException {
    // SQL query to get the best submission for a given exercise by a given user
    String sql = "SELECT " +
             "    s.SubmissionId, " +
             "    q.QuestionId, " +
             "    g.Grade, " +
             "    s.SubmissionTime " +
             "FROM " +
             "    Submission s " +
             "INNER JOIN " +
             "    QuestionGrade g ON s.SubmissionId = g.SubmissionId " +
             "INNER JOIN " +
             "    Question q ON g.QuestionId = q.QuestionId AND q.ExerciseId = s.ExerciseId " +
             "WHERE " +
             "    s.UserId = (SELECT UserId FROM User WHERE Username = ?) " +
             "    AND s.ExerciseId = ? " +
             "    AND s.SubmissionId = ( " +
             "        SELECT g1.SubmissionId " +
             "        FROM QuestionGrade g1 " +
             "        INNER JOIN Submission s1 ON g1.SubmissionId = s1.SubmissionId " +
             "        WHERE s1.UserId = (SELECT UserId FROM User WHERE Username = ?) " +
             "        AND s1.ExerciseId = ? " +
             "        GROUP BY g1.SubmissionId " +
             "        ORDER BY SUM(g1.Grade) DESC " +
             "        LIMIT 1 " +
             "    ) " +
             "ORDER BY " +
             "    q.QuestionId " +
             "LIMIT ?;";
    // Prepare the statement and return it
    return db.prepareStatement(sql);
}

    /**
     * Return a submission for the given exercise by the given user that satisfies
     * some condition (as defined by an SQL prepared statement).
     * <p>
     * The prepared statement should accept the user name as parameter 1, the exercise id as parameter 2 and a limit on the
     * number of rows returned as parameter 3, and return a row for each question corresponding to the submission, sorted by questionId.
     * <p>
     * Return null if the user has not submitted the exercise (or is not in the database).
     *
     * @param user
     * @param exercise
     * @param stmt
     * @return
     * @throws SQLException
     */
    Submission getSubmission(User user, Exercise exercise, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, user.username);
        stmt.setInt(2, exercise.id);
        stmt.setInt(3, exercise.questions.size());

        ResultSet res = stmt.executeQuery();

        boolean hasNext = res.next();
        if (!hasNext)
            return null;

        int sid = res.getInt("SubmissionId");
        Date submissionTime = new Date(res.getLong("SubmissionTime"));

        float[] grades = new float[exercise.questions.size()];

        for (int i = 0; hasNext; ++i, hasNext = res.next()) {
            grades[i] = res.getFloat("Grade");
        }

        return new Submission(sid, user, exercise, submissionTime, (float[]) grades);
    }

    /**
     * Return the latest submission for the given exercise by the given user.
     * <p>
     * Return null if the user has not submitted the exercise (or is not in the database).
     *
     * @param user
     * @param exercise
     * @return
     * @throws SQLException
     */
    public Submission getLastSubmission(User user, Exercise exercise) throws SQLException {
        return getSubmission(user, exercise, getLastSubmissionGradesStatement());
    }


    /**
     * Return the submission with the highest total grade
     *
     * @param user the user for which we retrieve the best submission
     * @param exercise the exercise for which we retrieve the best submission
     * @return
     * @throws SQLException
     */
    public Submission getBestSubmission(User user, Exercise exercise) throws SQLException {
        return getSubmission(user, exercise, getBestSubmissionGradesStatement());
    }
}
