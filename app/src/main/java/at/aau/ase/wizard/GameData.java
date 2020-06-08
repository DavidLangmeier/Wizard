package at.aau.ase.wizard;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.NotePadMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;

class GameData {
    private Hand table;
    private Hand myHand;
    private Notepad scores;
    private Color trump;
    private int roundsLeft;
    private int dealer;
    private int activePlayer;
    private int betTricksCounter;
    private int activePlayerIndex;

    GameData() {
    }

    void updateState(StateMessage stateMessage) {
        this.table = stateMessage.getTable();
        this.scores = stateMessage.getScores();
        this.trump = stateMessage.getTrump();
        this.roundsLeft = stateMessage.getRoundsLeft();
        this.dealer = stateMessage.getDealer();
        this.activePlayer = stateMessage.getActivePlayer();
        this.betTricksCounter = stateMessage.getBetTricksCounter();
        this.activePlayerIndex = stateMessage.getActivePlayerIndex();
    }

    void setMyHand(HandMessage handMessage) {
        this.myHand = handMessage.getHand();
    }

    void setScores(NotePadMessage notePadMessage) {
        this.scores = notePadMessage.getScores();
    }

    Hand getTable() {
        return table;
    }

    Hand getMyHand() {
        return myHand;
    }

    Notepad getScores() {
        return scores;
    }

    Color getTrump() {
        return trump;
    }

    int getRoundsLeft() {
        return roundsLeft;
    }


    int getActivePlayer() {
        return activePlayer;
    }

    int getBetTricksCounter() {
        return betTricksCounter;
    }

    int getActivePlayerIndex() {
        return activePlayerIndex;
    }
}




