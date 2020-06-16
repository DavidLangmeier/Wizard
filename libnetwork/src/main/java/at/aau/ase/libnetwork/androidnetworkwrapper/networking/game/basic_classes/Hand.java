package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Hand {
    private List<Card> playerCards;

    public Hand() {
        this.playerCards = new ArrayList<>();
    }

    public void clear() {
        this.playerCards.clear();
    }

    public void add(Card card) {
        this.playerCards.add(card);
    }

    public String showCardsInHand() {
        StringBuilder s = new StringBuilder();
        for (Card c : playerCards) {
            s.append(c.toString() + "\n");
        }
        return s.toString();
    }

    public boolean dealCard(Card card, Hand otherHand) {
        boolean cardFound = false;
        int cardID = card.getCardId();

        for (Iterator<Card> iterator = playerCards.iterator(); iterator.hasNext();) {
            Card currentCard = iterator.next();
            if (currentCard.getCardId() == cardID) {
                iterator.remove();
                otherHand.add(card);
                cardFound = true;
            }
        }
        return cardFound;
    }

    public List<Card> getCards() {
        return playerCards;
    }

    public void setCards(List<Card> cards) {
        this.playerCards = cards;
    }
}
