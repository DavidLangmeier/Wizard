package at.aau.ase;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.WizardConstants;

public class TestGame {

    @Test
    public void getActiveColorStandardCaseTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player-1", 1));
        players.add(new Player("Player-2", 2));
        players.add(new Player("Player-3", 3));

        Hand table = new Hand();
        ArrayList<Card> tablecards = new ArrayList<>();
        tablecards.add(new Card(Color.BLUE, Value.TEN));
        tablecards.add(new Card(Color.RED, Value.SIX));
        tablecards.add(new Card(Color.YELLOW, Value.ONE));
        table.setCards(tablecards);

        Game game = new Game(Mockito.mock(WizardServer.class), players);
        game.setTable(table);
        Assert.assertEquals(Color.BLUE, game.getActiveColor());
    }

    @Test
    public void getActiveColorJesterCaseTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player-1", 1));
        players.add(new Player("Player-2", 2));
        players.add(new Player("Player-3", 3));

        Hand table = new Hand();
        ArrayList<Card> tablecards = new ArrayList<>();
        tablecards.add(new Card(Color.JESTER, Value.JESTER));
        tablecards.add(new Card(Color.JESTER, Value.JESTER));
        tablecards.add(new Card(Color.YELLOW, Value.ONE));
        table.setCards(tablecards);

        Game game = new Game(Mockito.mock(WizardServer.class), players);
        game.setTable(table);
        Assert.assertEquals(Color.YELLOW, game.getActiveColor());
    }

    @Test
    public void getActiveColorWizardCaseTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player-1", 1));
        players.add(new Player("Player-2", 2));
        players.add(new Player("Player-3", 3));

        Hand table = new Hand();
        ArrayList<Card> tablecards = new ArrayList<>();
        tablecards.add(new Card(Color.WIZARD, Value.WIZARD));
        tablecards.add(new Card(Color.RED, Value.SIX));
        tablecards.add(new Card(Color.YELLOW, Value.ONE));
        table.setCards(tablecards);

        Game game = new Game(new WizardServer(), players);

        Assert.assertNull(game.getActiveColor());
    }

    @Test
    public void getActiveColorNoCardsOnTableCaseTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player-1", 1));
        players.add(new Player("Player-2", 2));
        players.add(new Player("Player-3", 3));

        Hand table = new Hand();
        table.setCards(null);

        Game game = new Game(new WizardServer(), players);

        Assert.assertNull(game.getActiveColor());
    }

    @Test
    public void updateScoresCheatingIfCheatingTest() {
        List<Player> players = new ArrayList<>();
        String player1name = "Player-1";
        String player2name = "Player-2";
        String player3name = "Player-3";
        players.add(new Player(player1name, 1));
        players.add(new Player(player2name, 2));
        players.add(new Player(player3name, 3));

        Game game = new Game(Mockito.mock(WizardServer.class), players);
        int currentround = 4;
        game.setCurrentRound(currentround);
        game.updateScoresCheating(true, player1name, player3name); // Player3 checking if Player1 cheats, what he does
        Notepad scores = game.getScores();

        Assert.assertEquals((int) -WizardConstants.CHEAT_PENALTY, scores.getPointsPerPlayerPerRound()[0][currentround-1]); // Cheater
        Assert.assertEquals((int) WizardConstants.CHEAT_DETECTION_BONUS, scores.getPointsPerPlayerPerRound()[2][currentround-1]); // Checker
    }

    @Test
    public void updateScoresCheatingIfNotCheatingTest() {
        List<Player> players = new ArrayList<>();
        String player1name = "Player-1";
        String player2name = "Player-2";
        String player3name = "Player-3";
        players.add(new Player(player1name, 1));
        players.add(new Player(player2name, 2));
        players.add(new Player(player3name, 3));

        Game game = new Game(Mockito.mock(WizardServer.class), players);
        int currentround = 4;
        game.setCurrentRound(currentround);
        game.updateScoresCheating(false, player2name, player3name); // Player3 checking if Player2 cheats, what he does
        Notepad scores = game.getScores();

        Assert.assertEquals((int) -WizardConstants.CHEAT_PENALTY, scores.getPointsPerPlayerPerRound()[2][currentround-1]);
    }

    @Test
    public void matchPlayerNameToPlayerIdxTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player-1", 1));
        players.add(new Player("Player-2", 2));
        players.add(new Player("Player-3", 3));

        Game game = new Game(new WizardServer(), players);

        // the expected PlayerIdx is not the connectionID of the Players in the list.
        Assert.assertEquals(0, game.matchPlayerNameToPlayerIdx("Player-1"));
        Assert.assertEquals(1, game.matchPlayerNameToPlayerIdx("Player-2"));
        Assert.assertEquals(2, game.matchPlayerNameToPlayerIdx("Player-3"));
    }
}
