package at.aau.ase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.Callback;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.EndscreenMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.CheatMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.ErrorMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.GoodbyeMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.CardMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LifecycleMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.NotePadMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.PlayerMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.TextMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

import static com.esotericsoftware.minlog.Log.*;

/**
 * Class to handle all incoming messages of all clients.
 * Defines all actions of the server as response to client messages.
 */
public class ServerCallback implements Callback<BaseMessage> {

    private WizardServer server;
    private List<Player> players;
    private int playersReady;
    private Game game = null;
    private EndscreenCalculations calculations = new EndscreenCalculations();

    public ServerCallback(WizardServer server, List<Player> players) {
        this.server = server;
        this.players = players;
        this.playersReady = 0;
    }

    @Override
    public void callback(BaseMessage message) {
        if (message instanceof LobbyMessage) {
            handleLobbyMessage((LobbyMessage) message);
        } else if (message instanceof GoodbyeMessage) {
            handleGoodbyeMessage((GoodbyeMessage) message);
        } else if (message instanceof LifecycleMessage) {
            handleLifecycleMessage((LifecycleMessage) message);
        } else if (message instanceof ActionMessage) {
            handleActionMessage((ActionMessage) message);
        } else if (message instanceof CardMessage) {
            handleCardMessage((CardMessage) message);
        } else if (message instanceof NotePadMessage) {
            handleNotepadMessage((NotePadMessage) message);
        } else if (message instanceof CheatMessage) {
            handleCheatMessage((CheatMessage) message);
        } else if (message instanceof EndscreenMessage) {
            handleEndscreenMessage((EndscreenMessage) message);
        }else{
            info("Received message cannot be handled correctly!");
            server.broadcastMessage(new TextMessage("Server could not handle sent message correctly!"));
        }
    }

    private void handleNotepadMessage(NotePadMessage message) {
        info("Recieved Notepad to enter prediction!");
        NotePadMessage msg = message;
        if (!(game.checkBet(msg.getBetTrick()))) {
            server.sentTo(game.getActivePlayerID(), new ErrorMessage("Not valid!"));
        } else {
            game.writeBetTricksToNotePad(msg.getScores(), game.getActivePlayerIndex(), msg.getBetTrick());
        }
    }

    private void handleCardMessage(CardMessage message) {
        info("Recieved Card to put on Table!");
        CardMessage msg = message;
        info("SERVER_CALLBACK: CARD: " + msg.getCard().toString());
        info("SERVER_CALLBACK: Card object id: " + msg.getCard().hashCode());
        game.dealOnePlayerCardToTable(msg.getCard());
    }

    private void handleActionMessage(ActionMessage message) {
        info("Received ActionMessage.");
        ActionMessage msg = message;

        switch (msg.getActionType()) {
            case START:
                info("Received Action START. Creating new game.");
                game = new Game(server, players);
                info("Starting game.");
                game.startGame();
                break;

            case DEAL:
                info("Received Action DEAL.");
                game.dealCards();
                break;

            case READY:
                info("Received Action READY.");
                playersReady++;
                info(playersReady + "/" + players.size() + " users are ready.");
                if (playersReady >= players.size()) {
                    game.broadcastGameState();
                }
                break;

            case EXIT:
                info("Recieved Action EXIT.");
                game.setGamerunning(false);
                playersReady = 0;
                players = new ArrayList<>();
                game = null;
                server.broadcastMessage(new ActionMessage(Action.EXIT));
                break;


            default:
                info("Unknown Action. Cannot handle Message");
        }
    }

    private void handleLifecycleMessage(LifecycleMessage message) {
        info("Received LifecycleMessage: " + message.getMsg());
        server.broadcastMessage(message);
    }

    private void handleGoodbyeMessage(GoodbyeMessage message) {
        GoodbyeMessage msg = message;
        Player playerLeaving = msg.getPlayer();
        if (playerLeaving != null) { // Player closed app
            info("User " + playerLeaving.getName() + " left: " + msg.getGoodbye());
            for (Player p : players) {
                if (p.getPlayerId() == playerLeaving.getConnectionID()) {
                    this.players.remove(p);
                }
            }
            server.broadcastMessage(msg);
        } else { // Player tried to join a running game in progress
            info("Late joining user connection closed: " + msg.getGoodbye());
        }
    }

    private void handleLobbyMessage(LobbyMessage message) {
        LobbyMessage msg = message;
        info("New user " + msg.getNewUsername());
        Integer newPlayerID = server.getLastConnectionID();
        Player newplayer = new Player(msg.getNewUsername(), newPlayerID);
        debug(msg.getNewUsername() + " has now ID: " + newPlayerID);
        debug("=====================" + game);
        if (game != null) {
            if (game.isGamerunning()) { // if game is already running send back info and close connection
                server.sentTo(newPlayerID, new ErrorMessage("Game is already in progress, join later"));
                debug("Send error msg, that game is already runnning");
            }
        } else {
            players.add(newplayer);
            info("Broadcasting newplayer as LobbyMessage.");
            server.broadcastMessage(new LobbyMessage(players));

            PlayerMessage newPlayerMsg = new PlayerMessage(newplayer);
            info("Sending playerMessage to new Player.");
            server.sentTo(newplayer.getConnectionID(), newPlayerMsg);
        }
    }

    private void handleCheatMessage(CheatMessage message) {
        String playerSuspectedOfCheating = message.getPlayerName();
        String playerChecking = message.getSender().getName();
        debug("=============> CHEATING: player suspected of cheating: "+playerSuspectedOfCheating);
        debug("=============> CHEATING: player checking: "+playerChecking);
        boolean isCheating = game.getCheatDetector().check(playerSuspectedOfCheating);
        debug("=============> CHEATING: isCheating("+playerSuspectedOfCheating+") = "+isCheating);
        if (isCheating) {
            game.updateScoresCheating(isCheating, playerSuspectedOfCheating, playerChecking);
            server.sentTo(message.getSender().getConnectionID(), new CheatMessage("Correct: Player "+playerSuspectedOfCheating+" is cheating!"));
            game.broadcastGameState();
        } else {
            game.updateScoresCheating(isCheating, playerSuspectedOfCheating, playerChecking);
            server.sentTo(message.getSender().getConnectionID(), new CheatMessage("Wrong: Player "+playerSuspectedOfCheating+" is not cheating!"));
            game.broadcastGameState();
        }
    }

    private void handleEndscreenMessage(EndscreenMessage message) {
        EndscreenMessage msg = message;
        Notepad endscreenScores = new Notepad();
        List<String> playersInRankingOrder = calculations.sortPlayersByRanking(msg.getScores().getTotalPointsPerPlayer(), msg.getScores().getPlayerNamesList());
        int[][] totalPointsInRankingOrder = calculations.sortPlayerTotalPointsByRanking(msg.getScores().getTotalPointsPerPlayer(), msg.getScores().getPlayerNamesList());
        debug("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + Arrays.deepToString(totalPointsInRankingOrder));
        int[] imageID = calculations.setActualIconID(totalPointsInRankingOrder);
        endscreenScores.setPlayerNamesList(playersInRankingOrder);
        endscreenScores.setTotalPointsPerPlayer(totalPointsInRankingOrder);
        server.broadcastMessage(new EndscreenMessage(endscreenScores, imageID));
    }

    // ###################### Setter
    public void setGame(Game game) {
        this.game = game;
    }

}
