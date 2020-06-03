package at.aau.ase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

/**
 * Tests for following methods of the game logic:
 * checkBet(), writeBetTricksToNotePad()
 */
public class TestGameLogicBetting {
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
    public void testCheckBetNegativeNumber() {
        int bet = -1;
        Assert.assertFalse(game.checkBet(bet));
    }

    @Test
    public void testCheckBetRound1True() {
        int bet = 1;
        Assert.assertTrue(game.checkBet(bet));
    }

    @Test
    public void testCheckBetRound2True() {
        game.setCurrentRound(2);
        int bet = 0;
        Assert.assertTrue(game.checkBet(bet));
    }

    @Test
    public void testCheckBetRound1False() {
        int bet = 2;
        Assert.assertFalse(game.checkBet(bet));
    }

    @Test
    public void testCheckBetRound20True1() {
        game.setCurrentRound(20);
        int bet = 10;
        Assert.assertTrue(game.checkBet(bet));
    }

    @Test
    public void testCheckBetRound20True2() {
        game.setCurrentRound(20);
        int bet = 20;
        Assert.assertTrue(game.checkBet(bet));
    }

    @Test
    public void testCheckBetRound20False1() {
        game.setCurrentRound(20);
        int bet = 21;
        Assert.assertFalse(game.checkBet(bet));
    }

    @Test
    public void testWriteBetToNotePadRound1() {
        game.writeBetTricksToNotePad(game.getScores(), 0, 1 );
        game.writeBetTricksToNotePad(game.getScores(), 1, 1 );
        game.writeBetTricksToNotePad(game.getScores(), 2, 0 );
        Assert.assertEquals(game.getScores().getBetTricksPerPlayerPerRound()[0][0], 1);
        Assert.assertEquals(game.getScores().getBetTricksPerPlayerPerRound()[1][0], 1);
        Assert.assertEquals(game.getScores().getBetTricksPerPlayerPerRound()[2][0], 0);
    }

    @Test
    public void testWriteBetToNotePadRound5() {
        game.setCurrentRound(5);
        game.writeBetTricksToNotePad(game.getScores(), 0, 3 );
        game.writeBetTricksToNotePad(game.getScores(), 1, 2 );
        game.writeBetTricksToNotePad(game.getScores(), 2, 5 );
        Assert.assertEquals(game.getScores().getBetTricksPerPlayerPerRound()[0][4], 3);
        Assert.assertEquals(game.getScores().getBetTricksPerPlayerPerRound()[1][4], 2);
        Assert.assertEquals(game.getScores().getBetTricksPerPlayerPerRound()[2][4], 5);
    }

}
