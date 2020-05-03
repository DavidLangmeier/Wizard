package at.aau.ase;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

public class Game implements Runnable {
    List<Player> players;
    Deck deck;
    Hand table;         // for the cards within 1 trickround
    Notepad scores;
    int rounds;
    Hand[] playerHands;
    Card trump;
    int dealer;
    int activePlayer;

    public Game(List<Player> players) {
        this.players = players;
        this.deck = new Deck();
        this.deck.shuffle();
        this.table = new Hand();
        this.scores = new Notepad((short) players.size());
        this.rounds = 60 / players.size();
        this.playerHands = new Hand[players.size()];
        this.trump = null;
        this.dealer = -1;
        this.activePlayer = -1;
    }

    @Override
    public void run() {
        System.out.println("GAME: New Game started!");
        printPlayers();


    }

    public void printPlayers() {
        for (int i = 0; i < players.size(); i++) {
            System.out.println("Player " + i + ", name=" + players.get(i).getName()
                    + ", connectionID=" + players.get(i).getConnectionID());
        }

    }

}
