package pl.coderslab.workshops2.ProgrammingSchool;

import pl.coderslab.workshops2.ProgrammingSchool.ConnectionManager;
import pl.coderslab.workshops2.ProgrammingSchool.models.Exercise;
import pl.coderslab.workshops2.ProgrammingSchool.models.UserGroup;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ZarzadzanieGrupami {

    public static void main(String[] args) {


        try(Connection conn = ConnectionManager.getConnection()) {

            while (true) {
                System.out.println("############### LISTA GRUP ###############");
                printGroups(conn);

                System.out.println();
                System.out.println("############### MENU ###############");
                System.out.println("########## DOSTĘPNE OPCJE ##########");
                System.out.println("1. 'add' – dodanie grupy.");
                System.out.println("2. 'edit' – edycja grupy,");
                System.out.println("3. 'delete' – usunięcie grupy,");
                System.out.println("4. 'quit' – zakończenie programu.");
                System.out.print("\nTwój wybór: ");

                Scanner scanner = new Scanner(System.in);
                String answer = scanner.nextLine();

                switch (answer) {
                    case "add":
                        addGroup(conn);
                        break;
                    case "edit":
                        editGroup(conn);
                        break;
                    case "delete":
//                        deleteUser(conn);
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

    public static void printGroups(Connection conn) throws SQLException { // Wyswietlanie wszytkich grup

        ArrayList<UserGroup> allGroups = UserGroup.loadAllGroups(conn);

        int i = 1;

        for (UserGroup userGroup : allGroups) {
            System.out.println(i + ". " + "ID grupy: " + userGroup.getId() + " \n    Nazwa grupy: " + userGroup.getName());
            i++;
        }


    }

    public static void addGroup(Connection conn) throws SQLException { //Dodawanie grupy

        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj nazwe: ");
        String title = scanner.nextLine();

        UserGroup userGroup = new UserGroup(title);
        userGroup.saveGroupToDb(conn);


    }

    public static void editGroup(Connection conn) throws SQLException { // Edycja grupy

        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj id grupy do edycji: ");

        while(!scanner.hasNextInt()) {
            scanner.nextLine();
        }

        int group_id = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        UserGroup userGroup = UserGroup.loadGroupByID(conn, group_id);
        if (userGroup == null) {
            System.out.println("Grupa o podanym id nie istnieje!!!");
        } else {

            System.out.print("Podaj nową nazwę: ");
            String name = scanner.nextLine();

            userGroup.setName(name);
            userGroup.saveGroupToDb(conn);

        }

    }

    public static void deleteGroup(Connection conn) throws SQLException { // todo zrobic usuwanie z solution

        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj id gupy do usunięcia: ");

        while(!scanner.hasNextInt()) {
            scanner.nextLine();
        }

        int group_id = scanner.nextInt();

        UserGroup userGroup = UserGroup.loadGroupByID(conn, group_id);
        if (userGroup == null) {
            System.out.println("Grupa o podanym id nie istnieje!!!");
        } else {
            userGroup.deleteGroup(conn);
        }


    }







}
