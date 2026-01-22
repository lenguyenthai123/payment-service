package org.example.cli;

import java.util.Arrays;
import java.util.List;

public final class CommandParser {

    public static Command parse(String input) {
        String[] tokens = input.trim().split("\\s+");

        String name = tokens[0].toUpperCase();
        List<String> args = Arrays.asList(tokens).subList(1, tokens.length);

        return new Command(name, args);
    }
}
