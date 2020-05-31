package at.aau.ase;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value;

public class TestCheatDetectorImpl {

    @Test
    public void Player2CheatsTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player-1", 1));
        players.add(new Player("Player-2", 2));
        players.add(new Player("Player-3", 3));

        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getPlayers()).thenReturn(players);
        Mockito.when(game.getActivePlayerIndex()).thenReturn(1); // activePlayerIndex from 0 to numPlayers-1

        Hand hand1 = new Hand();
        ArrayList<Card> cards1 = new ArrayList<>();
        cards1.add(new Card(Color.BLUE, Value.EIGHT));
        cards1.add(new Card(Color.RED, Value.THREE));
        hand1.setCards(cards1);

        Hand hand2 = new Hand();
        ArrayList<Card> cards2 = new ArrayList<>();
        cards2.add(new Card(Color.BLUE, Value.SEVEN));
        cards2.add(new Card(Color.YELLOW, Value.ELEVEN));
        hand2.setCards(cards2);

        Hand hand3 = new Hand();
        ArrayList<Card> cards3 = new ArrayList<>();
        cards3.add(new Card(Color.BLUE, Value.TEN));
        cards3.add(new Card(Color.RED, Value.SIX));
        cards3.add(new Card(Color.YELLOW, Value.ONE));
        hand3.setCards(cards3);

        Mockito.when(game.getPlayerHands()).thenReturn(new Hand[]{hand1, hand2,hand3});
        Mockito.when(game.getActiveColor()).thenReturn(Color.YELLOW); // Active color

        CheatDetector cheatDetector = new CheatDetectorImpl(game);
        cheatDetector.update(new Card(Color.RED, Value.FIVE));
        boolean ischeating = cheatDetector.check("Player-2");
        Assert.assertTrue(ischeating);
    }

    @Test
    public void Players2and3CheatingTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player-1", 1));
        players.add(new Player("Player-2", 2));
        players.add(new Player("Player-3", 3));

        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getPlayers()).thenReturn(players);

        Hand hand1 = new Hand();
        ArrayList<Card> cards1 = new ArrayList<>();
        cards1.add(new Card(Color.BLUE, Value.EIGHT));
        cards1.add(new Card(Color.RED, Value.THREE));
        hand1.setCards(cards1);

        Hand hand2 = new Hand();
        ArrayList<Card> cards2 = new ArrayList<>();
        cards2.add(new Card(Color.BLUE, Value.SEVEN));
        cards2.add(new Card(Color.YELLOW, Value.ELEVEN));
        hand2.setCards(cards2);

        Hand hand3 = new Hand();
        ArrayList<Card> cards3 = new ArrayList<>();
        cards3.add(new Card(Color.RED, Value.SIX));
        cards3.add(new Card(Color.YELLOW, Value.ONE));
        hand3.setCards(cards3);

        Mockito.when(game.getPlayerHands()).thenReturn(new Hand[]{hand1, hand2,hand3});
        Mockito.when(game.getActiveColor()).thenReturn(Color.YELLOW); // Active color

        Mockito.when(game.getActivePlayerIndex()).thenReturn(1,1, 2); // Player2 plays card and then Player3. getActivePlayerIndex() gets called in new CheatDetector() and in update()
        CheatDetector cheatDetector = new CheatDetectorImpl(game);

        cheatDetector.update(new Card(Color.RED, Value.FIVE));
        Assert.assertTrue(cheatDetector.check("Player-2"));

        cheatDetector.update(new Card(Color.BLUE, Value.TEN));
        Assert.assertTrue(cheatDetector.check("Player-2")); // If Player3 is active and queries cheating-check of Player2
        Assert.assertTrue(cheatDetector.check("Player-3"));
        Assert.assertFalse(cheatDetector.check("Player-1")); // Default should be false
    }

    @Test
    public void NobodyCheatsTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player-1", 1));
        players.add(new Player("Player-2", 2));
        players.add(new Player("Player-3", 3));

        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getPlayers()).thenReturn(players);

        Hand hand1 = new Hand();
        ArrayList<Card> cards1 = new ArrayList<>();
        cards1.add(new Card(Color.BLUE, Value.EIGHT));
        cards1.add(new Card(Color.RED, Value.THREE));
        hand1.setCards(cards1);

        Hand hand2 = new Hand();
        ArrayList<Card> cards2 = new ArrayList<>();
        cards2.add(new Card(Color.BLUE, Value.SEVEN));
        cards2.add(new Card(Color.YELLOW, Value.ELEVEN));
        hand2.setCards(cards2);

        Hand hand3 = new Hand();
        ArrayList<Card> cards3 = new ArrayList<>();
        cards3.add(new Card(Color.RED, Value.SIX));
        cards3.add(new Card(Color.YELLOW, Value.ONE));
        hand3.setCards(cards3);

        Mockito.when(game.getPlayerHands()).thenReturn(new Hand[]{hand1, hand2,hand3});
        Mockito.when(game.getActiveColor()).thenReturn(Color.GREEN); // Active color

        Mockito.when(game.getActivePlayerIndex()).thenReturn(1,1, 2); // Player2 plays card and then Player3. getActivePlayerIndex() gets called in new CheatDetector() and in update()
        CheatDetector cheatDetector = new CheatDetectorImpl(game);

        cheatDetector.update(new Card(Color.RED, Value.FIVE));
        Assert.assertFalse(cheatDetector.check("Player-2"));

        cheatDetector.update(new Card(Color.BLUE, Value.TEN));
        Assert.assertFalse(cheatDetector.check("Player-2")); // If Player3 is active and queries cheating-check of Player2
        Assert.assertFalse(cheatDetector.check("Player-3"));
        Assert.assertFalse(cheatDetector.check("Player-1")); // Default should be false
    }

    @Test
    public void ResetTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player-1", 1));
        players.add(new Player("Player-2", 2));
        players.add(new Player("Player-3", 3));

        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getPlayers()).thenReturn(players);

        Hand hand1 = new Hand();
        ArrayList<Card> cards1 = new ArrayList<>();
        cards1.add(new Card(Color.BLUE, Value.EIGHT));
        cards1.add(new Card(Color.RED, Value.THREE));
        hand1.setCards(cards1);

        Hand hand2 = new Hand();
        ArrayList<Card> cards2 = new ArrayList<>();
        cards2.add(new Card(Color.BLUE, Value.SEVEN));
        cards2.add(new Card(Color.YELLOW, Value.ELEVEN));
        hand2.setCards(cards2);

        Hand hand3 = new Hand();
        ArrayList<Card> cards3 = new ArrayList<>();
        cards3.add(new Card(Color.RED, Value.SIX));
        cards3.add(new Card(Color.YELLOW, Value.ONE));
        hand3.setCards(cards3);

        Mockito.when(game.getPlayerHands()).thenReturn(new Hand[]{hand1, hand2,hand3});
        Mockito.when(game.getActiveColor()).thenReturn(Color.YELLOW); // Active color

        Mockito.when(game.getActivePlayerIndex()).thenReturn(1,1, 2); // Player2 plays card and then Player3. getActivePlayerIndex() gets called in new CheatDetector() and in update()
        CheatDetector cheatDetector = new CheatDetectorImpl(game);

        cheatDetector.update(new Card(Color.RED, Value.FIVE));
        Assert.assertTrue(cheatDetector.check("Player-2"));

        cheatDetector.update(new Card(Color.BLUE, Value.TEN));
        Assert.assertTrue(cheatDetector.check("Player-2")); // If Player3 is active and queries cheating-check of Player2
        Assert.assertTrue(cheatDetector.check("Player-3"));

        cheatDetector.reset();
        Assert.assertFalse(cheatDetector.check("Player-2"));
        Assert.assertFalse(cheatDetector.check("Player-3"));

    }
}
