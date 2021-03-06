package at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.Callback;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.NetworkClient;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;

import static com.esotericsoftware.minlog.Log.*;

public class NetworkClientKryo implements NetworkClient, KryoNetComponent {
    private Client client;
    private Callback<BaseMessage> callback;

    public NetworkClientKryo() {
        client = new Client();
    }

    public void registerClass(Class c) {
        client.getKryo().register(c);
    }

    public void connect(String host) throws IOException {
        client.start();
        client.connect(5000, host, NetworkConstants.TCP_PORT);

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (callback != null && object instanceof BaseMessage)
                    callback.callback((BaseMessage) object);
            }
        });
    }

    public void registerCallback(Callback<BaseMessage> callback) {
        this.callback = callback;
    }

    public void deregisterCallback() {
        this.callback = null;
    }

    public void sendMessage(BaseMessage message) {
        client.sendTCP(message);
    }

    public void disconnect() {
        debug("Goodbye. Closing connection now.");
        client.stop();
        client.close();
    }
}
