package pl.coderslab.workshops2.ProgrammingSchool;


import pl.coderslab.workshops2.ProgrammingSchool.models.Exercise;
import pl.coderslab.workshops2.ProgrammingSchool.models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ZarzadzanieZadaniami {

    public static void main(String[] args) {


        try (Connection conn = ConnectionManager.getConnection()) {

            while (true) {
                System.out.println("############### LISTA ZADAŃ ###############");
                printExercises(conn);
                System.out.println();
                System.out.println("############### MENU ###############");
                System.out.println("########## DOSTĘPNE OPCJE ##########");
                System.out.println("1. 'add' – dodanie zadania");
                System.out.println("2. 'edit' – edycja zadania,");
                System.out.println("3. 'delete' – usunięcie zadania,");
                System.out.println("4. 'quit' – zakończenie programu.");
                System.out.print("\nTwój wybór: ");

                Scanner scanner = new Scanner(System.in);
                String answer = scanner.nextLine();

                switch (answer) {
                    case "add":
                        addExercise(conn);
                        break;
                    case "edit":
                        editExercise(conn);
                        break;
                    case "delete":
                        deleteExercise(conn);
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

    public static void printExercises(Connection conn) throws SQLException { //Wyswietlanie wszystkich zadan

        ArrayList<Exercise> allExercises = Exercise.loadAllExercises(conn);

        int i = 1;

        for (Exercise exercise : allExercises) {
            System.out.println(i + ". " + "ID zadania: " + exercise.getId() + " \n    Nazwa zadania: " + exercise.getTitle());
            i++;
        }

        System.out.println();

    }

    public static void addExercise(Connection conn) throws SQLException { //Dodawanie zadania

        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj tytuł: ");
        String title = scanner.nextLine();

        System.out.print("Podaj opis: ");
        String description = scanner.nextLine();

        Exercise exercise = new Exercise(title, description);
        exercise.saveToDb(conn);


    }

    public static void editExercise(Connection conn) throws SQLException { // Edycja zadania

        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj id zadania do edycji: ");

        while(!scanner.hasNextInt()) {
            scanner.nextLine();
        }

        int exercise_id = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        Exercise exercise = Exercise.loadExerciseById(conn, exercise_id);
        if (exercise == null) {
            System.out.println("Zadanie o podanym id nie istnieje!!!");
        } else {

            System.out.print("Podaj nowy tytuł: ");
            String title = scanner.nextLine();

            System.out.print("Podaj nowy opis: ");
            String description = scanner.nextLine();

            exercise.setTitle(title);
            exercise.setDescription(description);
            exercise.saveToDb(conn);

        }

    }

    public static void deleteExercise(Connection conn) throws SQLException { // todo zrobic usuwanie z solution

        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj id zadania do usunięcia: ");

        while(!scanner.hasNextInt()) {
            scanner.nextLine();
        }

        int exercise_id = scanner.nextInt();

        Exercise exercise = Exercise.loadExerciseById(conn, exercise_id);
        if (exercise == null) {
            System.out.println("Zadanie o podanym id nie istnieje!!!");
        } else {
            exercise.deleteExercise(conn);
        }

    }
































}
