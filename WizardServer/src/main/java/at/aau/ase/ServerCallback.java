package at.aau.ase;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.Callback;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.CheatMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.ErrorMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.GoodbyeMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.CardMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LifecycleMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.NotePadMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.PlayerMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
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
    private CheatDetector cheatDetector;

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
        } else {
            info("Received message cannot be handled correctly!");
            server.broadcastMessage(new TextMessage("Server could not handle sent message correctly!"));
        }
    }

    private void handleNotepadMessage(NotePadMessage message) {
        info("Recieved Notepad to enter prediction!");
        NotePadMessage msg = message;
        if(!(game.checkBet(msg.getBetTrick()))){
            server.sentTo(msg.getActivePlayer(), new ErrorMessage("Not valid!"));
        }else{
            game.writeBetTricksToNotePad(msg.getScores(), (msg.getActivePlayer() - 1), msg.getBetTrick());
        }
    }

    private void handleCardMessage(CardMessage message) {
        info("Recieved Card to put on Table!");
        CardMessage msg = message;
        info("SERVER_CALLBACK: CARD: " + msg.getCard().toString());
        info("SERVER_CALLBACK: Card object id: " + msg.getCard().hashCode());
        game.dealOnePlayerCardToTable(msg.getCard());
        cheatDetector.update(msg.getCard());
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
                this.cheatDetector = new CheatDetector(game);
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

            default:
                info("Unknown Action. Cannot handle Message");
        }
    }

    private void handleLifecycleMessage(LifecycleMessage message) {
        info("Received LifecycleMessage: "+message.getMsg());
        server.broadcastMessage(message);
    }

    private void handleGoodbyeMessage(GoodbyeMessage message) {
        GoodbyeMessage msg = message;
        Player playerLeaving = msg.getPlayer();
        if (playerLeaving != null) { // Player closed app
            info("User " + playerLeaving.getName() + " left: " + msg.getGoodbye());
            for (Player p : players) {
                if (p.getPlayer_id() == playerLeaving.getConnectionID()) {
                    this.players.remove(p);
                }
            }
            server.broadcastMessage(msg);
        } else { // Player tried to join a running game in progress
            info("Late joining user connection closed: "+msg.getGoodbye());
        }
    }

    private void handleLobbyMessage(LobbyMessage message) {
        LobbyMessage msg = message;
        info("New user " + msg.getNewUsername());
        Integer newPlayerID = server.getLastConnectionID();
        Player newplayer = new Player(msg.getNewUsername(), newPlayerID);
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
        String playerToCheck = message.getPlayerName();
        boolean isCheating = cheatDetector.check(playerToCheck);//game.getCheatDetector().check(playerToCheck);
        if (isCheating) {
            game.updateScoresCheating(isCheating, message.getSender().getName(), playerToCheck);
            server.sentTo(message.getSender().getConnectionID(), new CheatMessage("Correct: Player "+playerToCheck+" is cheating!"));
            game.broadcastGameState();
        } else {
            game.updateScoresCheating(isCheating, message.getSender().getName(), playerToCheck);
            server.sentTo(message.getSender().getConnectionID(), new CheatMessage("Wrong: Player "+playerToCheck+" is not cheating!"));
            game.broadcastGameState();
        }
    }
}
