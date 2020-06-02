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
 * Tests for following methods of the game logic:
 * checkTrickWinner(), dealOnePlayerCardToTable
 * TW short for TrickWinner
 */
public class TestGameLogicCheckTW {
    private List<Player> players = new ArrayList<>();
    private Game game;
    Card cardWiz = new Card(Color.WIZARD, Value.WIZARD);
    Card cardWiz2 = new Card(Color.WIZARD, Value.WIZARD);
    Card cardWiz3 = new Card(Color.WIZARD, Value.WIZARD);
    Card cardJes = new Card(Color.JESTER, Value.JESTER);
    Card cardJes2 = new Card(Color.JESTER, Value.JESTER);
    Card cardJes3 = new Card(Color.JESTER, Value.JESTER);
    Card cardGreenOne = new Card(Color.GREEN, Value.ONE);
    Card cardGreenEight = new Card(Color.GREEN, Value.EIGHT);
    Card cardGreenTen = new Card(Color.GREEN, Value.TEN);
    Card cardBlueOne = new Card(Color.BLUE, Value.ONE);
    Card cardBlueEight = new Card(Color.BLUE, Value.EIGHT);
    Card cardBlueTen = new Card(Color.BLUE, Value.TEN);
    Card cardRedOne = new Card(Color.RED, Value.ONE);
    Card cardRedEight = new Card(Color.RED, Value.EIGHT);
    Card cardRedTen = new Card(Color.RED, Value.TEN);
    Card cardYellowOne = new Card(Color.YELLOW, Value.ONE);
    Card cardYellowEight = new Card(Color.YELLOW, Value.EIGHT);
    Card cardYellowTen = new Card(Color.YELLOW, Value.TEN);


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
    public void testThreeWizards() {
        cardWiz.setPlayedBy(0);
        game.getTable().add(cardWiz);
        cardWiz2.setPlayedBy(1);
        game.getTable().add(cardWiz2);
        cardWiz3.setPlayedBy(2);
        game.getTable().add(cardWiz3);
        Assert.assertEquals(0, game.checkTrickWinner());
    }

    @Test
    public void testThreeJesters() {
        cardJes.setPlayedBy(0);
        game.getTable().add(cardJes);
        cardJes2.setPlayedBy(1);
        game.getTable().add(cardJes2);
        cardJes3.setPlayedBy(2);
        game.getTable().add(cardJes3);
        Assert.assertEquals(0, game.checkTrickWinner());
    }

    @Test
    public void testWizardFirstCard() {
        game.setTrump(Color.BLUE);
        cardWiz.setPlayedBy(0);
        game.getTable().add(cardWiz);
        cardBlueEight.setPlayedBy(1);
        game.getTable().add(cardBlueEight);
        cardWiz2.setPlayedBy(2);
        game.getTable().add(cardWiz2);
        Assert.assertEquals(0, game.checkTrickWinner());
    }

    @Test
    public void testWizardSecondCard() {
        game.setTrump(Color.BLUE);
        cardBlueEight.setPlayedBy(0);
        game.getTable().add(cardBlueEight);
        cardWiz.setPlayedBy(1);
        game.getTable().add(cardWiz);
        cardWiz2.setPlayedBy(2);
        game.getTable().add(cardWiz2);
        Assert.assertEquals(1, game.checkTrickWinner());
    }

    @Test
    public void testWizardThirdCard() {
        game.setTrump(Color.BLUE);
        cardBlueEight.setPlayedBy(0);
        game.getTable().add(cardBlueEight);
        cardJes.setPlayedBy(1);
        game.getTable().add(cardJes);
        cardWiz2.setPlayedBy(2);
        game.getTable().add(cardWiz2);
        Assert.assertEquals(2, game.checkTrickWinner());
    }

    @Test
    public void testHighestValue() {
        game.setTrump(Color.RED);
        cardGreenOne.setPlayedBy(0);
        game.getTable().add(cardGreenOne);
        cardGreenTen.setPlayedBy(1);
        game.getTable().add(cardGreenTen);
        cardGreenEight.setPlayedBy(2);
        game.getTable().add(cardGreenEight);
        Assert.assertEquals(1, game.checkTrickWinner());
    }

    @Test
    public void testHighestValueTrump() {
        game.setTrump(Color.GREEN);
        cardGreenOne.setPlayedBy(0);
        game.getTable().add(cardGreenOne);
        cardGreenTen.setPlayedBy(1);
        game.getTable().add(cardGreenTen);
        cardGreenEight.setPlayedBy(2);
        game.getTable().add(cardGreenEight);
        Assert.assertEquals(1, game.checkTrickWinner());
    }

    @Test
    public void testTrump() {
        game.setTrump(Color.YELLOW);
        cardGreenEight.setPlayedBy(0);
        game.getTable().add(cardGreenEight);
        cardGreenTen.setPlayedBy(1);
        game.getTable().add(cardGreenTen);
        cardYellowOne.setPlayedBy(2);
        game.getTable().add(cardYellowOne);
        Assert.assertEquals(2, game.checkTrickWinner());
    }

    @Test
    public void testAllDifferentColors() {
        game.setTrump(Color.YELLOW);
        cardGreenEight.setPlayedBy(0);
        game.getTable().add(cardGreenEight);
        cardRedTen.setPlayedBy(1);
        game.getTable().add(cardRedTen);
        cardBlueOne.setPlayedBy(2);
        game.getTable().add(cardBlueOne);
        Assert.assertEquals(0, game.checkTrickWinner());
    }

    @Test
    public void testFirstCardJester() {
        game.setTrump(Color.GREEN);
        cardJes.setPlayedBy(0);
        game.getTable().add(cardJes);
        cardBlueEight.setPlayedBy(1);
        game.getTable().add(cardBlueEight);
        cardBlueOne.setPlayedBy(2);
        game.getTable().add(cardBlueOne);
    }

    @Test
    public void testDealPlayerCardToTable() {
        game.getPlayerHands()[0].add(cardRedTen);
        game.setActivePlayerIndex(0);
        game.dealOnePlayerCardToTable(cardRedTen);
        Assert.assertEquals("Red", game.getTable().getCards().get(0).getColor().getColorName());
        Assert.assertEquals(10, game.getTable().getCards().get(0).getValue().getValueCode());
        Assert.assertEquals(0, game.getTable().getCards().get(0).getPlayedBy());
    }


}