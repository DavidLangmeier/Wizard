package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

public class CheatMessage extends BaseMessage {
    private String playerName;
    private Player sender;
    private String message;

    public CheatMessage() {}

    public CheatMessage(String message) {
        this.message = message;
    }

    public CheatMessage(String playerToCheck, Player sender) {
        this.playerName = playerToCheck;
        this.sender = sender;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Player getSender() {
        return this.sender;
    }

    public String getMessage() {
        return message;
    }
}
