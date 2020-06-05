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

/**
 * Tests for following method of the game logic:
 * checkCurrentTrickRound, waitSafe();
 * CTR short for CurrentTrickRound
 */
public class TestGameLogicCheckCTR {
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

        // three cards have to lay on the table to check trick winner
        Card card1 = new Card(Color.WIZARD, Value.WIZARD);
        card1.setPlayedBy(0);
        game.getTable().add(card1);
        Card card2 = new Card(Color.JESTER, Value.JESTER);
        card2.setPlayedBy(1);
        game.getTable().add(card2);
        Card card3 = new Card(Color.GREEN, Value.EIGHT);
        card3.setPlayedBy(2);
        game.getTable().add(card3);
    }

    @Test
    public void testCheckCTR1() {
        game.dealCards();
        Assert.assertEquals(0, game.getTrickRoundTurn());
        Assert.assertEquals(2, game.getActivePlayerIndex());
        Assert.assertEquals(3, game.getActivePlayerID());
    }

    @Test
    public void testCheckCTRIncrement() {
        game.dealCards();
        game.checkCurrentTrickRound();
        Assert.assertEquals(1, game.getTrickRoundTurn());
        Assert.assertEquals(0, game.getActivePlayerIndex());
        Assert.assertEquals(1, game.getActivePlayerID());
    }

    @Test
    public void testCheckCTRIncrement2() {
        game.dealCards();
        game.checkCurrentTrickRound();
        game.checkCurrentTrickRound();
        Assert.assertEquals(2, game.getTrickRoundTurn());
        Assert.assertEquals(1, game.getActivePlayerIndex());
        Assert.assertEquals(2, game.getActivePlayerID());
    }

    @Test
    public void testCheckCTRTrickCompleteRoundIncomplete() {
        game.dealCards();
        game.setTrickRoundTurn(2);
        int newActivePlayerIndex = game.checkTrickWinner();
        game.checkCurrentTrickRound();
        Assert.assertEquals(0, game.getTrickRoundTurn());
        Assert.assertEquals(1, game.getCurrentRound());
        Assert.assertEquals(newActivePlayerIndex, game.getActivePlayerIndex());
    }

    @Test
    public void testCheckCTRTrickCompleteRoundComplete() {
        game.dealCards();
        game.setTrickRoundTurn(2);
        game.getPlayerHands()[0].clear();
        game.getPlayerHands()[1].clear();
        game.getPlayerHands()[2].clear();
        int newActivePlayerIndex = game.checkTrickWinner();
        game.checkCurrentTrickRound();
        Assert.assertEquals(0, game.getTrickRoundTurn());
        Assert.assertEquals(2, game.getCurrentRound());
        Assert.assertEquals(newActivePlayerIndex, game.getActivePlayerIndex());
    }

    @Test
    public void testCheckCTRTrickCompleteRoundAndGameComplete() {
        game.dealCards();
        game.setTrickRoundTurn(2);
        game.setCurrentRound(game.getTotalRounds());
        game.getPlayerHands()[0].clear();
        game.getPlayerHands()[1].clear();
        game.getPlayerHands()[2].clear();
        game.checkCurrentTrickRound();
        Assert.assertEquals(0, game.getTrickRoundTurn());
        Assert.assertEquals(20, game.getCurrentRound());
    }

    @Test
    public void testWaitSafeInterrupted() {
        Thread.currentThread().interrupt();
        game.waitSafe(5000);
        Assert.assertTrue(Thread.interrupted());
    }


}
