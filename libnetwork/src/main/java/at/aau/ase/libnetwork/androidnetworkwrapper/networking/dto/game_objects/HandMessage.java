package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;

public class HandMessage extends BaseMessage {
    private Hand hand;
    boolean clearBetTricks;

    public HandMessage() {}

    public HandMessage(Hand hand, boolean clearBetTricks){
        this.hand = hand;
        this.clearBetTricks = clearBetTricks;
    }

    public Hand getHand(){
        return hand;
    }

    public void setHand(Hand hand){
        this.hand = hand;
    }

    public List<Card> getCardsInHand(){
        return hand.getCards();
    }

    public boolean getClearBetTricks(){
        return this.clearBetTricks;
    }

    @Override
    public String toString(){
        return hand.showCardsInHand();
    }



}
