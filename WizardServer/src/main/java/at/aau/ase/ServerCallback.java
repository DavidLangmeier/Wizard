package at.aau.ase;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.Callback;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.ErrorMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.GoodbyeMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.PlayerMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.TextMessage;
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

    public ServerCallback(WizardServer server, List<Player> players) {
        this.server = server;
        this.players = players;
        this.playersReady = 0;
    }

    @Override
    public void callback(BaseMessage message) {
        if (message instanceof LobbyMessage) {
            LobbyMessage msg = (LobbyMessage) message;
            info("New user " + msg.getNewUsername());
            Integer newPlayerID = server.getLastConnectionID();
            Player newplayer = new Player(msg.getNewUsername(), newPlayerID);
            if (game != null) {
                if (game.isGamerunning()) { // if game is already running send back info and close connection
                    server.sentTo(newPlayerID, new ErrorMessage("Game is already in progress, join later"));
                    //server.disconnect(newPlayerID);
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
        } else if (message instanceof GoodbyeMessage) {
            GoodbyeMessage msg = (GoodbyeMessage) message;
            Player playerLeaving = msg.getPlayer();
            info("User "+playerLeaving.getName()+" left: "+msg.getGoodbye());
            for (Player p: players) {
                if (p.getPlayer_id() == playerLeaving.getConnectionID()) {
                    this.players.remove(p);
                }
            }
        } else if (message instanceof ActionMessage) {
            info("Received ActionMessage.");
            ActionMessage msg = (ActionMessage) message;

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
                    info(playersReady +"/" +players.size() +" users are ready.");
                    if (playersReady >= players.size()) {
                        game.broadcastGameState();
                    }
                    break;

                default:
                    info("Unknown Action. Cannot handle Message");
            }

        } else {
            info("Received message cannot be handled correctly!");
            server.broadcastMessage(new TextMessage("Server could not handle sent message correctly!"));
        }
    }

}
