package at.aau.ase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.Callback;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.PlayerMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.TextMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.DEAL;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.START;
import static com.esotericsoftware.minlog.Log.info;

public class ServerCallback implements Callback<BaseMessage> {

    private WizardServer server;
    private List<Player> players;
    private int playersReady;
    private Game game;

    public ServerCallback(WizardServer server, List<Player> players) {
        this.server = server;
        this.players = players;
        this.playersReady = 0;
    }

    @Override
    public void callback(BaseMessage message) {
        if (message instanceof TextMessage) {
            info(message.toString());
            server.broadcastMessage(
                    new TextMessage("Hi client, I'm the server and I'm waiting for requests!\nDid you say: "
                            + ((TextMessage) message).text
                            + "\nat "
                            + LocalDateTime.now().toString()
                            + " ?"
                    ));

        } else if (message instanceof LobbyMessage) {
            LobbyMessage msg = (LobbyMessage) message;
            info("New user " + msg.getNewUsername());
            Player newplayer = new Player(msg.getNewUsername(), server.getLastConnectionID());
            players.add(newplayer);
            info("Broadcasting newplayer as LobbyMessage.");
            server.broadcastMessage(new LobbyMessage(newplayer));

            PlayerMessage newPlayerMsg = new PlayerMessage(newplayer);
            info("Sending playerMessage to new Player.");
            server.sentTo(newplayer.getConnectionID(), newPlayerMsg);

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
