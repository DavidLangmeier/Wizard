package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

public class LobbyMessage extends BaseMessage {

    private String newUsername = null;
    private Player player = null;

    public LobbyMessage() {}

    public LobbyMessage(String username) {
        this.newUsername = username;
    }

    public LobbyMessage(Player player) {
        this.player = player;
    }

    public LobbyMessage(Player player, String newUsername) {
        this.newUsername = newUsername;
        this.player = player;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public Player getPlayer() {
        return player;
    }
}
