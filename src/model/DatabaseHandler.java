package model;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DatabaseHandler {
    
    private MessageDispatcher messageDispatcher;

    private static String DATABASE_URL = "jdbc:derby:" + Paths.get("PERSONALASSISTANT").toAbsolutePath() + ";bootPassword=oaimnUAFNIAfunwefciu34598$@*csndikAF";

    static Map taskQuery(String query) throws Exception {
        Map hm = new HashMap();
        Connection connection = DriverManager.getConnection(DATABASE_URL);
        Statement statement = connection.createStatement();
        System.out.println("The Query is : " + query);
        ResultSet resultSet = statement.executeQuery(query);
        
        while (resultSet.next()) {
            hm.put(resultSet.getInt("ID"), resultSet.getString("TASK"));
        }

        return hm;
    }

    static Password passwordQuery(String query) throws Exception {
        Password password = new Password();
        Connection connection = DriverManager.getConnection(DATABASE_URL);
        Statement statement = connection.createStatement();
        System.out.println("The Query is : " + query);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            password.setName(resultSet.getString("NAME"));
            password.setUsername(resultSet.getString("USERNAME"));
            password.setPassword(resultSet.getString("PASS"));
        }
        return password;
    }

    static LinkedList<Password> queryTable(String query) throws Exception {

        LinkedList<Password> ll = new LinkedList<Password>();
        Connection connection = DriverManager.getConnection(DATABASE_URL);
        Statement statement = connection.createStatement();
        System.out.println("The Query is : " + query);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {   // Move the cursor to the next row, return false if no more row
            Password pw = new Password();
            pw.setName(resultSet.getString("NAME"));
            pw.setUsername(resultSet.getString("USERNAME"));
            pw.setPassword(resultSet.getString("PASS"));
            ll.add(pw);
        }

        return ll;
    }

    static void addEditDeleteQuery(String query) throws Exception {
        Connection connection = DriverManager.getConnection(DATABASE_URL);
        Statement statement = connection.createStatement();
        System.out.println("The Query is : " + query);
        int resultSet = statement.executeUpdate(query);
        System.out.println("Affected rows : " + resultSet);
    }

    public static void createDatabaseAndTable() throws Exception {

        if (!Files.exists(Paths.get("PERSONALASSISTANT").toAbsolutePath())) {
            //databse does not exist, so create it
            String databasepath = "jdbc:derby:" + Paths.get("PERSONALASSISTANT").toAbsolutePath() + ";create=true;dataEncryption=true;encryptionAlgorithm=Blowfish/CBC/NoPadding;bootPassword=oaimnUAFNIAfunwefciu34598$@*csndikAF";
            Connection connection = DriverManager.getConnection(databasepath);
            Statement statement = connection.createStatement();
            System.out.println("database created");
            // create PASSWORD table
            String query = "create table PASSWORD ("
                    + " ID integer not null primary key generated always as identity (start with 1, increment by 1),"
                    + " NAME varchar(40) unique,"
                    + " USERNAME varchar(40),"
                    + " PASS varchar(40)"
                    + ")";

            statement.executeUpdate(query);
            System.out.println("PASSWORD table created");
            // also insert the master password
            query = "insert into PASSWORD(NAME, USERNAME, PASS) values('MASTER','master','master')";
            addEditDeleteQuery(query);

            // create TASKS table
            query = "create table TASKS ("
                    + " ID integer not null primary key generated always as identity (start with 1, increment by 1),"
                    + " TASK varchar(30) unique"
                    + ")";
            statement.executeUpdate(query);
            System.out.println("TASKS table created");

            // add initial list items
            query = "insert into TASKS(TASK) values ('Study'),"
                    + "('Listen'),"
                    + "('Research'),"
                    + "('Write'),"
                    + "('Sleep')";
            addEditDeleteQuery(query);

        }

    }

    public void setMessageDispatcher(MessageDispatcher md) {
        this.messageDispatcher = md;
    }
}
