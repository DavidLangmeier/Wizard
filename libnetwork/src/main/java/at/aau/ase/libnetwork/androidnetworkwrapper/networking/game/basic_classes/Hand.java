package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> playerCards;


    public Hand() {
        this.playerCards = new ArrayList<Card>();
    }

    public void clear(){
        this.playerCards.clear();
    }

    public void add(Card card){
        this.playerCards.add(card);
    }

    public String showCardsInHand(){
        String s = "";
        for (Card c : playerCards) {
            s += c.toString() + "\n";
        }
        return s;
    }

    public boolean dealCard(Card card, Hand otherHand){
        if(!playerCards.contains(card))
            return false;
        else {
            playerCards.remove(card);
            otherHand.add(card);
            return true;
        }
    }

    public ArrayList<Card> getCards() {
        return playerCards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.playerCards = cards;
    }
}
