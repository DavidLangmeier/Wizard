package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;

public class Game {
    Player[] players;
    Deck deck;
    Hand table;         // for the cards within 1 trickround
    Notepad scores;
    int rounds;
    Hand[] playerHands;
    Card trump;
    int dealer;
    int activePlayer;

    public Game(Player[] players) {
        this.players = players;
        this.deck = new Deck();
        this.deck.shuffle();
        this.table = new Hand();
        this.scores = new Notepad((short) players.length);
        this.rounds = players.length;
        this.playerHands = new Hand[players.length];
        this.trump = null;
        this.dealer = -1;
        this.activePlayer = -1;
    }


}
