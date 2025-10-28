package model.Cards.CardVisitorProgresser;

import Connections.Messages.Message;
import Controller.State.AddAndRearrangeStocks;
import Controller.State.CardDrawing;
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

public class CardVisProg_claimRewardChoiceTest {
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
    void testVisitSmugglersCard(){
        Message messagePos = mock(Message.class);
        when(messagePos.getNickname()).thenReturn("sara");
        //choice is positive!!
        when(messagePos.isClaimed()).thenReturn(true);

        Smugglers smugglers = mock(Smugglers.class);
        CardVisProg_claimRewardChoice state= new CardVisProg_claimRewardChoice(game, players, messagePos, nicks);

        NextGameStateAndMessages nextGameStateAndMessages = state.visit(smugglers);
        //so... next state-> add and rearrange stocks
        assertEquals(AddAndRearrangeStocks.class, nextGameStateAndMessages.getNextGameState().getClass());
        verify(sara).setDaysToMove(-smugglers.daysLoss);

        Message negMessage = mock(Message.class);
        when(negMessage.getNickname()).thenReturn("sara");
        //choice is negative
        when(negMessage.isClaimed()).thenReturn(false);

        CardVisProg_claimRewardChoice stateNeg = new CardVisProg_claimRewardChoice(game, players, negMessage, nicks);
        NextGameStateAndMessages next = stateNeg.visit(smugglers);
        //in this case next state is card drawing
        assertEquals(CardDrawing.class, next.getNextGameState().getClass());
    }

    @Test
    void testVisitSlaversCard(){
        Message messagePos = mock(Message.class);
        when(messagePos.getNickname()).thenReturn("sara");
        //choice is positive!
        when(messagePos.isClaimed()).thenReturn(true);

        Slavers slavers = mock(Slavers.class);
        CardVisProg_claimRewardChoice state= new CardVisProg_claimRewardChoice(game, players, messagePos, nicks);

        NextGameStateAndMessages nextGameStateAndMessages = state.visit(slavers);
        //next state->card drawing
        assertEquals(CardDrawing.class, nextGameStateAndMessages.getNextGameState().getClass());
        verify(sara).addCredits(slavers.creditsGained);
        verify(sara).setDaysToMove(-slavers.daysLoss);


        Message negMessage = mock(Message.class);
        when(negMessage.getNickname()).thenReturn("sara");
        //choice is negative
        when(negMessage.isClaimed()).thenReturn(false);

        CardVisProg_claimRewardChoice stateNeg = new CardVisProg_claimRewardChoice(game, players, negMessage, nicks);
        NextGameStateAndMessages next = stateNeg.visit(slavers);
        //even in this case next state is card drawing
        assertEquals(CardDrawing.class, next.getNextGameState().getClass());
    }

    @Test
    void testVisitPiratesCard(){
        Message messagePos = mock(Message.class);
        when(messagePos.getNickname()).thenReturn("sara");
        //choice is positive!
        when(messagePos.isClaimed()).thenReturn(true);

        Pirates pirates = mock(Pirates.class);
        CardVisProg_claimRewardChoice state= new CardVisProg_claimRewardChoice(game, players, messagePos, nicks);

        NextGameStateAndMessages nextGameStateAndMessages = state.visit(pirates);
        //next state->card drawing
        assertEquals(CardDrawing.class, nextGameStateAndMessages.getNextGameState().getClass());
        verify(sara).addCredits(pirates.creditsGained);
        verify(sara).setDaysToMove(-pirates.daysLoss);


        Message negMessage = mock(Message.class);
        when(negMessage.getNickname()).thenReturn("sara");
        //choice is negative
        when(negMessage.isClaimed()).thenReturn(false);

        CardVisProg_claimRewardChoice stateNeg = new CardVisProg_claimRewardChoice(game, players, negMessage, nicks);
        NextGameStateAndMessages next = stateNeg.visit(pirates);
        //even in this case next state is card drawing
        assertEquals(CardDrawing.class, next.getNextGameState().getClass());
    }

}
