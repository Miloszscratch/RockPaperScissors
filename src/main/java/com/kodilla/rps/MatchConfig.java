package com.kodilla.rps;

public class MatchConfig implements Cloneable {
    public GameMode mode = GameMode.PVC;
    public String player1Name = "Player 1";
    public String player2Name = "Computer";
    public int roundsToWin = 3;
    public boolean extended = false;
    public boolean cheatMode = false;

    public static MatchConfig defaults() { return new MatchConfig(); }

    public MatchConfig copy() {
        MatchConfig c = new MatchConfig();
        c.mode = this.mode;
        c.player1Name = this.player1Name;
        c.player2Name = this.player2Name;
        c.roundsToWin = this.roundsToWin;
        c.extended = this.extended;
        c.cheatMode = this.cheatMode;
        return c;
    }

    public void ensureComputerName() {
        if (player2Name == null || player2Name.isBlank()) player2Name = "Computer";
    }

    public String displayP2Name() {
        return mode == GameMode.PVC ? (player2Name == null || player2Name.isBlank() ? "Computer" : player2Name)
                : (player2Name == null || player2Name.isBlank() ? "Player 2" : player2Name);
    }
}
