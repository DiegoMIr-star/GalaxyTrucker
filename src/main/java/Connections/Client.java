package Connections;

import Connections.Messages.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class is used for the client to communicate with the server: it's abstract in order to
 * be implemented for the socket and the RMI connection
 */
public abstract class Client extends UnicastRemoteObject {

    /**
     * Constructor of the client
     * @throws RemoteException thrown in case of RMI
     */
    public Client() throws RemoteException{}

    /**
     * Method used to handle a message: RMI calls different methods
     * @param message current message
     * @throws IOException input output exception thrown
     */
    public void sendMessage(Message message) throws IOException{}

    /**
     * Method used in order to read a message
     */
    public void readMessage(){}

    /**
     * Method used for the disconnection
     * @throws IOException input output exception thrown
     */
    public void disconnect() throws IOException {}
}
