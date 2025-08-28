package com.kodilla.rps;

public enum Move {
    ROCK("rock"),
    PAPER("paper"),
    SCISSORS("scissors"),
    LIZARD("lizard"),
    SPOCK("Spock");

    private final String label;
    Move(String label) { this.label = label; }
    public String label() { return label; }
}
