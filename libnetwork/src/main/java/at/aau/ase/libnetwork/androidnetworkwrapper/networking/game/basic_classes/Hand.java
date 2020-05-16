package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> playerCards;

    public Hand() {
        this.playerCards = new ArrayList<Card>();
    }

    public void clear() {
        this.playerCards.clear();
    }

    public void add(Card card) {
        this.playerCards.add(card);
    }

    public String showCardsInHand() {
        String s = "";
        for (Card c : playerCards) {
            s += c.toString() + "\n";
        }
        return s;
    }

    public boolean dealCard(Card card, Hand otherHand) {
        boolean cardFound = false;
        int cardID = card.getCard_id();

        int indexOfCardToRemove;
        for (int i = 0; i < playerCards.size(); i++) {
            if (playerCards.get(i).getCard_id() == cardID) {
                indexOfCardToRemove = i;
                //removing playerCard by Index
                playerCards.remove(indexOfCardToRemove);
                otherHand.add(card);
                cardFound = true;
            }
        }
        return cardFound;
    }

    public ArrayList<Card> getCards() {
        return playerCards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.playerCards = cards;
    }
}
