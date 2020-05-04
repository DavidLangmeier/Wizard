package at.aau.ase;

import java.time.LocalDateTime;
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
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.SHUFFLE;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.START;
import static com.esotericsoftware.minlog.Log.info;

public class ServerCallback implements Callback<BaseMessage> {

    private WizardServer server;
    private List<Player> players;
    private Game game;

    public ServerCallback(WizardServer server, List<Player> players) {
        this.server = server;
        this.players = players;
    }

    @Override
    public void callback(BaseMessage basemessage) {
        if (basemessage instanceof TextMessage) {
            info(basemessage.toString());
            server.broadcastMessage(
                    new TextMessage("Hi client, I'm the server and I'm waiting for requests!\nDid you say: "
                            + ((TextMessage) basemessage).text
                            + "\nat "
                            + LocalDateTime.now().toString()
                            + " ?"
                    ));
        }

        else if (basemessage instanceof LobbyMessage) {
            LobbyMessage msg = (LobbyMessage) basemessage;
            info("New user "+msg.getNewUsername());
            Player newplayer = new Player(msg.getNewUsername(),server.getLastConnectionID());
            players.add(newplayer);
            info("Broadcasting newplayer as LobbyMessage.");
            server.broadcastMessage(new LobbyMessage(newplayer));

            PlayerMessage newPlayerMsg = new PlayerMessage(newplayer);
            info("Sending playerMessage to new Player");
            info(newPlayerMsg.toString());
            server.sentTo(newplayer.getConnectionID(), newPlayerMsg);
        }

        else if ((basemessage instanceof ActionMessage) && (((ActionMessage) basemessage).getActionType() == START)) {
            info("Received Action START.");
            //server.broadcastMessage(
            //        new TextMessage("Action "+((ActionMessage)basemessage).getActionType()+" received"))
            //server.test();
            info("Creating new game.");
            game = new Game(server, players);
            info("Starting game.");
            game.startGame();
        }

        else if ((basemessage instanceof ActionMessage) && (((ActionMessage) basemessage).getActionType() == DEAL)) {
            info("Received Action DEAL.");
            game.dealCards();
        }

        else {
            info("Received message cannot be handled correctly!");
            server.broadcastMessage(new TextMessage("Server could not handle sent message correctly!"));
        }
    }
}
