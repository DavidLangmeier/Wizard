package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;

public class EndscreenMessage extends BaseMessage {
    private int [][] sortedTotalPoints;
    private List sortedplayerNames;
    private int [] imageID;
    Notepad scores;

    public EndscreenMessage(){}

    public EndscreenMessage(Notepad scores){
        this.scores = scores;
    }

    public EndscreenMessage(Notepad scores, int [] imageID){
       this.scores = scores;
        this.imageID = imageID;
    }

    public int[] getImageID() {
        return imageID;
    }

    public Notepad getScores() {
        return scores;
    }
}
