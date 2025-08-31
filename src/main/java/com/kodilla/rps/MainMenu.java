package com.kodilla.rps;

import java.util.Random;
import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner;
    private final Random random;
    private final MatchConfig config;
    private final SessionStats stats;

    public MainMenu(Scanner scanner, Random random, MatchConfig config, SessionStats stats) {
        this.scanner = scanner;
        this.random  = random;
        this.config  = config;
        this.stats   = stats;
    }

    public void loop() {
        boolean exit = false;
        while (!exit) {
            printHeader();
            System.out.println("1) Play Player vs Computer");
            System.out.println("2) Play Player vs Player");
            System.out.println("3) Settings");
            System.out.println("4) View Session Stats");
            System.out.println("x) Exit");
            System.out.print("> ");

            String choice = readLineSafe();
            if (choice == null) {
                System.out.println();
                System.out.println("(No input detected. Exiting.)");
                return;
            }
            choice = choice.trim().toLowerCase();

            switch (choice) {
                case "1" -> play(GameMode.PVC);
                case "2" -> play(GameMode.PVP);
                case "3" -> settings();
                case "4" -> {
                    System.out.println();
                    System.out.println(stats);
                    System.out.println();
                }
                case "x" -> exit = confirm("Are you sure you want to exit the app? (y/n): ");
                default  -> System.out.println("Invalid option. Try again.\n");
            }
        }
    }


    private String readLineSafe() {
        try {
            if (!scanner.hasNextLine()) return null;
            return scanner.nextLine();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = readLineSafe();
            if (s == null) return false;
            s = s.trim().toLowerCase();
            if (s.equals("y")) return true;
            if (s.equals("n")) return false;
            System.out.println("Please answer with 'y' or 'n'.");
        }
    }

    private boolean confirm(String q) { return askYesNo(q); }

    private int askInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = readLineSafe();
            if (line == null) {
                System.out.println();
                System.out.println("(No input detected. Using minimum value " + min + ".)");
                return min;
            }
            try {
                int v = Integer.parseInt(line.trim());
                if (v < min || v > max) {
                    System.out.printf("Enter an integer in range [%d..%d].%n", min, max);
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.printf("Enter an integer in range [%d..%d].%n", min, max);
            }
        }
    }

    private String askNonEmpty(String prompt, String fallback) {
        System.out.print(prompt);
        String s = readLineSafe();
        if (s == null) return fallback;
        s = s.trim();
        return s.isEmpty() ? fallback : s;
    }


    private void play(GameMode mode) {
        MatchConfig matchCfg = config.copy();
        matchCfg.mode = mode;
        if (mode == GameMode.PVC) matchCfg.ensureComputerName();

        Game game = new Game(matchCfg, scanner, random);
        Game.Outcome outcome = game.runMatch();

        if (outcome == Game.Outcome.PLAYER1_WIN) stats.p1MatchesWon++;
        else if (outcome == Game.Outcome.PLAYER2_OR_CPU_WIN) stats.p2OrCpuMatchesWon++;
        if (outcome != Game.Outcome.ABORTED) stats.totalMatches++;
    }

    private void settings() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- Settings ---------------------------------------------------");
            System.out.printf("1) Target wins: %d%n", config.roundsToWin);
            System.out.printf("2) Extended moves (Lizard-Spock): %s%n", yesNo(config.extended));
            System.out.printf("3) Cheat mode (computer bias 50/25/25): %s%n", yesNo(config.cheatMode));
            System.out.printf("4) Player 1 name: %s%n", config.player1Name);
            System.out.printf("5) Player 2 name (PvP) / Computer name (PvC): %s%n", config.player2Name);
            System.out.println("b) Back");
            System.out.print("> ");

            String c = readLineSafe();
            if (c == null) return;
            c = c.trim().toLowerCase();

            switch (c) {
                case "1" -> config.roundsToWin = askInt("Play to how many WINS? (1..100): ", 1, 100);
                case "2" -> config.extended = askYesNo("Enable Lizard-Spock? (y/n): ");
                case "3" -> config.cheatMode = askYesNo("Enable cheat mode (only for PvC)? (y/n): ");
                case "4" -> config.player1Name = askNonEmpty("Player 1 name: ", "Player 1");
                case "5" -> config.player2Name = askNonEmpty("Player 2/Computer name: ", "Computer");
                case "b" -> back = true;
                default  -> System.out.println("Invalid option.\n");
            }
        }
    }

    private String yesNo(boolean b) { return b ? "yes" : "no"; }

    private void printHeader() {
        System.out.println();
        System.out.println("===============================================================");
        System.out.println("   ROCK / PAPER / SCISSORS" + (config.extended ? " / LIZARD / SPOCK" : ""));
        System.out.println("===============================================================");
    }
}
