package at.aau.ase;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.CheatMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

import org.mockito.Mockito;
import org.mockito.Mockito.*;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @Test
    public void cheatmessageIsActuallyCheatingTest() {
        List<Player> players = new ArrayList<>();
        String name1 = "Username-1";
        int connectionid1 = 1;
        Player player1 = new Player(name1,connectionid1) ;
        String name2 = "Username-2";
        int connectionid2 = 2;
        Player player2 = new Player(name2,connectionid2);
        String name3 = "Username-3";
        int connectionid3 = 3;
        Player player3 = new Player(name3, connectionid3);
        players.add(player1);
        players.add(player2);
        players.add(player3);

        Game game = Mockito.mock(Game.class);
        when(game.getPlayers()).thenReturn(players);

        CheatDetector cheatDetector = Mockito.mock(CheatDetectorImpl.class);
        boolean isActuallyCheating = true;
        when(cheatDetector.check(name2)).thenReturn(isActuallyCheating); // Player2 cheats
        when(game.getCheatDetector()).thenReturn(cheatDetector);

        WizardServer server = new WizardServer();
        ServerCallback serverCallback = new ServerCallback(server, players);
        serverCallback.setGame(game);

        CheatMessage cheatMessage = new CheatMessage(name2, player1); // Player1 checks if Player2 cheats
        serverCallback.callback(cheatMessage);

        verify(game, times(1)).updateScoresCheating(isActuallyCheating, name2, name1);
    }

    @Test
    public void cheatmessageIsNotActuallyCheatingTest() {
        List<Player> players = new ArrayList<>();
        String name1 = "Username-1";
        int connectionid1 = 1;
        Player player1 = new Player(name1,connectionid1) ;
        String name2 = "Username-2";
        int connectionid2 = 2;
        Player player2 = new Player(name2,connectionid2);
        String name3 = "Username-3";
        int connectionid3 = 3;
        Player player3 = new Player(name3, connectionid3);
        players.add(player1);
        players.add(player2);
        players.add(player3);

        Game game = Mockito.mock(Game.class);
        when(game.getPlayers()).thenReturn(players);

        CheatDetector cheatDetector = Mockito.mock(CheatDetectorImpl.class);
        boolean isActuallyCheating = false;
        when(cheatDetector.check(name2)).thenReturn(isActuallyCheating); // Player2 cheats
        when(game.getCheatDetector()).thenReturn(cheatDetector);

        WizardServer server = new WizardServer();
        ServerCallback serverCallback = new ServerCallback(server, players);
        serverCallback.setGame(game);

        CheatMessage cheatMessage = new CheatMessage(name2, player1); // Player1 checks if Player2 cheats
        serverCallback.callback(cheatMessage);

        verify(game, times(1)).updateScoresCheating(isActuallyCheating, name2, name1);
    }
}
