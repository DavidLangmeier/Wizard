package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;

public class NotePadMessage extends BaseMessage{
    private Notepad scores;
    private int activePlayer;
    private int betTricks;

    public NotePadMessage(){}

    public NotePadMessage(Notepad scores, int activePlayer, int betTricks){
        this.scores = scores;
        this.activePlayer = activePlayer;
        this.betTricks = betTricks;
    }
    public NotePadMessage(Notepad scores) {
        this.scores = scores;
    }

    public Notepad getScores() {
        return scores;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public int getBetTricks() {
        return betTricks;
    }

    public void setScores(Notepad scores) {
        this.scores = scores;
    }

}
