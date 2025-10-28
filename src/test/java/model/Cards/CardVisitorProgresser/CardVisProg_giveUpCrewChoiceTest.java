package model.Cards.CardVisitorProgresser;

import Connections.Messages.UpdateServerShipMessage;
import Controller.State.CardDrawing;
import Controller.State.FirePowerChoice;
import model.Cards.AbandonedShip;
import model.Cards.CombatZone;
import model.Cards.Enemies.Slavers;
import model.Game;
import model.NextGameStateAndMessages;
import model.ShipDashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CardVisProg_giveUpCrewChoiceTest {
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
    void testVisitCombatZone(){
        UpdateServerShipMessage message = mock(UpdateServerShipMessage.class);
        ShipDashboard updateSara = mock(ShipDashboard.class);
        when(message.getUpdatedShip()).thenReturn(updateSara);
        CombatZone combatZone = mock(CombatZone.class);
        CardVisProg_giveUpCrewChoice curr= new CardVisProg_giveUpCrewChoice(game, players, message, nicks);
        NextGameStateAndMessages next = curr.visit(combatZone);

        //combat zone applied?
        verify(combatZone).setAffectedPlayerIndex(-1);
        verify(combatZone).setLowestMotorPower(-1);
        //next state correct?
        assertEquals(FirePowerChoice.class, next.getNextGameState().getClass());
    }

    @Test
    void testAbandonedVisit(){
        UpdateServerShipMessage message = mock(UpdateServerShipMessage.class);
        ShipDashboard updateSara = mock(ShipDashboard.class);
        when(message.getUpdatedShip()).thenReturn(updateSara);
        when(message.getNickname()).thenReturn("sara");
        CardVisProg_giveUpCrewChoice actual= new CardVisProg_giveUpCrewChoice(game, players, message, nicks);
        AbandonedShip abandonedShip = mock(AbandonedShip.class);
        NextGameStateAndMessages next = actual.visit(abandonedShip);

        assertEquals(CardDrawing.class, next.getNextGameState().getClass());
    }

    @Test
    void testSlaversVisit(){
        UpdateServerShipMessage messageP = mock(UpdateServerShipMessage.class);
        when(messageP.getNickname()).thenReturn("sara");
        ShipDashboard updateSara = mock(ShipDashboard.class);
        when(messageP.getUpdatedShip()).thenReturn(updateSara);
        CardVisProg_giveUpCrewChoice act = new CardVisProg_giveUpCrewChoice(game, players, messageP, nicks);
        Slavers slavers = mock(Slavers.class);
        NextGameStateAndMessages next = act.visit(slavers);

        //only sara-> sara is last player-> next state is card drawing!
        assertEquals(CardDrawing.class, next.getNextGameState().getClass());

        //adding 2 players in order to check what happens in case of not last player
        ShipDashboard diego= mock(ShipDashboard.class);
        when(diego.getNickname()).thenReturn("diego");
        nicks.add("diego");
        players.add(diego);
        ShipDashboard updateDiego = mock(ShipDashboard.class);
        when(updateDiego.getNickname()).thenReturn("diego");
        UpdateServerShipMessage messageNotL = mock(UpdateServerShipMessage.class);
        ShipDashboard giulio = mock(ShipDashboard.class);
        players.add(giulio);
        ShipDashboard updateGiulio = mock(ShipDashboard.class);
        when(messageNotL.getUpdatedShip()).thenReturn(updateGiulio);
        when(messageNotL.getNickname()).thenReturn("diego");
        CardVisProg_giveUpCrewChoice cur= new CardVisProg_giveUpCrewChoice(game, players, messageNotL, nicks);
        NextGameStateAndMessages following= cur.visit(slavers);

        //firepower next?
        assertEquals(FirePowerChoice.class, following.getNextGameState().getClass());

    }
}
