package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;

public class DeckMessage extends BaseMessage{
    private Deck deck;

    public DeckMessage() {}

    public DeckMessage(Deck deck){
        this.deck = deck;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
}
