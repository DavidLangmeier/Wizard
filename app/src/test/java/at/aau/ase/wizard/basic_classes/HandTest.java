package at.aau.ase.wizard.basic_classes;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class HandTest {

    private Hand hand = new Hand();
    private Card card = new Card(Color.BLUE, Value.EIGHT);

    @Test
    public void testForAddToHand() {
        ArrayList<Card> playerCardsTest = new ArrayList<>();
        playerCardsTest.add(card);
        hand.add(card);
        assertEquals(playerCardsTest, hand.getCards());
    }

    @Test
    public void testForClearHand() {
        hand.add(card);
        assertEquals(1, hand.getCards().size());
        hand.clear();
        assertEquals(0, hand.getCards().size());
    }

    @Test
    public void testForShowCardsInHand() {
        Card card1 = new Card(Color.GREEN, Value.TWELVE);
        Card card2 = new Card(Color.BLUE, Value.ONE);
        Card card3 = new Card(Color.YELLOW, Value.THREE);
        Card card4 = new Card(Color.RED, Value.ELEVEN);

        hand.add(card);
        hand.add(card1);
        hand.add(card2);
        hand.add(card3);
        hand.add(card4);

        assertEquals(5, hand.getCards().size());
        assertEquals(("Blue Eight\n" + "Green Twelve\n" + "Blue One\n" + "Yellow Three\n" + "Red Eleven\n"), hand.showCardsInHand());
    }

    @Test
    public void testForDealCard() {
        Hand gameDesk = new Hand();
        hand.add(card);
        assertEquals(1, hand.getCards().size());
        assertEquals(0, gameDesk.getCards().size());
        assertEquals(true, hand.dealCard(card, gameDesk));
        hand.dealCard(card, gameDesk);

        assertEquals(0, hand.getCards().size());
        assertEquals(1, gameDesk.getCards().size());
        assertEquals(false, hand.dealCard(card, gameDesk));

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
