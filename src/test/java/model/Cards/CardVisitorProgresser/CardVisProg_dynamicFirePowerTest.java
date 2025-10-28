package model.Cards.CardVisitorProgresser;

import Connections.Messages.Message;
import Connections.Messages.TurnMessage;
import Controller.State.*;
import model.Cards.CombatZone;
import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.Game;
import model.NextGameStateAndMessages;
import model.Projectiles.Orientation;
import model.Projectiles.Projectile;
import model.Projectiles.ProjectileType;
import model.ShipDashboard;
import model.Stocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CardVisProg_dynamicFirePowerTest {
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
    void testVisitCombZone(){
        Message message = mock(Message.class);
        when(message.getNickname()).thenReturn("sara");
        CombatZone combatZone = new CombatZone(1, 2, 2, 1, new Projectile[]{new Projectile(Orientation.SOUTH, ProjectileType.LIGHT_SHOT)}, "TEST");
        CardVisProg_dynamicFirePower state = new CardVisProg_dynamicFirePower(game, players, message, nicks, 2, 3, 1);
        NextGameStateAndMessages next = state.visit(combatZone);

        assertEquals(0, combatZone.getAffectedPlayerIndex());
        assertEquals(1, combatZone.getLowestFirePower());
        //sara is last player-> next state is manage proj
        assertEquals(ManageProjectile.class, next.getNextGameState().getClass());

        //adding giulio e diego-> GIULIO is not the last
        when(message.getNickname()).thenReturn("giulio");
        ShipDashboard giulio = mock(ShipDashboard.class);
        when(giulio.getNickname()).thenReturn("giulio");
        players.add(giulio);
        ShipDashboard diego = mock(ShipDashboard.class);
        when(diego.getNickname()).thenReturn("diego");
        players.add(diego);
        //I'm setting firepower lower than the lowest firepower
        CardVisProg_dynamicFirePower stat= new CardVisProg_dynamicFirePower(game, players, message, nicks, 2, 3, 0);
        NextGameStateAndMessages follow = stat.visit(combatZone);

        assertTrue(follow.getPlayerMessage("diego").getFirst() instanceof TurnMessage);
        //in this case the following state doesn't change-> changes only the turn!!
    }

    @Test
    void testVisitSmugglers(){
        Message message = mock(Message.class);
        when(message.getNickname()).thenReturn("sara");
        Smugglers smugglers = new Smugglers(new Stocks(1,2,3,1), 2,2,3,1, "TEST");
        CardVisProg_dynamicFirePower stat= new CardVisProg_dynamicFirePower(game, players, message, nicks, 2, 3, 3);
        NextGameStateAndMessages next = stat.visit(smugglers);

        //->claim reward
        assertEquals(ClaimRewardChoice.class, next.getNextGameState().getClass());

        //I'm setting firepower higher than the one of the card differently from before
        Smugglers smuggler = new Smugglers(new Stocks(1,2,3,1), 2,4,3,1, "TEST");
        CardVisProg_dynamicFirePower state= new CardVisProg_dynamicFirePower(game, players, message, nicks, 2, 3, 3);
        NextGameStateAndMessages follow = state.visit(smuggler);

        //->card draw
        assertEquals(CardDrawing.class, follow.getNextGameState().getClass());

        //adding diego
        ShipDashboard diego = mock(ShipDashboard.class);
        when(diego.getNickname()).thenReturn("diego");
        players.add(diego);
        NextGameStateAndMessages following = state.visit(smuggler);

        assertTrue(following.getPlayerMessage("diego").getFirst() instanceof TurnMessage);
        //in this case the following state doesn't change-> changes only the turn!!
    }

    @Test
    void testSlaversVis(){
        Message message = mock(Message.class);
        when(message.getNickname()).thenReturn("sara");

        //fp lower
        Slavers slavers = new Slavers(1, 2, 1, 5, 7, "TEST");
        CardVisProg_dynamicFirePower state= new CardVisProg_dynamicFirePower(game, players, message, nicks, 2, 3, 3);
        NextGameStateAndMessages follow = state.visit(slavers);

        assertEquals(ClaimRewardChoice.class, follow.getNextGameState().getClass());

        //firepower higher
        Slavers slaversMin = new Slavers(1, 8, 1, 5, 7, "TEST");
        CardVisProg_dynamicFirePower stat= new CardVisProg_dynamicFirePower(game, players, message, nicks, 2, 3, 3);
        NextGameStateAndMessages following = stat.visit(slaversMin);

        assertEquals(GiveUpCrewChoice.class, following.getNextGameState().getClass());
    }

    @Test
    void testPiratesVisitor(){
        Message message = mock(Message.class);
        when(message.getNickname()).thenReturn("sara");

        //fp lower
        Pirates pirates = new Pirates(3, 1, 3, 2, new ArrayList<Projectile>(), "TEST");
        CardVisProg_dynamicFirePower state= new CardVisProg_dynamicFirePower(game, players, message, nicks, 2, 3, 3);
        NextGameStateAndMessages follow = state.visit(pirates);

        assertEquals(ClaimRewardChoice.class, follow.getNextGameState().getClass());

        //fp higher
        Pirates piratesH= mock(Pirates.class);
        //mock in order to set the trajectory to -1
        when(piratesH.getNextShotTrajectory()).thenReturn(-1);
        CardVisProg_dynamicFirePower stateH= new CardVisProg_dynamicFirePower(game, players, message, nicks, 2, 3, -5);
        NextGameStateAndMessages followH = stateH.visit(piratesH);

        assertEquals(ManageProjectile.class, followH.getNextGameState().getClass());
    }
}
