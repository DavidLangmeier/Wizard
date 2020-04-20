package at.aau.ase.wizard.basic_classes;

import org.junit.Test;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;

import static org.junit.Assert.*;

public class DeckTest {

    Deck deck = new Deck();

    @Test
    public void testForCreatingNewDeck() {
        assertEquals(60, deck.getCards().size());

        //display ordered Deck in Console
        System.out.println(deck.showCardsInHand());
    }

    @Test
    public void testForClearDeck() {
        deck.clear();
        assertEquals(0, deck.getCards().size());
    }

    @Test
    public void testForShuffleDeck() {
        Deck testDeck = new Deck();
        assertEquals(deck.getCards().toString(), testDeck.getCards().toString());
        deck.shuffle();
        assertNotEquals(deck.getCards().toString(), testDeck.getCards().toString());
    }


}
