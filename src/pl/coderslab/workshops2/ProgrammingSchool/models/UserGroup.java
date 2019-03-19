package pl.coderslab.workshops2.ProgrammingSchool.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserGroup {
    private int id;
    private String name;


    public UserGroup() {

    }

    public UserGroup(String name) {
        this.name = name;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////// Methods

    public void saveGroupToDb(Connection conn) throws SQLException { //Zapis grupy do bazy danych

        if (this.id == 0) { // Zapis
            String query = "INSERT INTO user_group (name) \n" +
                    "VALUES (?); ";
            PreparedStatement preparedStatement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, this.name);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                this.id = generatedKeys.getInt(1);
            }

        } else { //Modyfikacja

            String query = "UPDATE user_group SET name = ? WHERE id = ? ";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, this.name);
            preparedStatement.setInt(2, this.id);
            preparedStatement.executeUpdate();
        }


    }


    public void deleteGroup (Connection conn) throws SQLException { // USuwanie grupy

        if (this.id != 0) {
            String query = "DELETE FROM user_group WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }
    }


    public static UserGroup loadGroupByID (Connection conn, int id) throws SQLException { //Wczytanie grupy po ID

        String query = "SELECT * FROM user_group WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            UserGroup group = new UserGroup();
            group.id = resultSet.getInt("id");
            group.name = resultSet.getString("name");
            return group;
        }

        return null;
    }

    public static ArrayList<Integer> loadGroupIDs (Connection conn) throws SQLException {  // Pobranie wszystkich id grup. Używane w klasie User w funkcji checkGroupID

        ArrayList<Integer> groupsIDs = new ArrayList<>();
        String query = "SELECT id FROM user_group";
        PreparedStatement preparedStatement = conn.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int groupID = resultSet.getInt("id");
            groupsIDs.add(groupID);
        }

        return groupsIDs;
    }

    public static ArrayList<UserGroup> loadAllGroups (Connection conn) throws SQLException { //Wczytanie wszystkich grup

        ArrayList<UserGroup> groups = new ArrayList<>();
        String query = "SELECT * FROM user_group";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            UserGroup group = new UserGroup();
            group.id = resultSet.getInt("id");
            group.name = resultSet.getString("name");
            groups.add(group);
        }

        return groups;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Getters, Setters and toString


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
