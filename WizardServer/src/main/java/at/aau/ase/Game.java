package at.aau.ase;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.START;

public class Game {
    private boolean gamerunning = false;
    private List<Player> players;
    private Deck deck;
    private Hand table;         // for the cards within 1 trickround
    private Notepad scores;
    private int totalRounds;
    private int currentRound;
    private Hand[] playerHands;
    private Card trump;
    private int dealer;
    private int activePlayer;
    private WizardServer server;


    public Game(WizardServer server, List<Player> players) {
        this.server = server;
        this.players = players;
        this.deck = new Deck();
        this.table = new Hand();
        this.scores = new Notepad((short) players.size());
        this.totalRounds = 60 / players.size();
        this.currentRound = 1;
        this.playerHands = new Hand[players.size()];
        for (int i = 0; i < players.size(); i++) {
            this.playerHands[i] = new Hand();
        }
        this.trump = null;
        this.dealer = 1;
        this.activePlayer = 1;
    }

    public void startGame() {
        System.out.println("GAME: New Game started! Showing connected players:");
        gamerunning = true;
        printPlayers();

        // Send START to all users -> @client: trigger intent which starts gameActivity
        System.out.println("GAME: Broadcasting START now.");
        server.broadcastMessage(new ActionMessage(START));
    }

    public void broadcastGameState() {
        System.out.println("GAME: Broadcasting gameState");
        server.broadcastMessage(new StateMessage(table, scores, trump, totalRounds, dealer, activePlayer));
    }

    public void dealCards() {
        System.out.println("GAME: Dealing Cards.");
        deck.shuffle();

        // deal cards to playerHands serverside | round=5 hardcoded, has to be changed later
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < players.size(); j++) {
                System.out.println("GAME: Dealing to hand #" +j +" with players.size of " +players.size());
                //Card currentCard = deck.getCards().get(i);
                System.out.println("GAME: current card = " +deck.getCards().get(i).toString());
                //playerHands[j].add(currentCard);
                //deck.remove(currentCard);
                deck.dealCard(deck.getCards().get(i), playerHands[j]);
            }
        }

        // send every player his hand
        HandMessage currentHandMessage = new HandMessage();
        for (int i = 0; i < players.size(); i++) {
            Hand currentHand = playerHands[i];
            currentHandMessage.setHand(currentHand);
            server.sentTo(players.get(i).getConnectionID(), currentHandMessage);
            System.out.println("GAME: Hand sent for Player " + i);
            System.out.println(currentHand.showCardsInHand());
        }
    }

    public void printPlayers() {
        for (int i = 0; i < players.size(); i++) {
            System.out.println("GAME: Player " + i + ", name=" + players.get(i).getName()
                    + ", connectionID=" + players.get(i).getConnectionID());
        }

    }

    public boolean isGamerunning() {
        return gamerunning;
    }
}
