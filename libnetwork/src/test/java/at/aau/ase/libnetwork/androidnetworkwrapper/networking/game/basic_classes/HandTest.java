package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;

import org.junit.Test;

import java.util.ArrayList;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value;

import static org.junit.Assert.assertEquals;

public class HandTest {

    private Hand hand = new Hand();
    private Deck testDeck = new Deck();
    private Card card = new Card(Color.BLUE, Value.EIGHT);

    @Test
    public void testForAddToHand() {
        ArrayList<Card> playerCardsTest = new ArrayList<>();
        playerCardsTest.add(testDeck.getCards().get(0));
        hand.add(testDeck.getCards().get(0));
        assertEquals(playerCardsTest, hand.getCards());
    }

    @Test
    public void testForClearHand() {
        hand.add(testDeck.getCards().get(0));
        assertEquals(1, hand.getCards().size());
        hand.clear();
        assertEquals(0, hand.getCards().size());
    }

    @Test
    public void testForShowCardsInHand() {

        hand.add(testDeck.getCards().get(0));
        hand.add(testDeck.getCards().get(1));
        hand.add(testDeck.getCards().get(2));
        hand.add(testDeck.getCards().get(3));
        hand.add(testDeck.getCards().get(4));

        assertEquals(5, hand.getCards().size());
        assertEquals(("Jester Jester\n" + "Jester Jester\n" + "Jester Jester\n" + "Jester Jester\n" + "Blue One\n"), hand.showCardsInHand());
    }

    @Test
    public void testForDealCard() {
        Hand gameDesk = new Hand();
        hand.add(testDeck.getCards().get(0));
        assertEquals(1, hand.getCards().size());
        assertEquals(0, gameDesk.getCards().size());
        assertEquals(true, hand.dealCard(testDeck.getCards().get(0), gameDesk));
        hand.dealCard(testDeck.getCards().get(0), gameDesk);

        assertEquals(0, hand.getCards().size());
        assertEquals(1, gameDesk.getCards().size());
        assertEquals(false, hand.dealCard(testDeck.getCards().get(0), gameDesk));

    }

    @Test
    public void testForPlayerDoesNotHaveCard() {
        Hand gameDesk = new Hand();
        hand.dealCard((new Card(Color.WIZARD, Value.WIZARD)), gameDesk);

        assertEquals(0, hand.getCards().size());
        assertEquals(0, gameDesk.getCards().size());
        assertEquals(false, hand.dealCard((new Card(Color.WIZARD, Value.WIZARD)), gameDesk));

    }
    @Test
    public void testCardPicture(){
        Card card1 = new Card(Color.BLUE, Value.EIGHT);

       String test= card1.getPictureFileId();
        System.out.println(test);

    }
}
