package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

public class CheatMessage extends BaseMessage {
    private String playerName;

    public CheatMessage() {}

    public CheatMessage(String playerToCheck) {
        this.playerName = playerToCheck;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
