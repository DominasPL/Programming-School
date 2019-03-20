package pl.coderslab.workshops2.ProgrammingSchool.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Exercise {

    private int id;
    private String title;
    private String description;

    public Exercise() {

    }

    public Exercise(String title, String description) {
        this.title = title;
        this.description = description;
    }

    //////////////////////////////////////////////////////////////////////// Methods

    public void saveToDb(Connection conn) throws SQLException { // Zapis do bazdy danych

        if (this.id == 0) {

            String query = "INSERT INTO exercise (title, description) \n" +
                    "VALUES (?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,this.title);
            preparedStatement.setString(2,this.description);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                this.id = generatedKeys.getInt(1);
            }

        } else {  // Modyfikacja

            String query = "UPDATE exercise SET title = ?, description = ? WHERE id = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, this.title);
            preparedStatement.setString(2, this.description);
            preparedStatement.setInt(3, this.id);
            preparedStatement.executeUpdate();


        }

    }

    public void deleteExercise(Connection conn) throws SQLException { // Usunięcie zadania z bazdy danych

        if (this.id != 0) {

            String query = "DELETE FROM exercise WHERE id = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }

    }

    public static Exercise loadExerciseById(Connection conn, int id) throws  SQLException { //Pobranie zadania po id

        String query = "SELECT * FROM exercise WHERE id = ?;";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Exercise exercise = new Exercise();
            exercise.id = resultSet.getInt("id");
            exercise.title = resultSet.getString("title");
            exercise.description = resultSet.getString("description");

            return exercise;
        }

        return null;

    }

    public static ArrayList<Integer> loadExercisesIDs (Connection conn) throws SQLException {  // Pobranie wszystkich id zadań. Używane w klasie Solution w funkcji checkExerciseID

        ArrayList<Integer> exercisesIDs = new ArrayList<>();
        String query = "SELECT id FROM exercise";
        PreparedStatement preparedStatement = conn.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int exerciseID = resultSet.getInt("id");
            exercisesIDs.add(exerciseID);
        }

        return exercisesIDs;
    }


    public static ArrayList<Exercise> loadAllExercises(Connection conn) throws SQLException { //Pobranie wszystkich zadan z bazy danych

        ArrayList<Exercise> exercises = new ArrayList<>();
        String query = "SELECT * FROM exercise;";
        PreparedStatement preparedStatement = conn.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Exercise exercise = new Exercise();
            exercise.id = resultSet.getInt("id");
            exercise.title = resultSet.getString("title");
            exercise.description = resultSet.getString("description");

            exercises.add(exercise);
        }

        return exercises;

    }



    //////////////////////////////////////////////////////// Getters, Setters and toString

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
