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

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.SHUFFLE;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.START;
import static com.esotericsoftware.minlog.Log.info;

public class ServerCallback implements Callback<BaseMessage> {

    private WizardServer server;
    private List<Player> players;
    Game game;

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
        else if ((basemessage instanceof ActionMessage) && (((ActionMessage) basemessage).getActionType() == START)) {
            info(basemessage.toString());

            //server.broadcastMessage(
            //        new TextMessage("Action "+((ActionMessage)basemessage).getActionType()+" received"))
            //server.test();

            game = new Game(server, players);
            Thread gameThread = new Thread (game);
            gameThread.start();


        }
        else if (basemessage instanceof LobbyMessage) {
            LobbyMessage msg = (LobbyMessage) basemessage;
            info("New user "+msg.getNewUsername());
            Player newplayer = new Player(msg.getNewUsername(),server.getLastConnectionID());
            players.add(newplayer);
            info("Broadcasting newplayer as LobbyMessage.");
            server.broadcastMessage(new LobbyMessage(newplayer));

            info("Sending playerMessage to new Player");
            PlayerMessage newPlayerMsg = new PlayerMessage(newplayer);
            server.sentTo(newplayer.getConnectionID(), newPlayerMsg);
        }
        else {
            info("Received message is not a Textmessage!");
            server.broadcastMessage(new TextMessage("Please send a Textmessage!"));
        }
    }
}
