package View;

import Controller.State.GameState;
import model.Cards.Card;
import model.ShipDashboard;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class used to implement TUI, GUI and Virtual View for the interaction between client and server
 * @see UI
 * @see VirtualView
 */
public interface View {
    void sendGenericMessage(String message) throws IOException;
    void askingNickname() throws IOException;
    void numberOfPlayers() throws IOException;
    void waitingPartners() throws IOException;
    void begin(ArrayList<String> players) throws IOException;
    void notifyTurn() throws IOException;
    void notifyNewGameState(GameState newGameState) throws IOException;
    void notifyNewCardDrawn(Card card) throws IOException;
    void winners(ArrayList<ShipDashboard> winners,String nickname) throws IOException;
}
