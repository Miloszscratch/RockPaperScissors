package com.kodilla.rps;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    public enum Outcome { PLAYER1_WIN, PLAYER2_OR_CPU_WIN, ABORTED }

    private final MatchConfig cfg;
    private final Scanner scanner;
    private final Random random;

    private int p1Wins = 0;
    private int p2Wins = 0;
    private final List<Move> legalMoves;

    private char lastCommand = 0;

    public Game(MatchConfig cfg, Scanner scanner, Random random) {
        this.cfg = cfg;
        this.scanner = scanner;
        this.random  = random;
        this.legalMoves = cfg.extended
                ? List.of(Move.ROCK, Move.PAPER, Move.SCISSORS, Move.LIZARD, Move.SPOCK)
                : List.of(Move.ROCK, Move.PAPER, Move.SCISSORS);
    }

    public Outcome runMatch() {
        printIntro();
        boolean end = false;

        while (!end) {
            printScore();

            Move p1 = readHumanMove(cfg.player1Name);
            if (p1 == null) {
                if (lastCommand == 'x' && confirm("Really exit match? (y/n): ")) return Outcome.ABORTED;
                if (lastCommand == 'n' && confirm("Really restart match? (y/n): ")) { reset(); continue; }
                else continue;
            }

            Move p2;
            if (cfg.mode == GameMode.PVP) {
                p2 = readHumanMove(cfg.player2Name);
                if (p2 == null) { // handle commands from P2 as well
                    if (lastCommand == 'x' && confirm("Really exit match? (y/n): ")) return Outcome.ABORTED;
                    if (lastCommand == 'n' && confirm("Really restart match? (y/n): ")) { reset(); continue; }
                    else continue;
                }
            } else {
                p2 = cfg.cheatMode ? pickBiasedAgainst(p1) : pickRandom();
                System.out.printf("Computer chooses: %s%n", p2.label());
            }

            System.out.printf("Round: %s -> %s   |   %s -> %s%n",
                    cfg.player1Name, p1.label(), cfg.displayP2Name(), p2.label());

            Result r = Result.of(p1, p2);
            switch (r) {
                case WIN  -> { p1Wins++; System.out.println("Round result: " + cfg.player1Name + " wins the round!"); }
                case LOSE -> { p2Wins++; System.out.println("Round result: " + cfg.displayP2Name() + " wins the round!"); }
                case DRAW -> System.out.println("Round result: DRAW.");
            }

            if (p1Wins >= cfg.roundsToWin || p2Wins >= cfg.roundsToWin) {
                System.out.println("------------------------------------");
                System.out.println(summary());
                System.out.println("------------------------------------");
                while (true) {
                    System.out.print("Press 'n' to start a NEW match, 'm' for MAIN MENU, or 'x' to EXIT app: ");
                    String s = scanner.nextLine().trim().toLowerCase();
                    if (s.equals("n")) { reset(); printIntro(); break; }
                    if (s.equals("m")) { return p1Wins > p2Wins ? Outcome.PLAYER1_WIN : Outcome.PLAYER2_OR_CPU_WIN; }
                    if (s.equals("x")) { System.exit(0); }
                }
            }
        }
        return Outcome.ABORTED;
    }

    private void reset() { p1Wins = 0; p2Wins = 0; }

    private void printIntro() {
        System.out.println("\n--- Match ---");
        System.out.printf("Mode: %s | Play to %d wins%n", cfg.mode, cfg.roundsToWin);
        System.out.printf("Players: %s vs %s%n", cfg.player1Name, cfg.displayP2Name());
        System.out.println("Controls:");
        System.out.println("  1 – rock");
        System.out.println("  2 – paper");
        System.out.println("  3 – scissors");
        if (cfg.extended) {
            System.out.println("  4 – lizard");
            System.out.println("  5 – Spock");
        }
        System.out.println("  x – exit match (with confirmation)");
        System.out.println("  n – restart match (with confirmation)\n");
    }

    private void printScore() {
        System.out.printf("Score: %s %d : %d %s%n",
                cfg.player1Name, p1Wins, p2Wins, cfg.displayP2Name());
    }

    private Move readHumanMove(String who) {
        while (true) {
            System.out.printf("%s, make your move (1/2/3%s, x – exit, n – new): ",
                    who, cfg.extended ? "/4/5" : "");
            String s = scanner.nextLine().trim().toLowerCase();
            switch (s) {
                case "1": return Move.ROCK;
                case "2": return Move.PAPER;
                case "3": return Move.SCISSORS;
                case "4": if (cfg.extended) return Move.LIZARD; break;
                case "5": if (cfg.extended) return Move.SPOCK;  break;
                case "x": lastCommand = 'x'; return null;
                case "n": lastCommand = 'n'; return null;
            }
            System.out.println("Invalid choice. Try again.");
        }
    }

    private boolean confirm(String q) {
        while (true) {
            System.out.print(q);
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("y")) return true;
            if (s.equals("n")) return false;
            System.out.println("Answer 'y' or 'n'.");
        }
    }

    private Move pickRandom() {
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }

    private Move pickBiasedAgainst(Move playerMove) {
        double r = random.nextDouble();
        if (r < 0.50) {
            return pickOneOf(Result.LOSE.movesAgainst(playerMove));
        } else if (r < 0.75) {
            return playerMove;
        } else {
            return pickOneOf(Result.WIN.movesAgainst(playerMove));
        }
    }

    private Move pickOneOf(EnumSet<Move> set) {
        Move[] arr = set.stream().filter(legalMoves::contains).toArray(Move[]::new);
        return arr[random.nextInt(arr.length)];
    }

    private String summary() {
        String winner = (p1Wins > p2Wins) ? cfg.player1Name : cfg.displayP2Name();
        return String.format("Match finished: %s WINS (%d : %d).", winner, Math.max(p1Wins, p2Wins), Math.min(p1Wins, p2Wins));
    }
}
