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
                server.broadcastMessage(new TextMessage("Received: "+ basemessage.toString()));
            }
            else {
                System.out.println("Not a Textmessage!");
                server.broadcastMessage(new TextMessage("Received: Not a Textmessage"));
            }
        });
    }
}
