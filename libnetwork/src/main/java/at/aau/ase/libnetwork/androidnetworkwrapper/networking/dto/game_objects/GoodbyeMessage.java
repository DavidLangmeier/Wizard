package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

public class GoodbyeMessage extends BaseMessage {
    String goodbye = null;
    Player player = null;

    public GoodbyeMessage() {}

    public GoodbyeMessage(String goodbyeString, Player playerLeaving) {
        this.goodbye = goodbyeString;
        this.player = playerLeaving;
    }

    public String getGoodbye() {
        return goodbye;
    }

    public void setGoodbye(String goodbye) {
        this.goodbye = goodbye;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
