package at.aau.ase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color.BLUE;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color.GREEN;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color.JESTER;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color.RED;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color.WIZARD;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color.YELLOW;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value.ELEVEN;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value.FOUR;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value.ONE;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value.THREE;

/**
 * Tests for following methods of the game logic:
 * start(), dealCards(), checkTrump(), some smaller helper methods
 */
public class TestGameLogicDealCards {
    private List<Player> players = new ArrayList<>();
    private WizardServer server;
    private Game game;


    @Before
    public void init() {
        // add minimum number of players
        players.add(new Player("Hans", 1));
        players.add(new Player("Karin", 2));
        players.add(new Player("Seppi", 3));

        // mock server
        WizardServer server = Mockito.mock(WizardServer.class);

        game = new Game(server, players);
    }


    @Test
    public void testStartGame() {
        game.startGame();
        Assert.assertEquals(true, game.isGamerunning());
    }

    @Test
    public void testDealCardsInRound1() {
        game.dealCards();
        Assert.assertEquals(57, game.getDeck().getCards().size());
        Assert.assertEquals(1, game.getPlayerHands()[0].getCards().size());
        Assert.assertEquals(1, game.getPlayerHands()[1].getCards().size());
        Assert.assertEquals(1, game.getPlayerHands()[2].getCards().size());
    }

    @Test
    public void testDealCardsInRound2() {
        game.setCurrentRound(2);
        game.dealCards();
        Assert.assertEquals(54, game.getDeck().getCards().size());
        Assert.assertEquals(2, game.getPlayerHands()[0].getCards().size());
        Assert.assertEquals(2, game.getPlayerHands()[1].getCards().size());
        Assert.assertEquals(2, game.getPlayerHands()[2].getCards().size());
    }

    @Test
    public void testDealCardsInRound20() {
        game.setCurrentRound(20);
        game.dealCards();
        Assert.assertEquals(0, game.getDeck().getCards().size());
        Assert.assertEquals(20, game.getPlayerHands()[0].getCards().size());
        Assert.assertEquals(20, game.getPlayerHands()[1].getCards().size());
        Assert.assertEquals(20, game.getPlayerHands()[2].getCards().size());
    }

    @Test
    public void testTrumpColorYellow() {
        game.getDeck().clear();
        game.getDeck().add(new Card(YELLOW, ONE));
        Assert.assertEquals(YELLOW, game.checkTrump());
    }

    @Test
    public void testTrumpColorRed() {
        game.getDeck().clear();
        game.getDeck().add(new Card(RED, THREE));
        game.getDeck().add(new Card(YELLOW, ONE));
        Assert.assertEquals(RED, game.checkTrump());
    }

    @Test
    public void testTrumpColorBlue() {
        game.getDeck().clear();
        game.getDeck().add(new Card(BLUE, FOUR));
        game.getDeck().add(new Card(YELLOW, ONE));
        Assert.assertEquals(BLUE, game.checkTrump());
    }

    @Test
    public void testTrumpColorGreen() {
        game.getDeck().clear();
        game.getDeck().add(new Card(GREEN, ELEVEN));
        game.getDeck().add(new Card(YELLOW, ONE));
        Assert.assertEquals(GREEN, game.checkTrump());
    }

    @Test
    public void testTrumpColorWizard() {
        game.getDeck().clear();
        game.getDeck().add(new Card(Color.WIZARD, Value.WIZARD));
        Assert.assertTrue(game.checkTrump() != WIZARD);
    }

    @Test
    public void testTrumpColorJester() {
        game.getDeck().clear();
        game.getDeck().add(new Card(JESTER, Value.JESTER));
        Assert.assertTrue(game.checkTrump() != JESTER);
    }

    @Test
    public void testActivePlayerIndexInRound1() {
        game.dealCards();
        Assert.assertEquals(2, game.getActivePlayerIndex());
        Assert.assertEquals(3, game.getActivePlayerID());
    }

    @Test
    public void testActivePlayerIndexInRound2() {
        game.setCurrentRound(2);
        game.dealCards();
        Assert.assertEquals(0, game.getActivePlayerIndex());
        Assert.assertEquals(1, game.getActivePlayerID());
    }

    @Test
    public void testActivePlayerIndexInRound3() {
        game.setCurrentRound(3);
        game.dealCards();
        Assert.assertEquals(1, game.getActivePlayerIndex());
        Assert.assertEquals(2, game.getActivePlayerID());
    }

    @Test
    public void testActivePlayerIndexInRound20() {
        game.setCurrentRound(20);
        game.dealCards();
        Assert.assertEquals(0, game.getActivePlayerIndex());
        Assert.assertEquals(1, game.getActivePlayerID());
    }

    @Test
    public void testGetTotalRounds() {
        Assert.assertEquals(20, game.getTotalRounds());
    }

    @Test
    public void testSetGameRunningTrue() {
        game.setGamerunning(true);
        Assert.assertTrue(game.isGamerunning());
    }

    @Test
    public void testSetGameRunningFalse() {
        game.setGamerunning(false);
        Assert.assertFalse(game.isGamerunning());
    }

}
