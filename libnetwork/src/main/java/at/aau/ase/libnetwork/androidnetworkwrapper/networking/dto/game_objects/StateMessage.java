package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

public class StateMessage extends BaseMessage {
    List<Player> players;
    Hand table;         // for the cards within 1 trickround
    Notepad scores;
    int roundsLeft;
    Hand[] playerHands;
    Card trump;
    public int dealer;
    public int activePlayer;
    boolean gameFinished;

    //public StateMessage() {}

    public StateMessage(List<Player> players, Hand table, Notepad scores, int roundsLeft, Hand[] playerHands,
                        Card trump, int dealer, int activePlayer, boolean gameFinished) {
        this.players = players;
        this.table = table;
        this.scores = scores;
        this.roundsLeft = roundsLeft;
        this.playerHands = playerHands;
        this.trump = trump;
        this.dealer = dealer;
        this.activePlayer = activePlayer;
        this.gameFinished = gameFinished;

    }

    //without player Hands
    public StateMessage(List<Player> players, Hand table, Notepad scores, int roundsLeft,
                        Card trump, int dealer, int activePlayer, boolean gameFinished) {
        this.players = players;
        this.table = table;
        this.scores = scores;
        this.roundsLeft = roundsLeft;
        this.trump = trump;
        this.dealer = dealer;
        this.activePlayer = activePlayer;
        this.gameFinished = gameFinished;
    }
}
