package com.kodilla.rps;

import java.util.EnumSet;

import static com.kodilla.rps.Move.*;

public enum Result {
    WIN, LOSE, DRAW;

    public static Result of(Move a, Move b) {
        if (a == b) return DRAW;
        return switch (a) {
            case ROCK     -> (b == SCISSORS || b == LIZARD) ? WIN : LOSE;
            case PAPER    -> (b == ROCK || b == SPOCK)      ? WIN : LOSE;
            case SCISSORS -> (b == PAPER || b == LIZARD)    ? WIN : LOSE;
            case LIZARD   -> (b == SPOCK || b == PAPER)     ? WIN : LOSE;
            case SPOCK    -> (b == SCISSORS || b == ROCK)   ? WIN : LOSE;
        };
    }

    public EnumSet<Move> movesAgainst(Move against) {
        EnumSet<Move> set = EnumSet.noneOf(Move.class);
        for (Move m : Move.values()) {
            if (of(against, m) == this) set.add(m);
        }
        return set;
    }
}
