package pl.coderslab.workshops2.ProgrammingSchool;

import pl.coderslab.workshops2.ProgrammingSchool.models.Exercise;
import pl.coderslab.workshops2.ProgrammingSchool.models.Solution;
import pl.coderslab.workshops2.ProgrammingSchool.models.User;
import pl.coderslab.workshops2.ProgrammingSchool.models.UserGroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ZarzadzanieUzytkownikami {

    public static void main(String[] args) {

        try(Connection conn = ConnectionManager.getConnection()) {

            while (true) {
                printUsers(conn);

                System.out.println();
                System.out.println("############### MENU ###############");
                System.out.println("########## DOSTĘPNE OPCJE ##########");
                System.out.println("1. 'add' – dodanie użytkownika,");
                System.out.println("2. 'edit' – edycja użytkownika,");
                System.out.println("3. 'delete' – usunięcie użytkownika,");
                System.out.println("4. 'quit' – zakończenie programu.");
                System.out.print("\nTwój wybór: ");

                Scanner scanner = new Scanner(System.in);
                String answer = scanner.nextLine();

                switch (answer) {
                    case "add":
                        addUser(conn);
                        break;
                    case "edit":
                        editUser(conn);
                        break;
                    case "delete":
                        deleteUser(conn);
                        break;
                    case "quit":
                        System.out.println("quit");
                        System.exit(0);
                    default:
                        System.out.println("Niepoprawny wybór. Spróbuj jeszcze raz.");
                        break;
                }

            }




        } catch (SQLException  e) {
            e.printStackTrace();
        }

    }

    public static void printUsers(Connection conn) throws SQLException { //Wyswietlanie uzytkownikow

        System.out.println("############### LISTA UŻYTKOWNIKÓW ###############");
        ArrayList<User> allUsers = User.loadAllUsers(conn);

        int i = 1;

        for (User user : allUsers) {
            System.out.println(i + ". " + "ID użytkownika: " + user.getId() + " \n    Nazwa użytkownika: " + user.getUsername());
            i++;
        }

        System.out.println();
    }

    public static void addUser(Connection conn) throws SQLException { // Dodawanie uzytkownika

        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj id grupy: ");

        while(!scanner.hasNextInt()) {
            scanner.nextLine();
        }

        int group_id = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        System.out.print("Podaj nazwę użytkownika: ");
        String username = scanner.nextLine();

        System.out.print("Podaj hasło: ");
        String password = scanner.nextLine();

        System.out.print("Podaj email: ");
        String email = scanner.nextLine();

        User user = new User(group_id, username, password, email);
        user.saveUserToDb(conn);

    }

    public static void editUser(Connection conn) throws SQLException { //Edycja uzytkownika

        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj id użytkownika do edycji: ");

        while(!scanner.hasNextInt()) {
            scanner.nextLine();
        }

        int user_id = scanner.nextInt();

        User user = User.loadUserById(conn, user_id);
        if (user == null) {
            System.out.println("Użytkownik o podanym id nie istnieje!!!");
        } else {
            System.out.print("Podaj nowy id grupy: ");

            while(!scanner.hasNextInt()) {
                scanner.nextLine();
            }

            int group_id = scanner.nextInt();

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            System.out.print("Podaj nową nazwę użytkownika: ");
            String username = scanner.nextLine();

            System.out.print("Podaj nowe hasło: ");
            String password = scanner.nextLine();

            System.out.print("Podaj nowy email: ");
            String email = scanner.nextLine();

            user.setGroup_id(group_id);
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.saveUserToDb(conn);
        }

    }

    public static void deleteUser(Connection conn) throws SQLException { //todo zrobić usuwanie najpierw z solution

        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj id użytkownika do usunięcia: ");

        while(!scanner.hasNextInt()) {
            scanner.nextLine();
        }

        int user_id = scanner.nextInt();

        User user = User.loadUserById(conn, user_id);
        if (user == null) {
            System.out.println("Użytkownik o podanym id nie istnieje!!!");
        } else {
            user.deleteUser(conn);
        }

    }





}
