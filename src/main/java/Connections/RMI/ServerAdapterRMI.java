package Connections.RMI;

import Connections.Messages.Message;
import Connections.Server;
import Controller.Controller;

import java.io.IOException;

/**
 * Class used to avoid to implement socket for rmi because ServerImpl expects Server
 */
public class ServerAdapterRMI extends Server {

    /**
     * Constructor used to initialize controller
     */
    public ServerAdapterRMI() {super();}

    /**
     * Method used to call the specific handle message
     * @param message current message
     * @throws IOException input output exception thrown
     */
    @Override
    public void handleMessage(Message message) throws IOException {Controller.getInstance().handleMessageController(message);}
}
