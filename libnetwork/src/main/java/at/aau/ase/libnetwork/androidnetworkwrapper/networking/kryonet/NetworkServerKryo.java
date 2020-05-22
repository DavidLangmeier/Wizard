package at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.Callback;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.NetworkServer;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;

public class NetworkServerKryo implements NetworkServer, KryoNetComponent {
    private Server server;
    private Callback<BaseMessage> messageCallback;

    public NetworkServerKryo() {
        server = new Server();
    }

    public void registerClass(Class c) {
        server.getKryo().register(c);
    }

    public void start() throws IOException {
        server.start();
        server.bind(NetworkConstants.TCP_PORT);

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (messageCallback != null && object instanceof BaseMessage)
                    messageCallback.callback((BaseMessage) object);
            }
        });
    }

    public void registerCallback(Callback<BaseMessage> callback) {
        this.messageCallback = callback;
    }

    /**
     * Send a message to all connected clients.
     * @param message to be send. Has to be subclass of BaseMessage, like all our defined classes should be.
     */
    public void broadcastMessage(BaseMessage message) {
        for (Connection connection : server.getConnections())
            connection.sendTCP(message);
    }

    /**
     * Send a message to just one client. You need to specify the connectionID of the client you want to send the msg to.
     * connectionID should be part of the player in List<Player> players, which keeps all players known to the server.
     * @param connectionID, use the one from player in players.
     * @param object, the message to be send. Should be subclass of BaseMessage.
     */
    public void sentTo(Integer connectionID, Object object) {
        server.sendToTCP(connectionID, object);
    }

    /**
     * Send a message to all connected clients except the specified one.
     * @param connectionID of the player not receiving the message. (Check players list for the connectionIDs)
     * @param object to be send. Should be subclass of BaseMessage.
     */
    public void sendToAllExcept(Integer connectionID, Object object) {
        server.sendToAllExceptTCP(connectionID, object);
    }

    /**
     * Returns the latest kryonet connectionID. This method should only be used in lobby where the clients connect.
     * Later on the connectionIDs can be accessed by the List<Player> players of the WizardServer.
     * @return the integer kryonet connectionID
     */
    public Integer getLastConnectionID() {
        if (server.getConnections().length == 0)
            return null;
        return server.getConnections()[0].getID();
    }

    /**
     * To disconnect a specific client connection. Used in case a user connects but the game is already running.
     * @param connectionID, which is to be closed.
     * @return true if successfully closed and false if the given connection was not closed.
     */
    public boolean disconnect(Integer connectionID) {
        Connection[] connections = server.getConnections();
        for (Connection c: connections) {
            if (c.getID() == connectionID) {
                c.close();
                return true;
            }
        }
        return false;
    }
}
