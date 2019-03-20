package pl.coderslab.workshops2.ProgrammingSchool.models;


import com.mysql.cj.xdevapi.SqlDataResult;
import pl.coderslab.workshops2.ProgrammingSchool.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Solution {
    private int id;
    private int exercise_id;
    private int user_id;
    private String created;
    private String updated;
    private String description;

    public Solution() {

    }

    public Solution(int exercise_id, int user_id, String created, String updated, String description) throws SQLException {
        checkExerciseID(exercise_id);
        checkUserID(user_id);
        this.created = created;
        this.updated = updated;
        this.description = description;
    }

    ////////////////////////////////////////////////////////////// Methods

    public void saveSolutionToDb(Connection conn) throws SQLException { // Zapis rozwiązania do bazy danych

        if (this.id == 0) {

            String query = "INSERT INTO solution (exercise_id, user_id, created, updated, description) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, this.exercise_id);
            preparedStatement.setInt(2, this.user_id);
            preparedStatement.setString(3, this.created);
            preparedStatement.setString(4, this.updated);
            preparedStatement.setString(5, this.description);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                this.id = generatedKeys.getInt(1);
            }

        } else { //Modyfikacja rowiązania

            String query = "UPDATE solution SET exercise_id = ?, user_id = ?, created = ?, updated = ?, description = ? WHERE id = ? ";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, this.exercise_id);
            preparedStatement.setInt(2, this.user_id);
            preparedStatement.setString(3, this.created);
            preparedStatement.setString(4, this.updated);
            preparedStatement.setString(5, this.description);
            preparedStatement.setInt(6, this.id);
            preparedStatement.executeUpdate();

        }


    }

    public void deleteSolution(Connection conn) throws SQLException { // Usuwanie rozwiązania z bazy danych
        if (this.id != 0) {
            String sql = "DELETE FROM solution WHERE id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }
    }

    public void checkExerciseID (int exercise_id) throws SQLException { // Sprawdzenie czy podany id zadania znajduje się w bazie danych.

        try (Connection conn = ConnectionManager.getConnection()) {
            ArrayList<Integer> exercisesIDs = Exercise.loadExercisesIDs(conn);

            if (!exercisesIDs.contains(exercise_id)) { // Jeżeli podana wartość  jest nieprawidłowa to pobieram wartość od uzytkownika

                while (true) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Zadanie o podanym id nie istnieje. Podaj jeszcze raz: ");
                    while (!scanner.hasNextInt()) {
                        scanner.nextLine();
                        System.out.print("Zadanie o podanym id nie istnieje. Podaj jeszcze raz: ");
                    }

                    int newExercise_Id = scanner.nextInt();

                    if (exercisesIDs.contains(newExercise_Id)) { //Jeżeli podana liczba znajduje sie w liście zadań to przerywam pętle
                        this.exercise_id = newExercise_Id;
                        break;
                    }

                }


            } else {
                this.exercise_id = exercise_id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkUserID (int user_id) throws SQLException { // Sprawdzenie czy podany id usera znajduje się w bazie danych.

        try (Connection conn = ConnectionManager.getConnection()) {
            ArrayList<Integer> usersIDs = User.loadUsersIDs(conn);
            int i = 1;

            if (!usersIDs.contains(user_id)) { // Jeżeli podana wartość  jest nieprawidłowa to pobieram wartość od uzytkownika

                while (true) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("User o podanym id nie istnieje. Podaj jeszcze raz: ");
                    while (!scanner.hasNextInt()) {
                        scanner.nextLine();
                        System.out.print("User o podanym id nie istnieje. Podaj jeszcze raz: ");
                    }

                    int newUser_Id = scanner.nextInt();

                    if (usersIDs.contains(newUser_Id)) { //Jeżeli podana liczba znajduje sie w liście zadań to przerywam pętle
                        this.user_id = newUser_Id;
                        break;
                    }

                }


            } else {
                this.user_id = user_id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static Solution loadSolutionById(Connection conn, int id) throws SQLException { // Wczytanie rozwiazania o konkretnym ID

        String query = "SELECT * FROM solution WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Solution solution = new Solution();
            solution.id = resultSet.getInt("id");
            solution.exercise_id = resultSet.getInt("exercise_id");
            solution.user_id = resultSet.getInt("user_id");
            solution.created = resultSet.getString("created");
            solution.updated = resultSet.getString("updated");
            solution.description = resultSet.getString("description");

            return solution;
        }

        return null;

    }


    public static ArrayList<Solution> loadAllSolutions(Connection conn) throws SQLException { //Wczytanie wszystkich uzytkownikow
        ArrayList<Solution> solutions = new ArrayList<>();
        String query = "SELECT * FROM solution";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Solution solution = new Solution();
            solution.id = resultSet.getInt("id");
            solution.exercise_id = resultSet.getInt("exercise_id");
            solution.user_id = resultSet.getInt("user_id");
            solution.created = resultSet.getString("created");
            solution.updated = resultSet.getString("updated");
            solution.description = resultSet.getString("description");

            solutions.add(solution);
        }

        return solutions;

    }

    public static ArrayList<Solution> loadAllSolutionsByUserId(Connection conn, int id) throws SQLException { // Wczytanie wszystkich rozwiazan danego uzytkownika
        ArrayList<Solution> solutions = new ArrayList<>();
        String query = "SELECT * FROM solution WHERE user_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Solution solution = new Solution();
            solution.id = resultSet.getInt("id");
            solution.exercise_id = resultSet.getInt("exercise_id");
            solution.user_id = resultSet.getInt("user_id");
            solution.created = resultSet.getString("created");
            solution.updated = resultSet.getString("updated");
            solution.description = resultSet.getString("description");

            solutions.add(solution);

        }

        return solutions;

    }

    public static ArrayList<Solution> loadAllSortedExerciseSolutions(Connection conn, int id) throws SQLException { // Wczytanie wszystkich rozwiazan danego zadania (posortowane)
        ArrayList<Solution> solutions = new ArrayList<>();
        String query = "SELECT * FROM solution WHERE exercise_id = ? ORDER BY created DESC;";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Solution solution = new Solution();
            solution.id = resultSet.getInt("id");
            solution.exercise_id = resultSet.getInt("exercise_id");
            solution.user_id = resultSet.getInt("user_id");
            solution.created = resultSet.getString("created");
            solution.updated = resultSet.getString("updated");
            solution.description = resultSet.getString("description");

            solutions.add(solution);

        }

        return solutions;

    }



    //////////////////////////////////////////////// Getters, Setters and toString

    public int getId() {
        return id;
    }


    public int getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(int exercise_id) throws SQLException {
        checkExerciseID(exercise_id);
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) throws SQLException{
        checkUserID(user_id);
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "id=" + id +
                ", exercise_id=" + exercise_id +
                ", user_id=" + user_id +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}
