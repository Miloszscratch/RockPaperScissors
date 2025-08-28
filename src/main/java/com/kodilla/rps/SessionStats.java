package com.kodilla.rps;

public class SessionStats {
    public int totalMatches = 0;
    public int p1MatchesWon = 0;
    public int p2OrCpuMatchesWon = 0;

    @Override public String toString() {
        return "Session Stats\n" +
                "  Matches played : " + totalMatches + "\n" +
                "  Player 1 wins  : " + p1MatchesWon + "\n" +
                "  Opponent wins  : " + p2OrCpuMatchesWon + "\n";
    }
}
