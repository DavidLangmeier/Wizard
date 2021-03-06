package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;

public class StateMessage extends BaseMessage {
    Hand table;         // for the cards within 1 trickround
    Notepad scores;
    Color trump;
    int roundsLeft;
    int dealer;
    int activePlayer;
    int betTricksCounter;
    boolean clearBetTricks;
    int activePlayerIndex;

    public StateMessage() {
    }

    public StateMessage(Hand table, Notepad scores, Color trump, int roundsLeft,
                        int dealer, int activePlayer, int betTricksCounter, boolean clearBetTricks, int activePlayerIndex) {
        this.table = table;
        this.scores = scores;
        this.roundsLeft = roundsLeft;
        this.trump = trump;
        this.dealer = dealer;
        this.activePlayer = activePlayer;
        this.betTricksCounter = betTricksCounter;
        this.clearBetTricks = clearBetTricks;
        this.activePlayerIndex = activePlayerIndex;
    }

    public Hand getTable() {
        return table;
    }

    public Notepad getScores() {
        return scores;
    }

    public Color getTrump() {
        return trump;
    }

    public int getRoundsLeft() {
        return roundsLeft;
    }

    public int getDealer() {
        return dealer;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public int getBetTricksCounter() {
        return betTricksCounter;
    }

    public boolean isClearBetTricks() {
        return clearBetTricks;
    }

    public int getActivePlayerIndex() {
        return activePlayerIndex;
    }
}
