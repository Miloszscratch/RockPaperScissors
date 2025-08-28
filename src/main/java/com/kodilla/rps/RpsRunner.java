package com.kodilla.rps;

import java.util.Random;
import java.util.Scanner;

public class RpsRunner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random  random  = new Random();

        MatchConfig config = MatchConfig.defaults();
        SessionStats stats = new SessionStats();

        MainMenu menu = new MainMenu(scanner, random, config, stats);
        menu.loop();

        scanner.close();
        System.out.println("Bye! 👋");
    }
}
