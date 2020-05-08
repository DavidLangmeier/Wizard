package at.aau.ase.wizard;

import java.io.IOException;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.NetworkClientKryo;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.WizardConstants;

import static com.esotericsoftware.minlog.Log.*;

public class WizardClient extends NetworkClientKryo {

    private String hostname = "192.168.1.80";
    //private String hostname = "se2-demo.aau.at";
    private static WizardClient wizardClient = null;

    private WizardClient() {
        super();
        DEBUG();
        for (Class<BaseMessage> c: WizardConstants.getWizardNetworkClasses()) {
            super.registerClass(c);
        }
        debug("WizardNetworkClasses registered");
        new Thread(() -> {
            try {
                super.connect(hostname);
            } catch (IOException e) {
                error("Could not establish network connection to server "+hostname, e);
            }
        }).start();
        debug("Network connection established");
    }

    public static WizardClient getInstance() {
        if (wizardClient == null)
            wizardClient = new WizardClient();
        return wizardClient;
    }

    @Override
    public void sendMessage(BaseMessage message) {
        new Thread(() ->
            super.sendMessage(message)
        ).start();
    }
}
