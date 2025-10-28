package Connections.RMI;

import Connections.ClientHandler;
import Connections.Messages.GameStateMessage;
import Connections.Messages.Message;


import java.io.IOException;
import java.util.function.Consumer;

/**
 * RMI client handler, used to handle the RMI connection for a client
 */
public class ClientHandlerRmi implements ClientHandler {

	/**
	 * True if the connection is established
	 */
	boolean connected = false;

	/**
	 * Reference to the remote client
	 */
	private final RemoteClientRMI client;

	/**
	 * Reference to the  remote server
	 */
	private final ServerImpl server;

	/**
	 * Callback to the function that handles the disconnection of a client
	 */
	private Consumer<String> onDisconnect;

	/**
	 * The clients nickname
	 */
	private String nickname;

	/**
	 * Class constructor, used to initialize server and client
	 * @param server current remote server
	 * @param client current remote client
	 */
	public ClientHandlerRmi(ServerImpl server, RemoteClientRMI client) {
		this.server = server;
		this.client = client;
		this.connected = true;
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
	 * Setter of the client's nickname
	 * @param nickname the client's nickname
	 */
	@Override
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * It sends message to the remote client
	 * @param message current message
	 */
	@Override
	public void send(Message message) {
		try{
			message.accept(new RMIServerToClient(client));
		} catch (IOException e) {
			System.err.println("Error sending message: " + e.getMessage());
			if (onDisconnect != null) onDisconnect.accept(nickname);
		}
	}

	/**
	 * It checks the connection
	 * @return true if it's connected
	 */
	@Override
	public boolean isConnected() {return connected;}
}
