package at.aau.ase.libnetwork.androidnetworkwrapper.networking;

import java.io.IOException;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;

public interface NetworkServer {

    /**
     * Starts the Server.
     *
     * @throws IOException
     */
    void start() throws IOException;

    /**
     * Registers a callback which gets called if a message is received.
     *
     * @param callback
     */
    void registerCallback(Callback<BaseMessage> callback);

    /**
     * Sends a message to all clients.
     *
     * @param message
     */
    void broadcastMessage(BaseMessage message);

}
