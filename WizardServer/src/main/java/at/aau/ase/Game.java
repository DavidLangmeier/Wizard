package at.aau.ase;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.NotePadMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.TextMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.WizardConstants;

import static com.esotericsoftware.minlog.Log.*;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.END;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.START;

public class Game {
    private boolean gamerunning = false;
    private List<Player> players;
    private Deck deck;
    private Hand table;         // for the cards within 1 trickround
    private Notepad scores;
    private int totalRounds;
    private int currentRound;
    private Hand[] playerHands; // if indexed with "activePlayer" put -1
    private Color trump;
    private int dealer;
    private int activePlayerID;   // refers to connectionID of player
    private int activePlayerIndex; // refers to activePlayers index in list "players"
    private WizardServer server;
    private int trickRoundTurn;
    private int betTricksCounter;
    private boolean clearBetTricks;
    private CheatDetector cheatDetector;


    Game(WizardServer server, List<Player> players) {
        this.server = server;
        this.players = players;
        this.deck = new Deck();
        this.table = new Hand();
        this.scores = new Notepad((short) players.size());
        this.totalRounds = 60 / players.size();
        this.currentRound = 1;
        this.trickRoundTurn = 0;
        this.betTricksCounter = 0;
        this.playerHands = new Hand[players.size()];
        for (int i = 0; i < players.size(); i++) {
            this.playerHands[i] = new Hand();
        }
        this.trump = null;
        this.dealer = players.get((currentRound % players.size())).getConnectionID();
        this.activePlayerID = -1;
        this.clearBetTricks = false;
    }

    void startGame() {
        info("GAME: New Game started! Showing connected players:");
        gamerunning = true;
        printPlayers();

        // Send START to all users -> @client: trigger intent which starts gameActivity
        info("GAME: Broadcasting START now.");
        server.broadcastMessage(new ActionMessage(START));
        server.broadcastMessage(new TextMessage("Shuffling and dealing cards..."));
        waitSafe(WizardConstants.TIME_TO_WAIT_SHORT);
        dealCards();
        this.cheatDetector = new CheatDetectorImpl(this);
    }

    void broadcastGameState() {
        info("GAME: Broadcasting gameState");
        server.broadcastMessage(new StateMessage(table, scores, trump, totalRounds, dealer, activePlayerID, betTricksCounter, clearBetTricks));
        info("GAME: DEALER = " + dealer + " ActivePlayer = " + activePlayerID);
    }

    void dealCards() {
        info("GAME: Dealing Cards.");
        deck = new Deck();
        deck.shuffle();

        // deal cards to playerHands serverside | round=5 hardcoded, has to be changed later
        for (int i = 0; i < currentRound; i++) {
            for (int j = 0; j < players.size(); j++) {
                info("GAME: Dealing to hand #" + j + " with players.size of " + players.size());
                info("GAME: current card = " + deck.getCards().get(0).toString());
                deck.dealCard(deck.getCards().get(0), playerHands[j]);
            }
        }

        // send every player his hand
        HandMessage currentHandMessage = new HandMessage();
        for (int i = 0; i < players.size(); i++) {
            Hand currentHand = playerHands[i];
            currentHandMessage.setHand(currentHand);
            server.sentTo(players.get(i).getConnectionID(), currentHandMessage);
            info("GAME: Hand sent for Player " + i);
            info(currentHand.showCardsInHand());
        }

        trump = setTrump();
        activePlayerIndex = (currentRound + 1) % players.size();
        activePlayerID = players.get(activePlayerIndex).getConnectionID();
        broadcastGameState();
    }

    Color setTrump() {
        Color trumpColor;

        // set trump color, rounds 1-19 have a trump, 20 has no trump
        if (currentRound != 20) {
            trumpColor = deck.getCards().get(0).getColor();
            info("GAME: Current TRUMP = " + trumpColor.getColorName());
        } else {
            trumpColor = Color.JESTER;
        }

        // if trump is wizard or jester a random color is set
        if (trumpColor == Color.WIZARD || trumpColor == Color.JESTER) {
            info("GAME: Current TRUMP = " + trumpColor.getColorName() + "is not valid! Random color generating...");
            int randomNr = new SecureRandom().nextInt(3);

            switch (randomNr) {
                case 0:
                    trumpColor = Color.YELLOW;
                    break;

                case 1:
                    trumpColor = Color.RED;
                    break;

                case 2:
                    trumpColor = Color.GREEN;
                    break;

                case 3:
                    trumpColor = Color.BLUE;
                    break;

                default:
                    info("ERROR! Could not generate random trump color!");
                    break;
            }
        }

        if (currentRound < 20) {
            server.broadcastMessage(new TextMessage("Current Trump is " + trumpColor.getColorName()));
        } else {
            server.broadcastMessage(new TextMessage("LAST ROUND HAS NO TRUMP!"));
        }

        return trumpColor;
    }

    void printPlayers() {
        for (int i = 0; i < players.size(); i++) {
            info("GAME: Player " + i + ", name=" + players.get(i).getName()
                    + ", connectionID=" + players.get(i).getConnectionID());
        }

    }

    void dealOnePlayerCardToTable(Card cardToPutOnTable) {
        info("GAME: Card recieved: " + cardToPutOnTable.toString() + " Trying to put on Table...");
        info("GAME: Dealer: " + dealer);
        info("GAME: Size of PlayerHands: " + playerHands.length);

        cardToPutOnTable.setPlayedBy(activePlayerIndex);
        playerHands[activePlayerIndex].dealCard(cardToPutOnTable, table);
        for (int i = 0; i < table.getCards().size(); i++) {
            info(table.getCards().get(i) + " is now on Table!");
        }
        server.sentTo(activePlayerID, new HandMessage(playerHands[activePlayerIndex], clearBetTricks));

        checkCurrentTrickRound();
        updateDealerAndActivePlayer();
        broadcastGameState();
        cheatDetector.update(cardToPutOnTable);
    }

    public Hand getTable() {
        return table;
    }

    // better not refer directly to current round, as it is not sure that the connectionIDs go like 1,2,3...
    void updateDealerAndActivePlayer() {
        this.dealer = players.get((currentRound - 1) % (players.size())).getConnectionID();
        this.activePlayerID = players.get(activePlayerIndex).getConnectionID();
        info("GAME: DEALER = " + dealer + " ActivePlayer = " + activePlayerID);
    }

    // checks if a trickRound is over and handles the remaining rounds of the game
    void checkCurrentTrickRound() {

        // trick is still incomplete
        if (trickRoundTurn < players.size() - 1) {
            trickRoundTurn++;
            activePlayerIndex = (activePlayerIndex + 1) % players.size();
            updateDealerAndActivePlayer();

            broadcastGameState();
            info("GAME: Current trick still incomplete. TrickRoundTurn=" + trickRoundTurn);

            // trick is complete, current round is still incomplete
        } else if ((trickRoundTurn == players.size() - 1) && (!playerHands[activePlayerIndex].getCards().isEmpty())) {
            activePlayerID = -1; // deactivate all players while showing full trick on table
            broadcastGameState(); // send to show full trick on table
            activePlayerIndex = checkTrickWinner();
            writeTookTricksToNotePad();
            trickRoundTurn = 0;
            table.clear();
            updateDealerAndActivePlayer();
            info("GAME: Trick complete. Table cleared - new trickRound starting.");

            // wait some time before sending cleared table
            waitSafe(WizardConstants.TIME_TO_WAIT_SHORT);
            broadcastGameState();

            // trick is complete, current round is over
        } else if ((trickRoundTurn == players.size() - 1) && (playerHands[activePlayerIndex].getCards().isEmpty())) {
            activePlayerID = -1; // deactivate all players while showing full trick on table
            broadcastGameState(); // send to show full trick on table
            checkTrickWinner();
            trickRoundTurn = 0;
            betTricksCounter = 0;
            writeTookTricksToNotePad();
            calculatePointsPerPlayerPerRound();
            clearBetTricks = true;
            table.clear();

            // wait some time before sending cleared table
            waitSafe(WizardConstants.TIME_TO_WAIT_MEDIUM);

            cheatDetector.reset(); // Cheating can be checked for every round (roundwise), so reset to nobody is cheating at end of round

            broadcastGameState();

            // check if round was the last round of the game
            if (currentRound < 20) {
                currentRound++;
                dealer = players.get((currentRound - 1) % (players.size())).getConnectionID();
                dealCards();
            } else {
                info("GAME: Last round played, Game is complete.");
                server.broadcastMessage(new TextMessage("Last round played, Game is complete."));

                // wait some time before sending Action END
                waitSafe(WizardConstants.TIME_TO_WAIT_LONG);
                // End-Msg should trigger the client going to Endscreen Activity
                server.broadcastMessage(new ActionMessage(END));
            }
        }
    }

    // returns trick-winners index in list "players"
    int checkTrickWinner() {
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
            else if ((currentRound != 20) && (highestCard.getColor() != trump) &&
                    (card.getColor() == trump)) {
                highestCard = card;
            }

            // if current highest card is Jester and compared card is not Jester
            else if ((highestCard.getValue().getValueCode() == 0) &&
                    (card.getValue().getValueCode() != 0)) {
                highestCard = card;
            }
        }
        String trickWinner = highestCard.toString() + " played by " + players.get(highestCard.getPlayedBy()).getName()
                + " has won the last trick";
        info("GAME: " + trickWinner);
        server.broadcastMessage(new TextMessage(trickWinner));
        return highestCard.getPlayedBy();
    }


    void writeBetTricksToNotePad(Notepad scores, int playerID, int betTricks) {
        clearBetTricks = false;
        scores.setBetTricksPerPlayerPerRound(playerID, betTricks, currentRound);
        this.scores = scores;
        info(Arrays.deepToString(scores.getBetTricksPerPlayerPerRound()));
        server.broadcastMessage(new NotePadMessage(this.scores));
        info("GAME: Trickroundturn: " + trickRoundTurn);
        //to check, when it's time to start with trickround
        if (betTricksCounter < players.size()) {
            betTricksCounter++;
            activePlayerIndex = (activePlayerIndex + 1) % players.size();
            activePlayerID = players.get(activePlayerIndex).getConnectionID();
            updateDealerAndActivePlayer();
            broadcastGameState();
        }
    }

    void writeTookTricksToNotePad() {
        scores.setTookTricksPerPlayerPerRound(checkTrickWinner(), currentRound);
    }

    void calculatePointsPerPlayerPerRound() {
        int pointsPerPlayerPerRound;
        for (int i = 0; i < players.size(); i++) {
            if (scores.getBetTricksPerPlayerPerRound()[i][currentRound - 1] == scores.getTookTricksPerPlayerPerRound()[i][currentRound - 1]) {
                int previousPointsPerPlayerPerRound = scores.getPointsPerPlayerPerRound()[players.get(i).getConnectionID()-1][currentRound];
                pointsPerPlayerPerRound = previousPointsPerPlayerPerRound + (scores.getBetTricksPerPlayerPerRound()[i][currentRound - 1]) * WizardConstants.MULTIPLIER_TOOK_TRICKS + WizardConstants.ADDEND_BET_TRICKS_CORRECTLY;
                info("IF Player " + players.get(i).getName() + " with PlayerID: " + i + " made " + pointsPerPlayerPerRound + " points!");
                info(Arrays.deepToString(scores.getPointsPerPlayerPerRound()));
                scores.setPointsPerPlayerPerRound(players.get(i).getConnectionID() - 1, pointsPerPlayerPerRound, currentRound);
            } else {
                int previousPointsPerPlayerPerRound = scores.getPointsPerPlayerPerRound()[players.get(i).getConnectionID()-1][currentRound];
                pointsPerPlayerPerRound = previousPointsPerPlayerPerRound + (-1) * WizardConstants.MULTIPLIER_TOOK_TRICKS * Math.abs((scores.getBetTricksPerPlayerPerRound()[i][currentRound - 1]) - (scores.getTookTricksPerPlayerPerRound()[i][currentRound - 1]));
                info("ELSE Player " + players.get(i).getName() + " with PlayerID: " + i + " made " + pointsPerPlayerPerRound + " points!");
                info(Arrays.deepToString(scores.getPointsPerPlayerPerRound()));
                scores.setPointsPerPlayerPerRound(players.get(i).getConnectionID() - 1, pointsPerPlayerPerRound, currentRound);
            }
        }
        server.broadcastMessage(new NotePadMessage(this.scores));
    }

    boolean isGamerunning() {
        return gamerunning;
    }

    //checks if bet is allowed
    boolean checkBet(int bet) {
        boolean checkBet = true;
        if (bet > currentRound || bet < 0) {
            checkBet = false;
        }
        if(!checkBetTricksCounter()){
            int sum = 0;
            for (int i = 0; i < players.size() - 1; i++) {
                int index = (((currentRound + 1) + i) % players.size());
                sum += scores.getBetTricksPerPlayerPerRound()[index][currentRound - 1];
            }
            if (((bet + sum) == currentRound) && !checkBetTricksCounter()){
                checkBet = false;
            }

        }
        return checkBet;
    }

    //checks if player is last player of this trickround
    boolean checkBetTricksCounter() {
        return betTricksCounter < players.size() - 1;
    }

    void waitSafe(long timeToWait) {
        try {
            Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
            error("GAME: Error while waiting.", e);
            Thread.currentThread().interrupt();
        }
    }

    public Color getActiveColor() {
        List<Card> cardsOnTable = table.getCards();
        if (cardsOnTable != null) {
            Card[] cardsToCheck = cardsOnTable.toArray(new Card[0]);
            Color activeColor = findActiveColor(cardsToCheck);
            info("active color = " + activeColor);
            return activeColor;
        } else {
            return null;
        }
    }

    /**
     * Return active color of the current round /cards on table
     * @param cardsOnTable
     * @return
     * Returns color of first card if first card is not a wizard or jester.
     * Returns null in case first card is wizard, since active color doesn't matter. Wizard already won the round.
     * In case first card in a jester, look at second card. If this is again a jester look for 3rd card. And so on.
     */
    private Color findActiveColor(Card[] cardsOnTable) {
        Color activeColor = null;
        if (cardsOnTable.length != 0) {
            if (cardsOnTable[0].getValue() != Value.JESTER && cardsOnTable[0].getValue() != Value.WIZARD) {
                activeColor = cardsOnTable[0].getColor();
            } else if (cardsOnTable[0].getValue() == Value.WIZARD) {
                activeColor = null;
            } else if (cardsOnTable[0].getValue() == Value.JESTER) {
                activeColor = findActiveColor(Arrays.copyOfRange(cardsOnTable, 1, cardsOnTable.length));
            }
        }
        return activeColor;
    }

    /**
     * Update scoreboard with penalty and bonus for cheater and checker
     * @param isCheating boolean, if suspected player is actually cheating or not
     * @param playerSuspectedOfCheating, String of the playerName suspected to be cheating
     * @param playerChecking, String of the playerName checking if the other one is cheating
     */
    public void updateScoresCheating(boolean isCheating, String playerSuspectedOfCheating, String playerChecking) {
        int playerCheatingIdx = matchPlayerNameToPlayerIdx(playerSuspectedOfCheating);
        int playerCheckingIdx = matchPlayerNameToPlayerIdx(playerChecking);
        int[][] pointsPerPlayerPerRound = scores.getPointsPerPlayerPerRound();
        if (isCheating) { // Punish cheater and bonus for checker
            int newscoreCheater = pointsPerPlayerPerRound[playerCheatingIdx][currentRound] - WizardConstants.CHEAT_PENALTY;
            int newscoreChecker = pointsPerPlayerPerRound[playerCheckingIdx][currentRound] + WizardConstants.CHEAT_DETECTION_BONUS;
            scores.setPointsPerPlayerPerRound(playerCheatingIdx, newscoreCheater, currentRound);
            scores.setPointsPerPlayerPerRound(playerCheckingIdx, newscoreChecker, currentRound);
        } else { // Punish checker
            int newscoreChecker = pointsPerPlayerPerRound[playerCheckingIdx][currentRound] - WizardConstants.CHEAT_PENALTY;
            scores.setPointsPerPlayerPerRound(playerCheckingIdx, newscoreChecker, currentRound);
        }
    }

    public int matchPlayerNameToPlayerIdx(String playerToMatch) {
        int playerIdx = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(playerToMatch)) {
                playerIdx = i;
            }
        }
        return playerIdx;
    }

    // ########################### GETTERS BELOW

    public int getActivePlayerIndex() {
        return activePlayerIndex;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Notepad getScores() {
        return scores;
    }

    public Hand[] getPlayerHands() {
        return playerHands;
    }

    public Color getTrump() {
        return trump;
    }

    public CheatDetector getCheatDetector() {
        return cheatDetector;
    }

    // ########################### SETTERS BELOW

    public void setGamerunning(boolean gamerunning) {
        this.gamerunning = gamerunning;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setTable(Hand table) {
        this.table = table;
    }

    public void setScores(Notepad scores) {
        this.scores = scores;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public void setPlayerHands(Hand[] playerHands) {
        this.playerHands = playerHands;
    }

    public void setTrump(Color trump) {
        this.trump = trump;
    }

    public void setDealer(int dealer) {
        this.dealer = dealer;
    }

    public void setActivePlayerIndex(int activePlayerIndex) {
        this.activePlayerIndex = activePlayerIndex;
    }

    public void setTrickRoundTurn(int trickRoundTurn) {
        this.trickRoundTurn = trickRoundTurn;
    }

    public void setBetTricksCounter(int betTricksCounter) {
        this.betTricksCounter = betTricksCounter;
    }

    public void setCheatDetector(CheatDetector cheatDetector) {
        this.cheatDetector = cheatDetector;
    }
}

