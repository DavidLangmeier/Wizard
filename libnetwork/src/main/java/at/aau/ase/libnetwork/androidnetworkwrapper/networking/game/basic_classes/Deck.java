package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends Hand {

    public Deck() {
        for (Color cardColor : Color.values()) {
            for (Value cardValue : Value.values()) {
                //adding 4 Jesters and 4 Wizards
                if (((cardColor == Color.JESTER) && (cardValue == Value.JESTER)) || ((cardColor == Color.WIZARD) && (cardValue == Value.WIZARD))) {
                    Card card = new Card(cardColor, cardValue);
                    this.add(card);
                    this.add(card);
                    this.add(card);
                    this.add(card);
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
        ArrayList<Card> cards = this.getCards();
        Collections.shuffle(cards);
        this.setCards(cards);
    }

    public void remove(Card card) {
        this.remove(card);
    }
}
