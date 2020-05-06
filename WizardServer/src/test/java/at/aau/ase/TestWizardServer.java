package at.aau.ase;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

public class TestWizardServer {

    @Test
    public void lobbymessageTest() {
        List<Player> players = new ArrayList<>();
        String name = "Username";
        Player player = new Player(name);

        LobbyMessage lobbyMessage = new LobbyMessage(player, name);
        ServerCallback serverCallback = new ServerCallback(new WizardServer(), players);
        serverCallback.callback(lobbyMessage);
    }
}
