package model.Cards.CardVisitorProgresser;

import Connections.Messages.CardActivationRequestMessage;
import Controller.State.AddAndRearrangeStocks;
import Controller.State.CardDrawing;
import Controller.State.DockingChoice;
import Controller.State.GiveUpCrewChoice;
import model.Cards.AbandonedShip;
import model.Cards.AbandonedStation;
import model.Game;
import model.NextGameStateAndMessages;
import model.ShipDashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CardVisProg_dockingChoiceTest {
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

    @Test void testVisitAbandonedCard(){
        CardActivationRequestMessage messagePos = mock(CardActivationRequestMessage.class);
        when(messagePos.getNickname()).thenReturn("sara");
        //choice is positive!!
        when(messagePos.isYes()).thenReturn(true);

        AbandonedShip abandonedShip = mock(AbandonedShip.class);

        CardVisProg_dockingChoice state = new CardVisProg_dockingChoice(game, players, messagePos, nicks);
        NextGameStateAndMessages nextGameStateAndMessages = state.visit(abandonedShip);
        //next state is -> Give up crew!
        assertEquals(GiveUpCrewChoice.class, nextGameStateAndMessages.getNextGameState().getClass());
        //bonus malus applied?
        verify(abandonedShip).bonusMalus(sara);

        //refused and last player
        CardActivationRequestMessage messageNegAndLast = mock(CardActivationRequestMessage.class);
        when(messageNegAndLast.getNickname()).thenReturn("sara");
        when(messageNegAndLast.isYes()).thenReturn(false);

        CardVisProg_dockingChoice stateLast = new CardVisProg_dockingChoice(game, players, messageNegAndLast, nicks);
        NextGameStateAndMessages next = stateLast.visit(abandonedShip);
        //CHANGES state to card drawing because there aren't following players!
        assertEquals(CardDrawing.class, next.getNextGameState().getClass());

        //refused and not last
        ShipDashboard giulio = mock(ShipDashboard.class);
        when(giulio.getNickname()).thenReturn("giulio");
        ShipDashboard diego = mock(ShipDashboard.class);
        when(diego.getNickname()).thenReturn("diego");
        players.add(giulio);
        nicks.add("giulio");
        players.add(diego);
        nicks.add("diego");

        CardActivationRequestMessage messageNegAndNotLast = mock(CardActivationRequestMessage.class);
        when(messageNegAndNotLast.getNickname()).thenReturn("giulio");
        when(messageNegAndNotLast.isYes()).thenReturn(false);
        CardVisProg_dockingChoice stateNotLast= new CardVisProg_dockingChoice(game, players, messageNegAndNotLast, nicks);
        NextGameStateAndMessages nextState= stateNotLast.visit(abandonedShip);
        assertEquals(DockingChoice.class, nextState.getNextGameState().getClass());
    }

    @Test
    void testVisitAbandonedStation(){
        //POS case
        CardActivationRequestMessage messagePos = mock(CardActivationRequestMessage.class);
        when(messagePos.getNickname()).thenReturn("sara");
        when(messagePos.isYes()).thenReturn(true);
        AbandonedStation abandonedStation = mock(AbandonedStation.class);

        CardVisProg_dockingChoice state= new CardVisProg_dockingChoice(game, players, messagePos, nicks);
        NextGameStateAndMessages nextState= state.visit(abandonedStation);
        assertEquals(AddAndRearrangeStocks.class, nextState.getNextGameState().getClass());

        //refused and last player
        CardActivationRequestMessage messageNegAndLast = mock(CardActivationRequestMessage.class);
        when(messageNegAndLast.getNickname()).thenReturn("sara");
        when(messageNegAndLast.isYes()).thenReturn(false);

        CardVisProg_dockingChoice stateLast = new CardVisProg_dockingChoice(game, players, messageNegAndLast, nicks);
        NextGameStateAndMessages next = stateLast.visit(abandonedStation);
        //CHANGES state to card drawing because there aren't following players!
        assertEquals(CardDrawing.class, next.getNextGameState().getClass());

        //refused and not last
        ShipDashboard giulio = mock(ShipDashboard.class);
        when(giulio.getNickname()).thenReturn("giulio");
        ShipDashboard diego = mock(ShipDashboard.class);
        when(diego.getNickname()).thenReturn("diego");
        players.add(giulio);
        nicks.add("giulio");
        players.add(diego);
        nicks.add("diego");

        CardActivationRequestMessage messageNegAndNotLast = mock(CardActivationRequestMessage.class);
        when(messageNegAndNotLast.getNickname()).thenReturn("giulio");
        when(messageNegAndNotLast.isYes()).thenReturn(false);
        CardVisProg_dockingChoice stateNotLast= new CardVisProg_dockingChoice(game, players, messageNegAndNotLast, nicks);
        NextGameStateAndMessages followState= stateNotLast.visit(abandonedStation);
        assertEquals(DockingChoice.class, followState.getNextGameState().getClass());

    }

}
