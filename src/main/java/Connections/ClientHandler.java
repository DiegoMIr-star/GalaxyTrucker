package Connections;


import Connections.Messages.Message;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * The class represents a generic client connected to the server
 */
public interface ClientHandler {

    /**
     * Send method is useful to send a message to a client (for example client.send(message))
     * @param message current message
     */
    void send(Message message) ;

    /**
     * Setter of the callback for when a client disconnects
     * @param callback the function that will be called when the client disconnects
     */
    void setOnDisconnect(Consumer<String> callback);

    /**
     * Setter of the nickname in the client
     * @param nickname the client's nickname
     */
    void setNickname(String nickname);

    /**
     * To verify if the client is connected or not
     * @return true if the client is disconnected
     * @throws IOException input output exception thrown
     */
    boolean isConnected() throws IOException;
}
