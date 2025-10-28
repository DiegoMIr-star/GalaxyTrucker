import Connections.RMI.ServerAdapterRMI;
import Connections.RMI.ServerImpl;
import Connections.Socket.SocketServer;
import Controller.Controller;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

/**
 * Main of the server useful to have both socket and RMI server ready to respond
 */
public class MainServer {
    public static void main(String[] args) throws IOException {
        //default port for Socket connection
        int port = 12345;
        String IP;
        boolean activeRMI = false;
        boolean activeSocket = false;

        for (String arg : args) {
            if (arg.equalsIgnoreCase("-rmi")) {
                activeRMI = true;
                System.out.println("Server RMI enabled");
            }
            else if (arg.equalsIgnoreCase("-socket")) {
                activeSocket = true;
                System.out.println("Server Socket enabled");
            } else {
                System.out.println("Unknown option: " + arg);
            }
        }

        /*persistence.createDirIfNotExists();
        try {
            System.out.println("Starting server from file...");
            controller = persistence.load();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found. A new controller will be created.");
            controller = new Controller();
            controller.addPersistence(persistence);
        }*/


        if (activeRMI) {
            try {
                //**************************
                try {
                    try (DatagramSocket socket = new DatagramSocket()) {
                        socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                        IP = socket.getLocalAddress().getHostAddress();
                        System.out.println("IP used for internet: " + IP);
                    }
                } catch (Exception e){
                    System.out.println("IP not found...");
                    return;
                }
                //****************************
                System.setProperty("java.rmi.server.hostname", IP);
                // System.out.println(InetAddress.getLocalHost().getHostAddress());
                ServerAdapterRMI serverAdapterRMI = new ServerAdapterRMI();
                ServerImpl serverRmi = new ServerImpl(serverAdapterRMI);
                serverRmi.start();
                System.out.println("RMI Server started");
            } catch (RemoteException | AlreadyBoundException e) {
                System.out.println("Remote exception: " + e.getMessage());
                throw new RuntimeException(e);
            }

        }

        if (activeSocket) {
            SocketServer socketServer = new SocketServer(port);
            Thread serverThread = new Thread(socketServer);
            serverThread.start();
            System.out.println("Socket Server started");
        }
    }
}
