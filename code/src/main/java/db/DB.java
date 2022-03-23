package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    // JDBC URL, username and password of MySQL server
    private static final String value = System.getenv("MYSQL_IP_ADDRESS");
    private static final String url = "jdbc:mysql://" + value + ":3306/ja?allowPublicKeyRetrieval=true&useSSL=false";
//rc1b-x1vq6vet43l6exsp:3306/ja?useSSL=false";
    private static final String user = "jauser";
    private static final String password = "12345678";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public void addUser(String login, String pass, String email) {
        String query1 = "CREATE TABLE IF NOT EXISTS users (login VARCHAR(10), pass VARCHAR(10), email VARCHAR(15));";
        String query2 = "INSERT INTO users VALUES('" + login + "','" + pass + "','" + email + "');";

        try {
            // opening database connection to MySQL server
//            System.out.println("ok");
            con = DriverManager.getConnection(url, user, password);
//            System.out.println("con");
            // getting Statement object to execute query
            stmt = con.createStatement();
//            System.out.println("stmt");
            stmt.executeUpdate(query1);
//            System.out.println("q1");
            // executing INSERT query
            stmt.executeUpdate(query2);
//            System.out.println("q2");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection and stmt here
            try { con.close(); } catch(SQLException se) {System.out.println("Can't close connection");}
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }

    }

    public boolean userFound(String login, String pass, String email) {
        String query1 = "CREATE TABLE IF NOT EXISTS users (login VARCHAR(10), pass VARCHAR(10), email VARCHAR(15));";
        String query2 = "SELECT COUNT(*) FROM users WHERE login='" + login + "' AND pass='" + pass + "' AND email='" + email + "';";
        int count = 0;

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            stmt.executeUpdate(query1);
            // executing SELECT query
            rs = stmt.executeQuery(query2);

            while (rs.next()) {
                count = rs.getInt(1);
                //System.out.println("Number of users : " + count);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) {System.out.println("Can't close connection");}
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }

        return count > 0;
    }

}
