package pl.coderslab.workshops2.ProgrammingSchool.models;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.workshops2.ProgrammingSchool.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;


public class User {
    private int id;
    private int group_id;
    private String username;
    private String password;
    private String email;

    public User() {

    }

    public User(int group_id, String username, String password, String email) throws  SQLException {
        checkGroupID(group_id); //Sprawdzenie czy id grupy znajduje sie w bazie danych
        this.username = username;
        setPassword(password); // Hashowanie hasła
        this.email = email;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Methods


    public void saveUserToDb(Connection conn) throws SQLException { //Zapis nowego uzykownika do bazy danych

        if (this.id == 0) { // Zapis
            String query = "INSERT INTO users (group_id, username, password, email) VALUES (?, ?, ?, ?) ";
            PreparedStatement preparedStatement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, this.group_id);
            preparedStatement.setString(2, this.username);
            preparedStatement.setString(3, this.password);
            preparedStatement.setString(4, this.email);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                this.id = generatedKeys.getInt(1);
            }

        } else { // Modyfikacja

            String query = "UPDATE users SET group_id = ?, username = ?, password = ?, email = ? WHERE id = ? ";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, this.group_id);
            preparedStatement.setString(2, this.username);
            preparedStatement.setString(3, this.password);
            preparedStatement.setString(4, this.email);
            preparedStatement.setInt(5, this.id);
            preparedStatement.executeUpdate();

        }


    }

    public void checkGroupID (int group_id) throws SQLException { // Sprawdzenie czy podany id grupy znajduje się w bazie danych. Jeżeli nie to id grupy ustawiane jest na 1.

        try (Connection conn = ConnectionManager.getConnection()) {
            ArrayList<Integer> groupsIDs = UserGroup.loadGroupIDs(conn);

            if (!groupsIDs.contains(group_id)) {
                this.group_id = 1;
                System.out.println("Grupa o podanym id nie istnieje. Użytkownik został automatycznie przypisany do grupy o id = 1 !!!");
            } else {
                this.group_id = group_id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteUser(Connection conn) throws SQLException { // Usuwanie uzytkownika z bazy danych
        if (this.id != 0) {
            String sql = "DELETE FROM users WHERE id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }
    }

    public static User loadUserById(Connection conn, int id) throws SQLException { // Wczytanie użytkownika o konkretnym ID

        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            User usr1 = new User();
            usr1.id = resultSet.getInt("id");
            usr1.group_id = resultSet.getInt("group_id");
            usr1.username = resultSet.getString("username");
            usr1.password = resultSet.getString("password");
            usr1.email = resultSet.getString("email");
            return usr1;

        }

        return null;

    }

    public static ArrayList<Integer> loadUsersIDs (Connection conn) throws SQLException {  // Pobranie wszystkich id uzytkownikow. Używane w klasie Solution w funkcji checkUserID

        ArrayList<Integer> usersIDs = new ArrayList<>();
        String query = "SELECT id FROM users";
        PreparedStatement preparedStatement = conn.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int userID = resultSet.getInt("id");
            usersIDs.add(userID);
        }

        return usersIDs;
    }


    public static ArrayList<User> loadAllUsers(Connection conn) throws SQLException { //Wczytanie wszystkich uzytkownikow
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            User usr1 = new User();
            usr1.id = resultSet.getInt("id");
            usr1.group_id = resultSet.getInt("group_id");
            usr1.username = resultSet.getString("username");
            usr1.password = resultSet.getString("password");
            usr1.email = resultSet.getString("email");

            users.add(usr1);
        }

        return users;

    }

    public static ArrayList<User> loadUsersByUserName(Connection conn, String username) throws SQLException { //Pobieranie uzytkownikow z bazy danych o wskazanym username
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE username = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            User usr1 = new User();
            usr1.id = resultSet.getInt("id");
            usr1.group_id = resultSet.getInt("group_id");
            usr1.username = resultSet.getString("username");
            usr1.password = resultSet.getString("password");
            usr1.email = resultSet.getString("email");

            users.add(usr1);
        }

        return users;

    }

    public static ArrayList<User> loadAllUsersFromGroup(Connection conn, int id) throws SQLException { //Pobieranie uzytkownikow z bazy danych o wskazanym username
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM users\n" +
                "WHERE group_id = ?;";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            User usr1 = new User();
            usr1.id = resultSet.getInt("id");
            usr1.group_id = resultSet.getInt("group_id");
            usr1.username = resultSet.getString("username");
            usr1.password = resultSet.getString("password");
            usr1.email = resultSet.getString("email");

            users.add(usr1);
        }

        return users;

    }







    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Getters, Setters and toString

    public int getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) throws SQLException {
        checkGroupID(group_id); //Sprawdzenie czy id grupy znajduje sie w bazie danych
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", group_id=" + group_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
