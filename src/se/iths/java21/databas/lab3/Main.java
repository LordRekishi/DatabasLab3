package se.iths.java21.databas.lab3;


import se.iths.java21.databas.lab3.database.DBCommands;

public class Main {
    public static void main(String[] args) {
        DBCommands.setupConnection();
        DBCommands.setupDatabase();
        DBCommands.setupArtistTable();
        MainMenu mainMenu = new MainMenu();
        mainMenu.run();
    }
}
