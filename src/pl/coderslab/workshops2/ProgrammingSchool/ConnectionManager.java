package pl.coderslab.workshops2.ProgrammingSchool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    public static Connection getConnection() throws SQLException {

        Connection conn = DriverManager.getConnection(

                "jdbc:mysql://localhost:3306/workshop2?useSSL=false&characterEncoding=utf8",

                "root", "coderslab");

        return conn;

    }
}
