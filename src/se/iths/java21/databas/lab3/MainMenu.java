package se.iths.java21.databas.lab3;

import se.iths.java21.databas.lab3.database.DBCommands;
import se.iths.java21.databas.lab3.tools.Command;
import se.iths.java21.databas.lab3.tools.InputHandler;

import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final Command[] commands = new Command[8];

    public MainMenu() {
        commands[1] = DBCommands::addArtist;
        commands[2] = DBCommands::deleteArtist;
        commands[3] = DBCommands::updateArtist;
        commands[4] = DBCommands::showAll;
        commands[5] = DBCommands::findByID;
        commands[6] = DBCommands::findByAge;
        commands[7] = DBCommands::findByName;
        commands[0] = this::shutDown;
    }

    public void run() {
        int choice;

        do {
            printMenuOptions();
            choice = readChoice();
            executeChoice(choice);
        } while (choice != 0);
    }

    private void printMenuOptions() {
        System.out.println("""
                
                        +--------+ +-----------+ +-----------+ +-------------+ +---------------+ +----------------+ +-----------------+ +---------+
                ARTISTS | 1. Add | | 2. Delete | | 3. Update | | 4. Show All | | 5. Find by ID | | 6. Find by Age | | 7. Find by Name | | 0. Exit |
                        +--------+ +-----------+ +-----------+ +-------------+ +---------------+ +----------------+ +-----------------+ +---------+
                
                Gör ditt menyval genom att skriva SIFFRAN och sedan trycka ENTER!
                ↓ Skriv här ↓""");
    }

    private int readChoice() {
        return InputHandler.getIntegerInput();
    }

    private void executeChoice(int choice) {
        try {
            commands[choice].execute();
        } catch (ArrayIndexOutOfBoundsException ignore) {
            System.out.println("Invalid choice, try again\n");
        }
    }

    private void shutDown() {
        System.exit(0);
    }
}
