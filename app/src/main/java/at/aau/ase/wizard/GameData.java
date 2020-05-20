package at.aau.ase.wizard;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.NotePadMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;

public class GameData {
    private Hand table;
    private Hand myHand;
    private Notepad scores;
    private Color trump;
    private int roundsLeft;
    private int dealer;
    private int activePlayer;
    private int numberOfPlayers;
    private int betTricksCounter;

    public GameData() {}

    public void updateState(StateMessage stateMessage) {
        this.table = stateMessage.getTable();
        this.scores = stateMessage.getScores();
        this.trump = stateMessage.getTrump();
        this.roundsLeft = stateMessage.getRoundsLeft();
        this.dealer = stateMessage.getDealer();
        this.activePlayer = stateMessage.getActivePlayer();
        this.betTricksCounter = stateMessage.getBetTricksCounter();
    }

    public void setMyHand(HandMessage handMessage) {
        this.myHand = handMessage.getHand();
    }

    public void setScores(NotePadMessage notePadMessage) {
        this.scores = notePadMessage.getScores();
    }

    public Hand getTable() {
        return table;
    }

    public Hand getMyHand() {
        return myHand;
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
}




