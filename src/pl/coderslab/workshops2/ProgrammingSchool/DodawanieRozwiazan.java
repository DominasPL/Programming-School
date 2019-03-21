package pl.coderslab.workshops2.ProgrammingSchool;

import pl.coderslab.workshops2.ProgrammingSchool.models.Exercise;
import pl.coderslab.workshops2.ProgrammingSchool.models.Solution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class DodawanieRozwiazan {

    public static void main(String[] args) {

        try(Connection conn = ConnectionManager.getConnection()) {

            while (true) {
                System.out.println("############### MENU ###############");
                System.out.println("########## DOSTĘPNE OPCJE ##########");
                System.out.println("1. 'add' – dodawanie rozwiązania.");
                System.out.println("2. 'view' – przeglądanie swoich rozwiązań.");
                System.out.println("3. 'quit' – zakończenie programu.");
                System.out.print("\nTwój wybór: ");


                Scanner scanner = new Scanner(System.in);
                String answer = scanner.nextLine();

                switch (answer) {
                    case "add": {
                        addSolution(conn);
                        break;
                    }
                    case "view":

                        break;
                    case "quit":
                        System.out.println("quit");
                        System.exit(0);
                    default:
                        System.out.println("Niepoprawny wybór. Spróbuj jeszcze raz.");
                        break;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Integer> showExercises(Connection conn, int user_id) throws SQLException { // Wyswietlenie wszystkich zadan do których użytkownik nie dodał rozwiązania

        ArrayList<Integer> userExercises = Exercise.loadExercisesIdsWithUserSolution(conn, user_id);
        ArrayList<Exercise> exercisesAll = Exercise.loadAllExercises(conn);
        ArrayList<Integer> exercisesWithoutSolution = new ArrayList<>(); //Utworzenie listy, która posiada id zadan bez rozwiązań


        for (Exercise exercise : exercisesAll) {
            exercisesWithoutSolution.add(exercise.getId());
        }

        for (Integer i : userExercises) { //Utworzenie listy, która posiada id zadan bez rozwiązań
            if (exercisesWithoutSolution.contains(i)) {
                exercisesWithoutSolution.remove(i);
            }
        }

        System.out.println("ZADANIA DLA KTÓRYCH UŻYTKOWNIK NIE PODAŁ ROZWIĄZANIA: ");
        for (Integer i : exercisesWithoutSolution) {
            System.out.println("ID zadania: " + i);
        }


        return exercisesWithoutSolution;

    }

    public static int getExerciseId(Connection conn) throws SQLException { //Pobieranie id zadania z konsoli

        while (true) {

            Scanner scanner = new Scanner(System.in);

            System.out.print("Podaj id zadania: ");

            while(!scanner.hasNextInt()) {
                scanner.nextLine();
            }

            int exercise_id = scanner.nextInt();

            Exercise exercise = Exercise.loadExerciseById(conn, exercise_id);
            if (exercise == null) {
                System.out.println("Zadanie o podanym id nie istnieje!!!");
            } else {
                return exercise_id;
            }

        }


    }

    public static void addSolution(Connection conn) throws SQLException { //Zapis nowego rozwiązania do bazy danych

        ArrayList<Integer> exercisesWithoutSolution = showExercises(conn, 9);
        int exerciseID = getExerciseId(conn);

        if (exercisesWithoutSolution.contains(exerciseID)) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            Solution solution = new Solution();
            solution.setUser_id(9);
            solution.setExercise_id(exerciseID);
            solution.setCreated(dateFormat.format(date));
            solution.saveSolutionToDb(conn);

        } else {
            System.out.println("Podano już rozwiązanie do tego zadania !");
        }

    }










}
