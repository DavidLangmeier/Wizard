package at.aau.ase;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.esotericsoftware.minlog.Log.*;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.NetworkServerKryo;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.TextMessage;

public class WizardServer {
    public static  void main(String[] args) {
        NetworkServerKryo server = new NetworkServerKryo();
        server.registerClass(TextMessage.class);
        try {
            server.start();
        } catch (IOException e) {
            error("Server start failed", e);
        }
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
            else {
                info("Received message is not a Textmessage!");
                server.broadcastMessage(new TextMessage("Please send a Textmessage!"));
            }
        });
        info("Server started and Callback registered. Listening ...");
    }
}
