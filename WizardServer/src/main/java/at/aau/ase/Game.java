package at.aau.ase;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.START;

public class Game {
    private List<Player> players;
    private Deck deck;
    private Hand table;         // for the cards within 1 trickround
    private Notepad scores;
    private int totalRounds;
    private int currentRound;
    private Hand[] playerHands; // if indexed with "activePlayer" put -1
    private Card trump;
    private int dealer;
    private int activePlayer;   // refers to connectionID of player
    private WizardServer server;
    private int trickRoundTurn;


    public Game(WizardServer server, List<Player> players) {
        this.server = server;
        this.players = players;
        this.deck = new Deck();
        this.table = new Hand();
        this.scores = new Notepad((short) players.size());
        this.totalRounds = 60 / players.size();
        this.currentRound = 1;
        this.trickRoundTurn = 0;
        this.playerHands = new Hand[players.size()];
        for (int i = 0; i < players.size(); i++) {
            this.playerHands[i] = new Hand();
        }
        this.trump = null;
        this.dealer = players.get(0).getConnectionID();
        //this.activePlayer = players.get(0).getConnectionID();
        this.activePlayer = -1;
        //incrementDealerAndActivePlayer();
    }

    public void startGame() {
        System.out.println("GAME: New Game started! Showing connected players:");
        printPlayers();

        // Send START to all users -> @client: trigger intent which starts gameActivity
        System.out.println("GAME: Broadcasting START now.");
        server.broadcastMessage(new ActionMessage(START));
    }

    public void broadcastGameState() {
        System.out.println("GAME: Broadcasting gameState");
        server.broadcastMessage(new StateMessage(table, scores, trump, totalRounds, dealer, activePlayer));
        System.out.println("GAME: DEALER = " + dealer + " ActivePlayer = " + activePlayer);
    }

    public void dealCards() {
        System.out.println("GAME: Dealing Cards.");
        deck.shuffle();

        // deal cards to playerHands serverside | round=7 hardcoded, has to be changed later
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < players.size(); j++) {
                System.out.println("GAME: Dealing to hand #" + j + " with players.size of " + players.size());
                System.out.println("GAME: current card = " + deck.getCards().get(0).toString());
                deck.dealCard(deck.getCards().get(0), playerHands[j]);
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

        // set trump card
        if (currentRound != 20) {
            trump = deck.getCards().get(0);
            System.out.println("GAME: Current TRUMP = " + trump.toString());
            //deck.remove(trump);
        }
        activePlayer = players.get(0).getConnectionID();
        broadcastGameState();
    }

    public void printPlayers() {
        for (int i = 0; i < players.size(); i++) {
            System.out.println("GAME: Player " + i + ", name=" + players.get(i).getName()
                    + ", connectionID=" + players.get(i).getConnectionID());
        }

    }

    public void dealOnePlayerCardToTable(Card cardToPutOnTable) {
        System.out.println("GAME: Card recieved: " + cardToPutOnTable.toString() + " Trying to put on Table...");
        //System.out.println("GAME: Card object id: " + cardToPutOnTable.hashCode());
        System.out.println("GAME: Dealer: " + dealer);
        System.out.println("GAME: Size of PlayerHands: " + playerHands.length);

        cardToPutOnTable.setPlayedBy(activePlayer - 1);
        playerHands[activePlayer - 1].dealCard(cardToPutOnTable, table);
        for (int i = 0; i < table.getCards().size(); i++) {
            System.out.println(table.getCards().get(i) + " is now on Table!");
        }
        //server.sentTo(players.get(activePlayer-1).getConnectionID(), new HandMessage(playerHands[activePlayer-1]));
        server.sentTo(activePlayer, new HandMessage(playerHands[activePlayer - 1]));

        checkCurrentTrickRound();
        updateDealerAndActivePlayer();
        broadcastGameState();
    }

    public Hand getTable() {
        return table;
    }

    // better not refer directly to current round, as it is not sure that the connectionIDs go like 1,2,3...
    public void updateDealerAndActivePlayer() {
        this.dealer = players.get((currentRound - 1) % (players.size())).getConnectionID();
        this.activePlayer = players.get(trickRoundTurn).getConnectionID();
        System.out.println("GAME: DEALER = " + dealer + " ActivePlayer = " + activePlayer);
    }

    public void checkCurrentTrickRound() {
        if (trickRoundTurn < players.size() - 1) {
            trickRoundTurn++;
            updateDealerAndActivePlayer();
            broadcastGameState();
            System.out.println("GAME: Current trick still incomplete. TrickRoundTurn=" + trickRoundTurn);
        } else {
            activePlayer = -1; // deactivate all players while showing full trick on table
            broadcastGameState(); // send to show full trick on table
            checkTrickWinner();

            trickRoundTurn = 0;
            table.clear();
            updateDealerAndActivePlayer();
            System.out.println("GAME: Trick complete. Table cleared - new trickRound starting.");

            // wait some time before sending cleared table
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            broadcastGameState();
        }
    }

    public void checkTrickWinner() {
        Card highestCard = table.getCards().get(0);

        for (Card card : table.getCards()) {

            // First Wizard on the table wins the trick
            if (card.getValue().getValueCode() == 14) {
                highestCard = card;
                break;
            }

            // if current highest card and compared card have same color (trump or not trump)
            else if ((card.getColor().getColorCode() == highestCard.getColor().getColorCode()) &&
                    (card.getValue().getValueCode() > highestCard.getValue().getValueCode())) {
                highestCard = card;
            }

            // if current highest card has not trump color but compared card has trump color
            else if ((card.getColor().getColorCode() == trump.getColor().getColorCode())) {
                highestCard = card;
            }

            // if current highest card is Jester and compared card is not Jester
            else if ((highestCard.getValue().getValueCode() == 0) &&
                    (card.getValue().getValueCode() != 0)) {
                highestCard = card;
            }
        }
        String trickWinner = "" + highestCard.toString() + " played by " + players.get(highestCard.getPlayedBy()).getName()
                + " has won the trick";
        System.out.println("GAME: " +trickWinner);
    }
}
