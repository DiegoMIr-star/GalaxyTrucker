package model.Cards.CardVisitorProgresser;

import Controller.State.*;
import model.Cards.*;
import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.Game;
import model.NextGameStateAndMessages;
import model.ShipDashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CardVisProg_cardDrawingStateTest {
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
    void testStardustVisit(){
        Stardust stardust = mock(Stardust.class);
        CardVisProg_cardDrawingState state = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages next = state.visit(stardust);

        //next state->card drawing
        assertEquals(CardDrawing.class, next.getNextGameState().getClass());
    }

    @Test
    void testPlanetsVisit(){
        Planets planets = mock(Planets.class);
        CardVisProg_cardDrawingState state = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages next = state.visit(planets);

        assertEquals(PLANETS_LandingChoice.class, next.getNextGameState().getClass());
    }

    @Test
    void testOpenSpaceVisit(){
        OpenSpace openSpace = mock(OpenSpace.class);
        CardVisProg_cardDrawingState state = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages next = state.visit(openSpace);

        assertEquals(MotorPowerChoice.class, next.getNextGameState().getClass());
    }

    @Test
    void testMeteorSwarmVisit(){
        MeteorSwarm meteorSwarm = mock(MeteorSwarm.class);
        CardVisProg_cardDrawingState state = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages next = state.visit(meteorSwarm);

        assertEquals(ManageProjectile.class, next.getNextGameState().getClass());
    }

    @Test
    void testEpidemicVisit(){
        Epidemic epidemic = mock(Epidemic.class);
        CardVisProg_cardDrawingState state = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages next = state.visit(epidemic);

        assertEquals(CardDrawing.class, next.getNextGameState().getClass());
    }

    @Test
    void testCombatZoneVisit(){
        CombatZone combatZone = mock(CombatZone.class);
        CardVisProg_cardDrawingState state = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages next = state.visit(combatZone);

        assertEquals(MotorPowerChoice.class, next.getNextGameState().getClass());
    }

    @Test
    void testAbandonedShipVisit(){
        AbandonedShip abandonedShip = mock(AbandonedShip.class);
        //sara->getCrew()<card.crewLoss-> to increment index in the loop and go over the first if
        when(sara.getCrew()).thenReturn(-1);
        CardVisProg_cardDrawingState cur = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages follow = cur.visit(abandonedShip);
        assertEquals(CardDrawing.class, follow.getNextGameState().getClass());

        ShipDashboard diego = mock(ShipDashboard.class);
        players.add(diego);
        when(diego.getNickname()).thenReturn("diego");
        CardVisProg_cardDrawingState state = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages next = state.visit(abandonedShip);

        assertEquals(DockingChoice.class, next.getNextGameState().getClass());

    }

    @Test
    void testAbandonedStatVisit(){
        AbandonedStation abandonedStation = mock(AbandonedStation.class);
        //sara->getCrew()<card.crewLoss-> to increment index in the loop and go over the first if
        when(sara.getCrew()).thenReturn(-1);
        CardVisProg_cardDrawingState cur = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages follow = cur.visit(abandonedStation);
        assertEquals(CardDrawing.class, follow.getNextGameState().getClass());

        ShipDashboard diego = mock(ShipDashboard.class);
        players.add(diego);
        when(diego.getNickname()).thenReturn("diego");
        CardVisProg_cardDrawingState state = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages next = state.visit(abandonedStation);

        assertEquals(DockingChoice.class, next.getNextGameState().getClass());

    }

    @Test
    void testSmugglersVisit(){
        Smugglers smugglers = mock(Smugglers.class);
        CardVisProg_cardDrawingState state = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages next = state.visit(smugglers);

        assertEquals(FirePowerChoice.class, next.getNextGameState().getClass());
    }

    @Test
    void testSlaversVisit(){
        Slavers slavers = mock(Slavers.class);
        CardVisProg_cardDrawingState state = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages next = state.visit(slavers);

        assertEquals(FirePowerChoice.class, next.getNextGameState().getClass());
    }

    @Test
    void testPiratesVisit(){
        Pirates pirates = mock(Pirates.class);
        CardVisProg_cardDrawingState state = new CardVisProg_cardDrawingState(game, players, nicks);
        NextGameStateAndMessages next = state.visit(pirates);

        assertEquals(FirePowerChoice.class, next.getNextGameState().getClass());
    }
}
