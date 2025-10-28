package Connections.Socket;

import Connections.*;
import Connections.Messages.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Socket version of the client
 */
public class ClientSocket extends Client{

    /**
     * Socket used for the connection
     */
    private final Socket socket;

    /**
     * Flows to read objects as messages
     */
    private final ObjectInputStream in;

    /**
     * Flows to write objects as messages
     */
    private final ObjectOutputStream out;

    /**
     * Client manager of the specific client
     */
    private final ClientInterface clientInterface;

    /**
     * Constructor, it initializes the attributes
     * @param IP current IP for socket
     * @param port current port for socket
     * @param clientInterface current client manager
     * @throws IOException input output exception thrown
     */
    public ClientSocket(String IP,int port,ClientInterface clientInterface) throws IOException{
        try{
            this.socket = new Socket(IP,port);
        } catch(Exception e){
            throw new SocketTimeoutException("Connection not established...");
        }
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        this.clientInterface=clientInterface;
    }

    /**
     * Method used to open a thread in order to read a message
     */
    @Override
    public void readMessage() {
        new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Message message = (Message) in.readObject();
                    try {
                        clientInterface.actions(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                System.err.println("Connection closed or error reading message: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Method used to send a message
     * @param message current message
     * @throws IOException input output exception thrown
     */
    @Override
    public synchronized void sendMessage(Message message) throws IOException{
        try{
            out.writeObject(message);
            out.reset();
        }
        catch(IOException e){
            throw new IOException();
        }
    }

    /**
     * Method used in order to close the connection
     * @throws IOException input output exception thrown
     */
    @Override
    public void disconnect() throws IOException{
        try{
            this.socket.close();
        }catch(Exception e){
            throw new IOException("Problem with disconnection.");
        }
    }
}