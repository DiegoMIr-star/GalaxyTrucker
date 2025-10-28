package Connections.Socket;

import Connections.ClientHandler;
import Connections.Messages.Message;
import Connections.Messages.MessageKind;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Class which handles the clients in socket connection
 */
public class SocketHandler implements Runnable, ClientHandler {

	/**
	 * Current socket used for connection
	 */
	private transient final Socket socket;

	/**
	 * Current server used for connection
	 */
	private transient final SocketServer server;

	/**
	 * True if the client is connected to the server
	 */
	private boolean connected;

	/**
	 * Flows to write objects as messages
	 */
	private transient final ObjectOutputStream out;

	/**
	 * Flows to read objects as messages
	 */
	private final ObjectInputStream in;

	/**
	 * Client nickname
	 */
	private String nickname;

	/**
	 * Lock used for synchronization
	 */
	private final Object lock = new Object();

	/**
	 * Callback to the function that handles the disconnection of a client
	 */
	private Consumer<String> onDisconnect;

	/**
	 * Constructor of the server handler: it initializes all attributes
	 * @param server current server
	 * @param socket current socket
	 * @throws IOException input output exception
	 */
	public SocketHandler(SocketServer server, Socket socket) throws IOException {
		this.server = server;
		this.socket = socket;
		this.connected = false;
		this.out = new ObjectOutputStream(this.socket.getOutputStream());
		this.in = new ObjectInputStream(this.socket.getInputStream());
	}

	/**
	 * Method used to open a thread which receives the messages from the client
	 */
	@Override
	public void run() {
		//necessary for the communication
		Message message;
		try{
			//infinite loop
			while(!Thread.currentThread().isInterrupted()){
				//try to read a message
				message = (Message) this.in.readObject();
				//case log request: add the client
				if(message.getKind().equals(MessageKind.LOG_REQUEST)){
					server.addClient(this, message.getNickname());
					this.connected = true;

				}else{
					//if it's a real message, it forwards the message to server (and so controller)
					server.handleMessage(message);
				}
			}
		}catch (IOException | ClassNotFoundException exception){
			connected = false;
			//case client not available or errors
			try {
				in.close();
				if (out != null) out.close();
				if (socket != null && !socket.isClosed()) socket.close();
			} catch (IOException e) {
				e.printStackTrace(); // or log
			}
			if (onDisconnect != null) onDisconnect.accept(nickname);
			Thread.currentThread().interrupt();
		}

	}

	/**
	 * Method to send a message to the client
	 * @param message current message
	 * @throws IOException input output exception thrown
	 */
	@Override
	public void send(Message message) {
		synchronized (lock){
			try{

				//to serialize and write the message
				out.writeObject(message);
				//to send immediately the message
				out.flush();
				//to avoid to send old things
				out.reset();
			} catch (IOException e) {
				System.err.println("Error sending message: " + e.getMessage());
				if (onDisconnect != null) onDisconnect.accept(nickname);

				try {
					if (in != null) in.close();
					out.close();
					if (socket != null && !socket.isClosed()) socket.close();
				} catch (IOException cleanupEx) {
					System.err.println("Error during socket cleanup: " + cleanupEx.getMessage());
					cleanupEx.printStackTrace();
				}
			}
		}
	}

	/**
	 * Setter of the callback
	 * @param callback callback to the function that handles the disconnection of a client
	 */
	@Override
	public void setOnDisconnect(Consumer<String> callback) {
		this.onDisconnect = callback;
	}

	/**
	 * Setter of the nickname in the client
	 * @param nickname the client's nickname
	 */
	@Override
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	/**
	 * Method used to verify the connection
	 * @return true if there's the connection
	 */
	@Override
	public boolean isConnected(){return connected;}
}
