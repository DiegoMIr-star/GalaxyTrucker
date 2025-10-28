package model.Cards.CardVisitorProgresser;

import Connections.Messages.UpdateServerShipMessage;
import Controller.State.CardDrawing;
import Controller.State.FirePowerChoice;
import Controller.State.ManageProjectile;
import model.Cards.CombatZone;
import model.Cards.Enemies.Pirates;
import model.Cards.MeteorSwarm;
import model.Game;
import model.NextGameStateAndMessages;
import model.ShipDashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardVisProg_manageProjectileTest {
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
    void testMeteorSwarmVisit(){
        UpdateServerShipMessage message = mock(UpdateServerShipMessage.class);
        when(message.getNickname()).thenReturn("sara");
        MeteorSwarm meteorSwarm = mock(MeteorSwarm.class);
        when(meteorSwarm.getPlayersDoneManagingCurrentMeteor()).thenReturn(1);
        when(meteorSwarm.allMeteorsHandled()).thenReturn(false);
        when(meteorSwarm.getNextMeteorTrajectory()).thenReturn(-1);
        CardVisProg_manageProjectile status = new CardVisProg_manageProjectile(game, players, message, nicks);
        status.visit(meteorSwarm);

        verify(meteorSwarm).getPlayersDoneManagingCurrentMeteor();
        verify(meteorSwarm).randomizeTrajectory(meteorSwarm.getIndexOfNextMeteorToHandle());

        //adding giulio
        ShipDashboard giulio = mock(ShipDashboard.class);
        when(giulio.getNickname()).thenReturn("giulio");
        players.add(giulio);
        nicks.add("giulio");
        when(message.getNickname()).thenReturn("giulio");
        when(meteorSwarm.allMeteorsHandled()).thenReturn(true);
        when(meteorSwarm.getPlayersDoneManagingCurrentMeteor()).thenReturn(2);
        CardVisProg_manageProjectile stat = new CardVisProg_manageProjectile(game, players, message, nicks);
        NextGameStateAndMessages next = stat.visit(meteorSwarm);

        assertEquals(CardDrawing.class, next.getNextGameState().getClass());

        //case not last player managing meteor
        when(meteorSwarm.getPlayersDoneManagingCurrentMeteor()).thenReturn(1);
        when(meteorSwarm.allMeteorsHandled()).thenReturn(false);
        CardVisProg_manageProjectile statNew = new CardVisProg_manageProjectile(game, players, message, nicks);
        NextGameStateAndMessages follow= statNew.visit(meteorSwarm);
        //it remains in current state-> next state is null
        assertNull(follow.getNextGameState());
    }

    @Test
    void testCombatZoneVisit(){
        UpdateServerShipMessage message = mock(UpdateServerShipMessage.class);
        when(message.getNickname()).thenReturn("sara");
        CombatZone combatZone = mock(CombatZone.class);
        //all shot false
        when(combatZone.allShotsHandled()).thenReturn(false);
        CardVisProg_manageProjectile status = new CardVisProg_manageProjectile(game, players, message, nicks);
        //still curr state
        NextGameStateAndMessages follow =status.visit(combatZone);

        verify(combatZone).markShotAsHandled();
        assertNull(follow.getNextGameState());

        //case: all shot handled true
        when(combatZone.allShotsHandled()).thenReturn(true);
        NextGameStateAndMessages following =status.visit(combatZone);

        verify(combatZone).setLowestFirePower(-1);
        verify(combatZone).setAffectedPlayerIndex(-1);
        //->next state
        assertEquals(CardDrawing.class, following.getNextGameState().getClass());
    }

    @Test
    void testPiratesVisit(){
        UpdateServerShipMessage message = mock(UpdateServerShipMessage.class);
        when(message.getNickname()).thenReturn("sara");
        Pirates pirates = mock(Pirates.class);
        //all shot handled false and trajectory -1->
        when(pirates.allShotsHandled()).thenReturn(false);
        when(pirates.getNextShotTrajectory()).thenReturn(-1);
        CardVisProg_manageProjectile status = new CardVisProg_manageProjectile(game, players, message, nicks);
        NextGameStateAndMessages follow =status.visit(pirates);

        verify(pirates).markShotAsHandled();
        assertEquals(ManageProjectile.class, follow.getNextGameState().getClass());

        //case not all handled and last player
        when(pirates.allShotsHandled()).thenReturn(true);
        NextGameStateAndMessages following =status.visit(pirates);
        verify(pirates).prepareShotsForNewTurn();
        assertEquals(CardDrawing.class, following.getNextGameState().getClass());

        //true and NOT last player
        ShipDashboard giulio = mock(ShipDashboard.class);
        when(giulio.getNickname()).thenReturn("giulio");
        players.add(giulio);
        nicks.add("giulio");
        CardVisProg_manageProjectile stat = new CardVisProg_manageProjectile(game, players, message, nicks);
        NextGameStateAndMessages next = stat.visit(pirates);
        assertEquals(FirePowerChoice.class, next.getNextGameState().getClass());
    }
}
