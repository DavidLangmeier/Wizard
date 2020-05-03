package at.aau.ase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;
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
        for (Class<BaseMessage> c: WizardConstants.getWizardNetworkClasses()) {
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

        server.registerCallback(new ServerCallback(server,players));
        info("Server started and ServerCallback registered. Listening ...");
    }

}
