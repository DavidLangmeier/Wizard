package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;

import java.util.Collections;
import java.util.List;

public class Deck extends Hand {

    public Deck() {
        for (Color cardColor : Color.values()) {
            for (Value cardValue : Value.values()) {
                //adding 4 Jesters and 4 Wizards
                if (((cardColor == Color.JESTER) && (cardValue == Value.JESTER)) || ((cardColor == Color.WIZARD) && (cardValue == Value.WIZARD))) {
                    Card card1 = new Card(cardColor, cardValue);
                    Card card2 = new Card(cardColor, cardValue);
                    Card card3 = new Card(cardColor, cardValue);
                    Card card4 = new Card(cardColor, cardValue);

                    this.add(card1);
                    this.add(card2);
                    this.add(card3);
                    this.add(card4);
                }
                //adding cards 1 to 13 of each color
                else if (((cardColor != Color.JESTER) && (cardValue != Value.JESTER)) && ((cardColor != Color.WIZARD) && (cardValue != Value.WIZARD))) {
                    Card card = new Card(cardColor, cardValue);
                    this.add(card);
                }
            }
        }
    }

    public void shuffle() {
        List<Card> cards = this.getCards();
        Collections.shuffle(cards);
        this.setCards(cards);
    }
}
