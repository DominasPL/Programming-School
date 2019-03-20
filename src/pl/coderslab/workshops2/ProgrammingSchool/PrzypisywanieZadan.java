package pl.coderslab.workshops2.ProgrammingSchool;

import pl.coderslab.workshops2.ProgrammingSchool.models.Exercise;
import pl.coderslab.workshops2.ProgrammingSchool.models.Solution;
import pl.coderslab.workshops2.ProgrammingSchool.models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static pl.coderslab.workshops2.ProgrammingSchool.ZarzadzanieUzytkownikami.printUsers;
import static pl.coderslab.workshops2.ProgrammingSchool.ZarzadzanieZadaniami.printExercises;

public class PrzypisywanieZadan {

    public static void main(String[] args) {

        try(Connection conn = ConnectionManager.getConnection()) {

            while (true) {
                System.out.println("############### MENU ###############");
                System.out.println("########## DOSTĘPNE OPCJE ##########");
                System.out.println("1. 'add' – przypisywanie zadań do użytkowników.");
                System.out.println("2. 'view' – przeglądanie rozwiązań danego użytkownika.");
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
                        showUserSolutions(conn);
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

    public static int getUserId(Connection conn) throws SQLException { // Pobieranie id uzytkownika z konsoli

        while (true) {

            Scanner scanner = new Scanner(System.in);

            System.out.print("Podaj id użytkownika: ");

            while(!scanner.hasNextInt()) {
                scanner.nextLine();
            }

            int user_id = scanner.nextInt();

            User user = User.loadUserById(conn, user_id);
            if (user == null) {
                System.out.println("Użytkownik o podanym id nie istnieje!!!");
            } else {
                return user_id;
            }



        }


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

        printUsers(conn);
        int userID = getUserId(conn);
        printExercises(conn);
        int exerciseID = getExerciseId(conn);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        Solution solution = new Solution();
        solution.setUser_id(userID);
        solution.setExercise_id(exerciseID);
        solution.setCreated(dateFormat.format(date));
        solution.saveSolutionToDb(conn);



    }

    public static void showUserSolutions(Connection conn) throws SQLException { //Wyswietlenie wszystkich rozwiazan danego uzytkownika

        int userID = getUserId(conn);

        ArrayList<Solution> userSolutions = Solution.loadAllSolutionsByUserId(conn, userID);

        for (Solution solution : userSolutions) {
            System.out.println(solution);
        }


    }




}
