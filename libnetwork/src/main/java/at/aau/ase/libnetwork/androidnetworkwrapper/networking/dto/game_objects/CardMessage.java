package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;

public class CardMessage extends BaseMessage {
    private Card card;

    public CardMessage() {}

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
        String cardName;
        if (card != null) {
            cardName = card.getColor().getColorName() + " " + card.getValue().getValueName();
        } else {
            cardName = "Card was empty!";
        }
        return cardName;
    }




}
