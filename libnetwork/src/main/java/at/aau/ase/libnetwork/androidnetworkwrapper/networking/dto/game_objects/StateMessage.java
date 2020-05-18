package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

public class StateMessage extends BaseMessage {
    Hand table;         // for the cards within 1 trickround
    Notepad scores;
    Card trump;
    int roundsLeft;
    int currentRound;
    int dealer;
    int activePlayer;
    int betTricksCounter;
    boolean clearBetTricks;

    public StateMessage() {
    }

    public StateMessage(Hand table, Notepad scores, Card trump, int roundsLeft,
                        int dealer, int activePlayer, int betTricksCounter, boolean clearBetTricks) {
        this.table = table;
        this.scores = scores;
        this.roundsLeft = roundsLeft;
        this.trump = trump;
        this.dealer = dealer;
        this.activePlayer = activePlayer;
        this.betTricksCounter = betTricksCounter;
        this.clearBetTricks = clearBetTricks;
    }

    public Hand getTable() {
        return table;
    }

    public Notepad getScores() {
        return scores;
    }

    public Card getTrump() {
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

    public int getCurrentRound() {
        return currentRound;
    }
}
