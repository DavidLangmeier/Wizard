package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import java.util.ArrayList;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;

public class HandMessage extends BaseMessage {
    private Hand hand;

    public HandMessage() {}

    public HandMessage(Hand hand){
        this.hand = hand;
    }

    public Hand getHand(){
        return hand;
    }

    public void setHand(Hand hand){
        this.hand = hand;
    }

    public ArrayList <Card> getCardsInHand(){
        return hand.getCards();
    }

    @Override
    public String toString(){
        return hand.showCardsInHand();
    }



}
