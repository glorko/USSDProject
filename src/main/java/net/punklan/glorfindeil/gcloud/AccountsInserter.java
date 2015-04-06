package net.punklan.glorfindeil.gcloud;


import org.apache.jorphan.logging.LoggingManager;

import org.apache.log.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


import static org.junit.Assert.fail;

/**
 * Created by glorfindeil on 03.04.15.
 */
public class AccountsInserter {
    Connection connect = null;

    public AccountsInserter() {

    }

    public AccountsInserter(String string) {

    }

    List<Connection> one = new ArrayList<Connection>();
    List<Connection> two = new ArrayList<Connection>();

    @Before
    public void setUp() {
        try {
            List<String> instances2 = Arrays.asList("173.194.253.19:3306/ussddb", "173.194.253.19:3306/ussddb");

            two = connectToInstances(instances2);
            List<String> instances = Arrays.asList("173.194.253.19:3306/ussddb");
            one = connectToInstances(instances);
        } catch (SQLException e) {
            log.error("connect", e);
            fail();
            throw new RuntimeException("Problems with one of instances");
        } catch (ClassNotFoundException e) {
            log.error("connect", e);
            fail();
        }
    }

    @After
    public void tearDown() {
        try {
            for (Connection con : one) {

                con.close();

            }
            for (Connection con : two) {
                con.close();
            }
        } catch (SQLException e) {
            log.error("connect", e);
        }
        // tear down logic here
    }

    private static final Logger log = LoggingManager.getLoggerForClass();
    static Long numMin = 89640000001L;
    static Long numMax = 89640100001L;

    private static Long getNumber() {

        Random r = new Random();
        long number = numMin + ((long) (r.nextDouble() * (numMax - numMin)));
        return number;
    }

    public void testSelect(Long number, Connection connect) {
        log.info("number is " + number + " and connection is " + connect);
        PreparedStatement statement = null;
        try {
            String insertString =
                    "select Account.sum from Account where number = ?";

            // Result set get the result of the SQL query


            // PreparedStatements can use variables and are more efficient
            PreparedStatement preparedStatement = connect
                    .prepareStatement(insertString);
            preparedStatement.setString(1, number + "");
            ResultSet rs = preparedStatement.executeQuery();
            log.info(rs.getFetchSize() + "");
            Long sum = 0L;
            while (rs.next()) {
                sum = rs.getLong(1);
                log.info("sum is: " + sum);

            }
            assert sum.equals(1000L);
        } catch (SQLException e) {
            log.error("Exception on select " + e);
            fail();

        }
    }

    @Test
    public void getFromTwoInstancesTest() {



            //get random phone number
            Long number = getNumber();
            //understand the region and get needed connection to database
            Connection connect = getConnectionForNumber(one, number);
            //get sum on account
            testSelect(number, connect);

    }

    @Test
    public void getFromOneInstanceTest() {

            //get random phone number
            Long number = getNumber();
            //understand the region and get needed connection to database
            Connection connect = getConnectionForNumber(two, number);
            //get sum on account
            testSelect(number, connect);


    }

    private List<Connection> connectToInstances(List<String> instances) throws ClassNotFoundException, SQLException {

        List<Connection> conns = new ArrayList<Connection>();
        Class.forName("com.mysql.jdbc.Driver");

        for (String url : instances) {

            connect = DriverManager
                    .getConnection("jdbc:mysql://" + url

                            , "glorfindeil", "564978");
            conns.add(connect);


        }
        return conns;


    }

    private Connection getConnectionForNumber(List<Connection> conns, Long number) {
        long diapason = (numMax - numMin) / conns.size();
        long up = (number - numMin);
        int index = (int) (up / diapason);
        log.info(diapason + "   " + up + "   " + index);
        return conns.get(index);
    }


}
