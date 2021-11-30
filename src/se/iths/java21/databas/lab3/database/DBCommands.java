package se.iths.java21.databas.lab3.database;

import se.iths.java21.databas.lab3.tools.InputHandler;

import java.sql.*;

public class DBCommands {
    static Connection connection;
    static Statement statement;

    static String username;
    static String password;

    public static void setupConnection() {
        boolean tryAgain = false;

        do {
            System.out.println("""
                    Welcome to Laboration 3 - Artist Database
                                    
                    Attempting to connect to server...
                    """);

            System.out.println("Enter username: ");
            String username = InputHandler.getStringInput();

            System.out.println("Enter password: ");
            String password = InputHandler.getStringInput();

            System.out.println("\nChecking connection to Server...");

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", username, password);
                statement = connection.createStatement();
                DBCommands.username = username;
                DBCommands.password = password;
            } catch (SQLException e) {
                System.out.println("Connection failed... try again...");
                tryAgain = true;
            }
        } while (tryAgain);
    }

    public static void setupDatabase() {

        System.out.println("Attempting to setup database...");

        try {
            statement.executeUpdate("CREATE DATABASE Lab3");
        } catch (SQLException e) {
            System.out.println("Database already exists... connecting...");
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Lab3", username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\nDatabase connected...");
    }

    public static void setupArtistTable() {
        System.out.println("Setting up Artist table...");

        try {
            statement.executeUpdate("CREATE TABLE Artist (" +
                    "id SMALLINT NOT NULL AUTO_INCREMENT," +
                    "first_name VARCHAR(255) NOT NULL," +
                    "last_name VARCHAR(255) NOT NULL," +
                    "age SMALLINT NOT NULL," +
                    "PRIMARY KEY(id))");
        } catch (SQLException e) {
            System.out.println("Table Artist already exists...");
        }

        System.out.println("\nSetup Done...");
    }

    public static void addArtist() {
        System.out.println("""
                ADD ARTIST
                """);

        System.out.println("Enter first name:");
        String firstName = InputHandler.getStringInput();

        System.out.println("Enter last name:");
        String lastName = InputHandler.getStringInput();

        System.out.println("Enter artist age:");
        int age = InputHandler.getIntegerInput();

        try {
            PreparedStatement addArtist = connection.prepareStatement("INSERT INTO Artist (first_name, last_name, age) VALUES (?, ?, ?)");
            addArtist.setString(1, firstName);
            addArtist.setString(2, lastName);
            addArtist.setInt(3, age);
            addArtist.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void deleteArtist() {
        System.out.println("""
                DELETE ARTIST
                """);

        System.out.println("Artists in List:\n");

        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery("SELECT * FROM Artist");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("ID") + ", Name: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name") + ", Age: " + resultSet.getString("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Please enter ID of Artist to DELETE:");
        int id = InputHandler.getIntegerInput();

        PreparedStatement deleteArtist;
        try {
            deleteArtist = connection.prepareStatement("DELETE FROM Artist WHERE id = ?");
            deleteArtist.setInt(1, id);
            deleteArtist.executeUpdate();
            System.out.println("Artist with ID " + id + " DELETED");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Artist with ID " + id + " not found...");
        }

    }

    public static void updateArtist() {

    }

    public static void showAll() {

    }

    public static void findByID() {

    }

    public static void findByAge() {

    }

    public static void findByName() {

    }
}
