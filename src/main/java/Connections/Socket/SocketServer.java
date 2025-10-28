package Connections.Socket;

import Connections.Messages.Message;
import Connections.Messages.MessageKind;
import Connections.Server;
import Controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class used to implement the socket server
 */
public class SocketServer extends Server implements Runnable {

    /**
     * ServerSocket is a java object that accepts tcp connections
     */
    private ServerSocket serverSocket;
    /**
     * The specific port of the connection
     */
    private final int port;

    /**
     * Constructor which gives the controller to Server constructor and initializes the port
     * @see Server
     * @param port current port
     */
    public SocketServer(int port) {
        super();
        this.port = port;
    }

    /**
     * Method used to start the thread which waits for new connections
     */
    @Override
    public void run() {
        //starting the server
        try{
           this.serverSocket = new ServerSocket(this.port);
           System.out.println("Server Socket started on port " + this.port);
        } catch (IOException exception){
            System.out.println("Server Socket Error starting up.");
        }

        //waits for new connection requests
        while(!Thread.currentThread().isInterrupted()){
            try {
                Socket client= serverSocket.accept();
                System.out.println("Accepted connection from " + client.getRemoteSocketAddress());
                //client.setSoTimeout(5000);
                //creates a handler for each client
                SocketHandler clientSocket= new SocketHandler(this, client);
                //starts a new thread for each client
                Thread clientThread = new Thread(clientSocket);
                clientThread.start();
            } catch (IOException exception){
                System.out.println("Server Error accepting client connection.");
                exception.printStackTrace();
            }

        }
    }

    /**
     * Method used to handle a message
     * @param message current message
     * @throws IOException input output exception thrown
     */
    @Override
    public void handleMessage(Message message) throws IOException {
        if(message.getKind() == MessageKind.END_GAME_REQUEST){
            handleMessageEndGame(message);
        }
        else{
            //send messages from clients to the controller
            getController().handleMessageController(message);
        }
    }
}
