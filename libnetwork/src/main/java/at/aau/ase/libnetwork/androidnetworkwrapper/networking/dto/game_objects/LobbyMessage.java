package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

public class LobbyMessage extends BaseMessage {

    private String newUsername = null;
    private List<Player> players = null;

    public LobbyMessage() {}

    public LobbyMessage(String username) {
        this.newUsername = username;
    }

    public LobbyMessage(List<Player> players) {
        this.players = players;
    }

    public LobbyMessage(List<Player> players, String newUsername) {
        this.newUsername = newUsername;
        this.players = players;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public List<Player> getPlayer() {
        return players;
    }
}
