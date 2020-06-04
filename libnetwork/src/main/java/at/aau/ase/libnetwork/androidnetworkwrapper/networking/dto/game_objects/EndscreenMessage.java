package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;

public class EndscreenMessage extends BaseMessage {
    private Notepad scores;
    private int [][] sortedTotalPoints;
    private List sortedplayerNames;
    private int [] imageID;

    public EndscreenMessage(){}

    public EndscreenMessage(int [][] sortedTotalPoints, List sortedplayerNames){
        this.sortedplayerNames = sortedplayerNames;
        this.sortedTotalPoints = sortedTotalPoints;
    }

    public EndscreenMessage(int [][] sortedTotalPoints, List sortedplayerNames, int [] imageID){
        this.sortedplayerNames = sortedplayerNames;
        this.sortedTotalPoints = sortedTotalPoints;
        this.imageID = imageID;
    }

    public int[][] getSortedTotalPoints() {
        return sortedTotalPoints;
    }

    public List getSortedplayerNames() {
        return sortedplayerNames;
    }

    public Notepad getScores() {
        return scores;
    }

    public int[] getImageID() {
        return imageID;
    }
}
