package at.aau.ase;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

import org.mockito.Mockito;
import org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

public class TestWizardServer {

    @Test
    public void lobbymessageTest() {
        List<Player> players = new ArrayList<>();
        String name = "Username";
        Player player = new Player(name);

        WizardServer server = Mockito.mock(WizardServer.class);
        when(server.getLastConnectionID()).thenReturn(1);

        LobbyMessage lobbyMessage = new LobbyMessage(name);
        ServerCallback serverCallback = new ServerCallback(server, players);
        serverCallback.callback(lobbyMessage);

        Assert.assertEquals(player.getName(), players.get(0).getName());
    }
}
