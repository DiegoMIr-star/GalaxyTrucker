package model.Cards.CardVisitorProgresser;

import Connections.Messages.DynamicMotorPowerMessage;
import Controller.State.CardDrawing;
import Controller.State.FirePowerChoice;
import Controller.State.MotorPowerChoice;
import model.Cards.CombatZone;
import model.Cards.OpenSpace;
import model.Game;
import model.NextGameStateAndMessages;
import model.Projectiles.Projectile;
import model.ShipDashboard;
import model.exceptions.NullCallbackException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardVisProg_dynamicMotorPowerTest {
    //used for set up
    private Game game;
    private ArrayList<ShipDashboard> players;
    private ArrayList<String> nicks;
    private ShipDashboard sara;

    @BeforeEach
    void setUp() {
        game= mock(Game.class);
        sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        players = new ArrayList<>();
        players.add(sara);
        nicks = new ArrayList<>();
        nicks.add("sara");
    }

    @Test
    void testOpenSpaceVisit(){
        DynamicMotorPowerMessage mess = mock(DynamicMotorPowerMessage.class);
        when(mess.getNickname()).thenReturn("sara");
        OpenSpace openSpace = new OpenSpace(1, "TEST");
        ZeroMotorPowerListener recall = mock(ZeroMotorPowerListener.class);
        //power=0
        CardVisProg_dynamicMotorPower state= new CardVisProg_dynamicMotorPower(game, players, mess, nicks, 3, 5, 0, recall);
        NextGameStateAndMessages next = state.visit(openSpace);

        //is last player and call back happens
        verify(recall).OnZeroMotorPower(0);
        assertEquals(CardDrawing.class, next.getNextGameState().getClass());

        //null recall
        CardVisProg_dynamicMotorPower stat= new CardVisProg_dynamicMotorPower(game, players, mess, nicks, 3, 5, 0, null);
        //->exception
        assertThrows(NullCallbackException.class, () -> stat.visit(openSpace));

        ShipDashboard diego = mock(ShipDashboard.class);
        players.add(diego);
        //power not zero->
        CardVisProg_dynamicMotorPower status= new CardVisProg_dynamicMotorPower(game, players, mess, nicks, 3, 5, 1, recall);
        NextGameStateAndMessages nex = status.visit(openSpace);

        verify(sara).setDaysToMove(1);
        //not last
        assertEquals(MotorPowerChoice.class, nex.getNextGameState().getClass());
    }

    @Test
    void testCombatZoneVisit(){
        DynamicMotorPowerMessage mess = mock(DynamicMotorPowerMessage.class);
        when(mess.getNickname()).thenReturn("sara");
        Projectile[] projectiles= new Projectile[2];
        CombatZone combatZone = new CombatZone(1, 2,5, 3, projectiles, "TESTT");
        CardVisProg_dynamicMotorPower state= new CardVisProg_dynamicMotorPower(game, players, mess, nicks, 3, 5, 0, null);
        NextGameStateAndMessages next = state.visit(combatZone);

        assertEquals(0, combatZone.getAffectedPlayerIndex());
        //in this case both if case are positive so the last "wins"
        assertEquals(FirePowerChoice.class, next.getNextGameState().getClass());

        //adding new player
        ShipDashboard diego = mock(ShipDashboard.class);
        players.add(diego);
        nicks.add("diego");
        when(diego.getNickname()).thenReturn("diego");
        CardVisProg_dynamicMotorPower stat= new CardVisProg_dynamicMotorPower(game, players, mess, nicks, 3, 5, 0, null);
        NextGameStateAndMessages follow = stat.visit(combatZone);
        //NEXT-> motor power choice
        assertEquals(MotorPowerChoice.class, follow.getNextGameState().getClass());

        CombatZone combCard = mock(CombatZone.class);
        when(combCard.getLowestMotorPower()).thenReturn(10);
        when(mess.getNickname()).thenReturn("diego");
        CardVisProg_dynamicMotorPower status= new CardVisProg_dynamicMotorPower(game, players, mess, nicks, 3, 5, 9, null);
        status.visit(combCard);
        verify(combCard).setLowestMotorPower(9);

        //diego is last and crew loss and stocks penalty aren't zero
        assertEquals(FirePowerChoice.class, status.visit(combatZone).getNextGameState().getClass());

        //adding another player to check last case
        ShipDashboard giulio = mock(ShipDashboard.class);
        players.add(giulio);
        nicks.add("giulio");
        assertEquals(MotorPowerChoice.class, status.visit(combatZone).getNextGameState().getClass());
    }
}

