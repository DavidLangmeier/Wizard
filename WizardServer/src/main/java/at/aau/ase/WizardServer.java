package at.aau.ase;

import java.io.IOException;

import at.aau.ase.androidnetworkwrapper.networking.kryonet.NetworkServerKryo;
import at.aau.ase.androidnetworkwrapper.networking.dto.TextMessage;

public class WizardServer {
    public static  void main(String[] args) {
        NetworkServerKryo server = new NetworkServerKryo();
        server.registerClass(TextMessage.class);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.registerCallback(basemessage -> {
            if (basemessage instanceof TextMessage) {
                System.out.println(basemessage.toString());
                server.broadcastMessage(
                        new TextMessage("Hi client, I'm the server and I'm waiting for requests!\nDid you say: "
                                + ((TextMessage) basemessage).text
                                + "?"
                        ));
            }
            else {
                System.out.println("Received message is not a Textmessage!");
                server.broadcastMessage(new TextMessage("Please send a Textmessage!"));
            }
        });
        System.out.println("Server started and Callback registered. Listening ...");
    }
}
