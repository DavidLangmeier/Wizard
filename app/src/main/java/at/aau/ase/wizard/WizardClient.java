package at.aau.ase.wizard;

import java.io.IOException;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.NetworkClientKryo;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.WizardConstants;

import static com.esotericsoftware.minlog.Log.*;

/**
 * Class to be used to access the network on the Android client. Send messages to the server.
 * Do not instantiate the class, instead use WizardClient.getInstance();
 */
public class WizardClient extends NetworkClientKryo {

    //private String hostname = "192.168.178.35";
    private String hostname = "se2-demo.aau.at";

    private static WizardClient wizardClient = null;

    private WizardClient() {
        super();
        DEBUG();
        for (Class<BaseMessage> c : WizardConstants.getWizardNetworkClasses()) {
            super.registerClass(c);
        }
        debug("WizardNetworkClasses registered");
        new Thread(() -> {
            try {
                super.connect(hostname);
            } catch (IOException e) {
                error("Could not establish network connection to server " + hostname, e);
            }
        }).start();
        debug("Network connection established");
    }

    /**
     * Static method to get an instance of the network client.
     *
     * @return An instance of WizardClient, which can be used to send messages to the server
     */
    public static WizardClient getInstance() {
        if (wizardClient == null)
            wizardClient = new WizardClient();
        return wizardClient;
    }

    /**
     * Send a message to the server. The message has to be a subclass of BaseMessage.
     * All our defined messagetypes should extend BaseMessage and provide a default constructor.
     *
     * @param message, can be MessageType we have defined.
     */
    @Override
    public void sendMessage(BaseMessage message) {
        new Thread(() ->
                super.sendMessage(message)
        ).start();
    }
}
