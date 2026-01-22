package org.example.cli;

import java.util.Scanner;

public final class CommandLoop {

    private final CommandHandler handler;
    private final Scanner scanner = new Scanner(System.in);

    public CommandLoop(CommandHandler handler) {
        this.handler = handler;
    }

    public void run() {
        System.out.println("Welcome to Bill Payment CLI!");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();

            if (line == null || line.isBlank()) {
                continue;
            }

            if (line.equalsIgnoreCase("EXIT")) {
                System.out.println("Good bye!");
                break;
            }

            try {
                handler.handle(line);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
