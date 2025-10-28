package model.Cards.CardVisitorProgresser;

import Connections.Messages.LogRequestMessage;
import Connections.Messages.Message;
import Controller.State.InitializationState;
import model.Bank;
import model.Cards.*;
import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.Deck;
import model.Game;
import model.ShipDashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardVisProg_stateTest {

    private CardVisProg_state cardVisProg_state;
    private CombatZone combatZone;
    private Slavers slavers;
    private AbandonedShip abandonedShip;
    private Pirates card1;
    private Smugglers card2;
    private AbandonedStation card3;
    private Epidemic card4;
    private MeteorSwarm card5;
    private OpenSpace card6;
    private Planets card7;
    private Stardust card8;

    @BeforeEach
    void setUp() {
        ArrayList<ShipDashboard> players = new ArrayList<>();
        ShipDashboard ship1 = new ShipDashboard();
        ShipDashboard ship2 = new ShipDashboard();
        ShipDashboard ship3 = new ShipDashboard();
        ship1.setNickname("diego");
        ship2.setNickname("giulio");
        ship3.setNickname("federica");
        players.add(ship1);
        players.add(ship2);
        players.add(ship3);
        cardVisProg_state = new CardVisProg_state(new Game(new Bank(60,60,60,60,60,60),6,new Deck(10,10),new ArrayList<>()),players,new ArrayList<>());
        cardVisProg_state.setGameState(new InitializationState());
        combatZone = new CombatZone(1,5,5,5,null,"");
        slavers = new Slavers(1,1,4,4,4,"");
        abandonedShip = new AbandonedShip(5,5,5,1,"");
        card1 = new Pirates(1,1,1,1,new ArrayList<>(),"");
        card2 = new Smugglers(null,1,1,1,1,"");
        card3 = new AbandonedStation(1,1,null,1,"");
        card4 = new Epidemic(1,"");
        card5 = new MeteorSwarm(1,new ArrayList<>(),"");
        card6 = new OpenSpace(1,"");
        card7 = new Planets(1,1,new ArrayList<>(),"");
        card8 = new Stardust(1,"");
    }

    @Test
    void getSenderIndex() {
        Message message1 = new LogRequestMessage("diego");
        Message message2 = new LogRequestMessage("giulio");
        Message message3 = new LogRequestMessage("federica");
        Message message4 = new LogRequestMessage("Sara");
        assertEquals(0,cardVisProg_state.getSenderIndex(message1));
        assertEquals(1,cardVisProg_state.getSenderIndex(message2));
        assertEquals(2,cardVisProg_state.getSenderIndex(message3));
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> cardVisProg_state.getSenderIndex(message4));
        assertTrue(exception.getMessage().contains("Couldn't find the player that sent the message in the players array:\n"));

    }

    @Test
    void visit() {
        Exception exception = assertThrows(RuntimeException.class, () -> cardVisProg_state.visit(card1));
        assertEquals("Unexpected card for InitializationState",exception.getMessage());
    }

    @Test
    void testVisit() {
        Exception exception = assertThrows(RuntimeException.class, () -> cardVisProg_state.visit(card2));
        assertEquals("Unexpected card for InitializationState",exception.getMessage());
    }

    @Test
    void testVisit1() {
        Exception exception = assertThrows(RuntimeException.class, () -> cardVisProg_state.visit(card3));
        assertEquals("Unexpected card for InitializationState",exception.getMessage());
    }

    @Test
    void testVisit2() {
        Exception exception = assertThrows(RuntimeException.class, () -> cardVisProg_state.visit(card4));
        assertEquals("Unexpected card for InitializationState",exception.getMessage());
    }

    @Test
    void testVisit3() {
        Exception exception = assertThrows(RuntimeException.class, () -> cardVisProg_state.visit(card5));
        assertEquals("Unexpected card for InitializationState",exception.getMessage());
    }

    @Test
    void testVisit4() {
        Exception exception = assertThrows(RuntimeException.class, () -> cardVisProg_state.visit(card6));
        assertEquals("Unexpected card for InitializationState",exception.getMessage());
    }

    @Test
    void testVisit5() {
        Exception exception = assertThrows(RuntimeException.class, () -> cardVisProg_state.visit(card7));
        assertEquals("Unexpected card for InitializationState",exception.getMessage());
    }

    @Test
    void testVisit6() {
        Exception exception = assertThrows(RuntimeException.class, () -> cardVisProg_state.visit(card8));
        assertEquals("Unexpected card for InitializationState",exception.getMessage());
    }

    @Test
    void testVisit7() {
        Exception exception = assertThrows(RuntimeException.class, () -> cardVisProg_state.visit(combatZone));
        assertEquals("Unexpected card for InitializationState",exception.getMessage());
    }

    @Test
    void testVisit8() {
        Exception exception = assertThrows(RuntimeException.class, () -> cardVisProg_state.visit(slavers));
        assertEquals("Unexpected card for InitializationState",exception.getMessage());
    }

    @Test
    void testVisit9() {
        Exception exception = assertThrows(RuntimeException.class, () -> cardVisProg_state.visit(abandonedShip));
        assertEquals("Unexpected card for InitializationState",exception.getMessage());
    }
}