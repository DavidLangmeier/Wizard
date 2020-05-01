package at.aau.ase;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.WizardConstants;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.NetworkServerKryo;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.TextMessage;

import static com.esotericsoftware.minlog.Log.*;

public class WizardServer extends NetworkServerKryo {

    private static List<Player> players = new ArrayList<>();

    public WizardServer()  {
        super();
        DEBUG();
        for (Class c: WizardConstants.wizardNetworkClasses) {
            super.registerClass(c);
        }
        debug("WizardNetworkClasses registered");
        try {
            super.start();
            debug("Server started");
        } catch (IOException e) {
            error("Server start failed", e);
        }
    }

    public void test() {
        Integer id = super.getLastConnectionID();
        super.sentTo(id, new TextMessage("Just send to connectionID "+id));
    }

    public static  void main(String[] args) {
        WizardServer server = new WizardServer();

        server.registerCallback(basemessage -> {
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
            else if (basemessage instanceof ActionMessage) {
                info(basemessage.toString());
                //server.broadcastMessage(
                //        new TextMessage("Action "+((ActionMessage)basemessage).getActionType()+" received"))
                server.test();
            }
            else if (basemessage instanceof LobbyMessage) {
                LobbyMessage msg = (LobbyMessage) basemessage;
                info("New user "+msg.getNewUsername());
                Player newplayer = new Player(msg.getNewUsername());
                players.add(newplayer);
                server.broadcastMessage(new LobbyMessage(newplayer));
            }
            else {
                info("Received message is not a Textmessage!");
                server.broadcastMessage(new TextMessage("Please send a Textmessage!"));
            }
        });
        info("Server started and Callback registered. Listening ...");
    }

}
