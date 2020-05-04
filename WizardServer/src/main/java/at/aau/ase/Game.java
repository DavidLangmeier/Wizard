package at.aau.ase;

import java.util.List;
import java.util.logging.Handler;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.Callback;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.DEAL;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.SHUFFLE;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.START;

public class Game implements Runnable, Callback<BaseMessage> {
    List<Player> players;
    Deck deck;
    Hand table;         // for the cards within 1 trickround
    Notepad scores;
    int rounds;
    Hand[] playerHands;
    Card trump;
    int dealer;
    int activePlayer;
    WizardServer server;
    int trickRound;


    public Game(WizardServer server, List<Player> players) {
        this.server = server;
        this.players = players;
        this.deck = new Deck();
        this.deck.shuffle();
        this.table = new Hand();
        this.scores = new Notepad((short) players.size());
        this.rounds = 60 / players.size();
        this.playerHands = new Hand[players.size()];
        this.trump = null;
        this.dealer = -1;
        this.activePlayer = 1;
        //test for Trickround 3
        this.trickRound = 3;
    }

    @Override
    public void run() {
        System.out.println("GAME: New Game started!");
        printPlayers();

        // Send start to all users -> clients should go to gameActivity now
        System.out.println("GAME: Broadcasting START now.");
        server.broadcastMessage(new ActionMessage(START));

        // setting dealer and active player, later this has to be automated every new round/trickround
        this.dealer = players.get(0).getConnectionID();
        this.activePlayer = players.get(0).getConnectionID();
        System.out.println("GAME: Broadcasting initial gameState");
        server.broadcastMessage(new StateMessage(players, table, scores, rounds, trump, dealer, activePlayer, false));

    }



    public void printPlayers() {
        for (int i = 0; i < players.size(); i++) {
            System.out.println("Player " + i + ", name=" + players.get(i).getName()
                    + ", connectionID=" + players.get(i).getConnectionID());
        }

    }

    @Override
    public void callback(BaseMessage baseMessage) {
    if((baseMessage instanceof ActionMessage) && ((ActionMessage) baseMessage).getActionType() == DEAL) {
        System.out.println("GAME: dealMessage recieved!");
        dealCards(4, deck, playerHands);
        for (int i = 0; i < players.size() ; i++) {
            Hand currentHand = playerHands[i];
            HandMessage currentHandMessage = new HandMessage(currentHand);
            server.sentTo(players.get(i).getConnectionID(), currentHandMessage);
            System.out.println("Hand sent for Player " + i);
        }
    }
    }

    void dealCards(int round, Deck deck, Hand[] playerHands){
        System.out.println("GAME: dealing Cards now!");
        deck.shuffle();
        for (int i = 0; i < round; i++) {
            for (int j = 0; j < players.size(); j++) {
                deck.dealCard(deck.getCards().get(i), playerHands[j]);
            }
        }
    }
}
