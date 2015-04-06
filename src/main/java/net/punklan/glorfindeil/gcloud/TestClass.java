package net.punklan.glorfindeil.gcloud;

import java.sql.*;

/**
 * Created by glorfindeil on 02.04.15.
 */
public class TestClass {
    public static void main(String[] args) throws SQLException {
        Connection connect = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://173.194.253.19:3306/ussddb"
                           ,"glorfindeil","564978");

            // Statements allow to issue SQL queries to the database
            PreparedStatement statement = null;
            String insertString =
                    "insert into Account (number, name, sum) values ( ?, ?, 1000)";

            // Result set get the result of the SQL query


            // PreparedStatements can use variables and are more efficient
            PreparedStatement preparedStatement = connect
                    .prepareStatement(insertString);
            connect.setAutoCommit(false);
            // "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
            // Parameters start with 1
            Long base = 89640000000L;
            for (int i = 1; i < 100000; i++) {
                preparedStatement.setString(1, (base + i) + "");
                preparedStatement.setString(2, "name" + i);

                preparedStatement.executeUpdate();

                System.out.println("inserted "+i);
            }
            connect.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
