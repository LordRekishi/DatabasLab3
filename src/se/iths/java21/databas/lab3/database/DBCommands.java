package se.iths.java21.databas.lab3.database;

import se.iths.java21.databas.lab3.tools.InputHandler;

import java.sql.*;

public class DBCommands {
    private static Connection connection;
    private static Statement statement;
    private static String username;
    private static String password;

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

        System.out.println("Connection successful!");
    }

    public static void setupDatabase() {
        System.out.println("\nAttempting to setup database...");

        try {
            statement.executeUpdate("CREATE DATABASE PatrikLab3");
            connection.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Database already exists... connecting...");
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PatrikLab3", username, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Connection failed...");
        }

        System.out.println("Database connected!");
    }

    public static void setupArtistTable() {
        System.out.println("\nSetting up Artist table...");

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

        System.out.println("Setup Done!");
    }

    public static void addArtist() {
        System.out.println("""
                                
                ADD ARTIST:
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
                                
                DELETE ARTIST:""");

        showAll();

        boolean tryAgain;
        int id;

        do {
            System.out.println("\nPlease enter ID of Artist to DELETE:");
            id = InputHandler.getIntegerInput();

            try {
                findByID(id);
                tryAgain = false;
            } catch (SQLException e) {
                System.out.println("Artist with ID " + id + " not found... try again");
                tryAgain = true;
            }
        } while (tryAgain);

        PreparedStatement deleteArtist;
        try {
            deleteArtist = connection.prepareStatement("DELETE FROM Artist WHERE id = ?");
            deleteArtist.setInt(1, id);
            deleteArtist.executeUpdate();
            System.out.println("Artist with ID " + id + " DELETED");
        } catch (SQLException e) {
            System.out.println("Artist with ID " + id + " not found...");
        }
    }

    public static void updateArtist() {
        System.out.println("""
                                
                UPDATE ARTIST:
                """);

        showAll();

        boolean tryAgain;
        int id;

        do {
            System.out.println("\nPlease enter ID of Artist to UPDATE:");
            id = InputHandler.getIntegerInput();

            try {
                findByID(id);
                tryAgain = false;
            } catch (SQLException e) {
                System.out.println("Artist with ID " + id + " not found... try again");
                tryAgain = true;
            }
        } while (tryAgain);

        System.out.println("\nEnter new first name:");
        String firstName = InputHandler.getStringInput();

        System.out.println("Enter new last name:");
        String lastName = InputHandler.getStringInput();

        System.out.println("Enter new artist age:");
        int age = InputHandler.getIntegerInput();

        PreparedStatement updateArtist;
        try {
            updateArtist = connection.prepareStatement("UPDATE Artist Set first_name = ?, last_name = ?, age = ? WHERE id = ?");
            updateArtist.setString(1, firstName);
            updateArtist.setString(2, lastName);
            updateArtist.setInt(3, age);
            updateArtist.setInt(4, id);
            updateArtist.executeUpdate();
            System.out.println("Artist with ID " + id + " UPDATED");
        } catch (SQLException e) {
            System.out.println("Artist with ID " + id + " not found...");
        }
    }

    public static void showAll() {
        System.out.println("""
                                
                SHOW ALL artists in list:
                """);

        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery("SELECT * FROM Artist");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name") + ", Age: " + resultSet.getString("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void findByID() {
        System.out.println("""
                                
                FIND Artist BY ID:
                """);

        System.out.println("Please enter ID of Artist to FIND:");
        int id = InputHandler.getIntegerInput();

        PreparedStatement findByID;
        try {
            findByID = connection.prepareStatement("SELECT * FROM Artist WHERE id = ?");
            ;
            findByID.setInt(1, id);
            ResultSet resultSet = findByID.executeQuery();
            resultSet.next();
            System.out.println("ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name") + ", Age: " + resultSet.getString("age"));
        } catch (SQLException e) {
            System.out.println("Artist with ID " + id + " not found...");
        }
    }

    private static void findByID(int id) throws SQLException {
        PreparedStatement findByID = connection.prepareStatement("SELECT * FROM Artist WHERE id = ?");
        findByID.setInt(1, id);
        ResultSet resultSet = findByID.executeQuery();

        if (!resultSet.next()) {
            throw new SQLException();
        }
    }

    public static void findByAge() {
        System.out.println("""
                                
                FIND Artist BY AGE:
                """);

        System.out.println("Please enter MINIMUM AGE of Artist to FIND:");
        int minAge = InputHandler.getIntegerInput();

        System.out.println("\nPlease enter MAXIMUM AGE of Artist to FIND:");
        int maxAge = InputHandler.getIntegerInput();

        PreparedStatement findByID;
        try {
            findByID = connection.prepareStatement("SELECT * FROM Artist WHERE age BETWEEN ? AND ?");
            ;
            findByID.setInt(1, minAge);
            findByID.setInt(2, maxAge);

            ResultSet resultSet = findByID.executeQuery();

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name") + ", Age: " + resultSet.getString("age"));
            }
        } catch (SQLException e) {
            System.out.println("Artists with AGE between" + minAge + " and " + maxAge + " not found...");
        }
    }

    public static void findByName() {
        System.out.println("""
                                
                FIND Artist BY NAME:
                """);

        System.out.println("Please enter part of, or the whole name of the artist you want to find:");
        String name = InputHandler.getStringInput();

        PreparedStatement findByName;
        try {
            findByName = connection.prepareStatement("SELECT * FROM Artist WHERE CONCAT(first_name, ' ', last_name) LIKE ?");
            findByName.setString(1, "%" + name + "%");

            ResultSet resultSet = findByName.executeQuery();

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name") + ", Age: " + resultSet.getString("age"));
            }
        } catch (SQLException e) {
            System.out.println("Artists with NAME like " + name + " not found...");
        }
    }
}
