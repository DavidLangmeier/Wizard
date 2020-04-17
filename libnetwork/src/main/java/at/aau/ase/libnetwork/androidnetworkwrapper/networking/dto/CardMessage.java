package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;

public class CardMessage extends BaseMessage {
    private Card card;

    public CardMessage(Card card){
        this.card = card;
    }

    public Card getCard(){
        return card;
    }

    public void setCard(Card card){
        this.card = card;
    }


    @Override
    public String toString(){
        return card.toString();
    }




}
