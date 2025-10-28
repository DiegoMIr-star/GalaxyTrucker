package model;

import Connections.Messages.LogRequestMessage;
import Controller.State.InitializationState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NextGameStateAndMessagesTest {

    private NextGameStateAndMessages nextGameStateAndMessages;
    @BeforeEach
    void setUp() {
        nextGameStateAndMessages = new NextGameStateAndMessages(null);
    }

    @Test
    void setPlayers() {
        ArrayList<ShipDashboard> players = new ArrayList<>();
        ShipDashboard player1 = new ShipDashboard();
        ShipDashboard player2 = new ShipDashboard();
        ShipDashboard player3 = new ShipDashboard();
        ShipDashboard player4 = new ShipDashboard();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        assertNull(nextGameStateAndMessages.getPlayers());
        nextGameStateAndMessages.setPlayers(players);
        assertEquals(players, nextGameStateAndMessages.getPlayers());
        assertEquals(player1, nextGameStateAndMessages.getPlayers().getFirst());
        assertEquals(player2, nextGameStateAndMessages.getPlayers().get(1));
        assertEquals(player3, nextGameStateAndMessages.getPlayers().get(2));
        assertEquals(player4, nextGameStateAndMessages.getPlayers().get(3));
    }

    @Test
    void setNextGameState() {
        assertNull(nextGameStateAndMessages.getNextGameState());
        nextGameStateAndMessages.setNextGameState(new InitializationState());
        assertInstanceOf(InitializationState.class, nextGameStateAndMessages.getNextGameState());
    }

    @Test
    void setPlayerMessage() {
        nextGameStateAndMessages.setPlayerMessage("Diego",new LogRequestMessage("Diego"));
        nextGameStateAndMessages.setPlayerMessage("Sara",new LogRequestMessage("Sara"));
        nextGameStateAndMessages.setPlayerMessage("Federica",new LogRequestMessage("Federica"));
        nextGameStateAndMessages.setPlayerMessage("Giulio",new LogRequestMessage("Giulio"));
        assertInstanceOf(LogRequestMessage.class,nextGameStateAndMessages.getPlayerMessage("Diego").getFirst());
        assertInstanceOf(LogRequestMessage.class,nextGameStateAndMessages.getPlayerMessage("Federica").getFirst());
        assertInstanceOf(LogRequestMessage.class,nextGameStateAndMessages.getPlayerMessage("Sara").getFirst());
        assertInstanceOf(LogRequestMessage.class,nextGameStateAndMessages.getPlayerMessage("Giulio").getFirst());
        assertInstanceOf(LogRequestMessage.class,nextGameStateAndMessages.getPlayerMessages().get("Diego").getFirst());
        assertInstanceOf(LogRequestMessage.class,nextGameStateAndMessages.getPlayerMessages().get("Sara").getFirst());
        assertInstanceOf(LogRequestMessage.class,nextGameStateAndMessages.getPlayerMessages().get("Federica").getFirst());
        assertInstanceOf(LogRequestMessage.class,nextGameStateAndMessages.getPlayerMessages().get("Giulio").getFirst());
    }
}