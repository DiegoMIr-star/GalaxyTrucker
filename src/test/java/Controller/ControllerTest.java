package Controller;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Connections.Messages.*;
import Controller.State.*;
import View.VirtualView;
import model.*;
import model.Cards.Card;
import model.Cards.CardVisitorProgresser.*;
import model.Cards.Planets;
import model.exceptions.NoSuchPlayerException;
import model.exceptions.UnexpectedMessageException;
import model.exceptions.WrongCardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class ControllerTest {
    private Controller controller;

    //set up before testing
    @BeforeEach
    void setUp() throws IOException {
        controller=new Controller();
    }

    //right initial state?
    @Test
    void testInitial(){
        assertEquals("InitializationState", controller.getState().getClass().getSimpleName());
    }

    @Test
    void testNickname() throws IOException {
        //mock of virtual view
        VirtualView sara = mock(VirtualView.class);

        controller.manageLogin("sara", sara);
        assertTrue(controller.checkNickname("sara"));
        assertFalse(controller.checkNickname("diego"));

        //what if I add a new player?
        VirtualView diego = mock(VirtualView.class);
        controller.manageLogin("diego", diego);
        assertTrue(controller.checkNickname("diego"));
        assertTrue(controller.checkNickname("sara"));
        assertTrue(controller.getVirtualViews().contains(sara));

        controller.getNewGameVirtualViews().put("sara", sara);
        controller.setWeAreInTheEndGameNow(true);
        assertTrue(controller.checkNickname("sara"));


    }

    @Test
    void testManageLog() throws IOException {
        VirtualView sara = mock(VirtualView.class);
        controller.manageLogin("sara", sara);

        //check if is correctly sent the request
        verify(sara).sendNumPlayersRequest();

        //check that is not sent notify turn because sara is first player
        verify(sara, never()).notifyTurn();

        controller.setNumPlayers(2);
        //adding a new player
        VirtualView diego = mock(VirtualView.class);
        controller.manageLogin("diego", diego);

        // 2 times: 1 for manageLogin and 1 for checkWaitingPlayers
        verify(diego, times(2)).notifyTurn();
        verify(sara, times(2)).notifyTurn();

        controller.setWeAreInTheEndGameNow(true);
        controller.manageLogin("sara", sara);
        controller.getNewGameVirtualViews().containsKey("sara");
    }

    @Test
    void testDisconnection() throws IOException {
        VirtualView sara = mock(VirtualView.class);
        controller.manageLogin("sara", sara);
        ShipDashboard saraShip = mock(ShipDashboard.class);
        when(saraShip.getNickname()).thenReturn("sara");
        //creates array of player and adds sara
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(saraShip);
        controller.setPlayers(players);
        //is present?
        assertTrue(controller.checkNickname("sara"));

        controller.disconnectPlayer("sara");
        //is still present?
        assertFalse(controller.checkNickname("sara"));
    }

    @Test
    void testChangeState() throws IOException {
        VirtualView sara = mock(VirtualView.class);
        controller.manageLogin("sara", sara);
        VirtualView diego = mock(VirtualView.class);
        controller.manageLogin("diego", diego);

        //creates a new state
        GameState state = new ShipConstructionState();

        controller.changeGameStateAndNotifyClients(state);
        //check changes notifications
        verify(sara).notifyNewGameState(state);
        verify(diego).notifyNewGameState(state);
        //check if the state is effectively changed
        assertEquals(controller.getState(), state);
    }

    @Test
    void testFindSender() {
        Message message = mock(Message.class);

        //players are null -> returns zero
        assertEquals(0, controller.findSenderTest(message));

        when(message.getNickname()).thenReturn("diego");
        ShipDashboard sara = mock(ShipDashboard.class);
        ShipDashboard diego = mock(ShipDashboard.class);
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        players.add(diego);
        controller.setPlayers(players);
        when(sara.getNickname()).thenReturn("sara");
        when(diego.getNickname()).thenReturn("diego");

        //expects 1 because sara is in pos 0 and diego in 1 and the sender is diego
        assertEquals(1, controller.findSenderTest(message));

        //testing the exception <- giulio is not a player
        when(message.getNickname()).thenReturn("giulio");
        controller.setWeAreInTheEndGameNow(false);

        //message is also correct?
        assertTrue(assertThrows(NoSuchPlayerException.class, () -> controller.findSenderTest(message)).getMessage().contains("There is no player with the nickname giulio found in the message."));
    }

    //linked with findSender
    @Test
    void testUpdateIndex(){
        Message message = mock(Message.class);
        VirtualView sara = mock(VirtualView.class);
        VirtualView diego = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", sara);
        controller.getVirtualViews().put("diego", diego);
        when(message.getNickname()).thenReturn("diego");

        controller.updateServerIndexTest(message);

        //diego's index is 1
        assertEquals(1, controller.getSenderIndex());

        //trying with beginning false
        controller.setBeginning(false);
        //for example if we are at the start of the game-> the array of players is empty
        ArrayList<ShipDashboard> players = new ArrayList<>();
        controller.setPlayers(players);

        //-> findSender -> diego is not present  -> exception like before!
        when(message.getNickname()).thenReturn("diego");
        assertThrows(NoSuchPlayerException.class, () -> controller.updateServerIndexTest(message));
    }

    @Test
    void testHandleToBeFixedAndFixing() throws IOException {
        //not Place request message
        Message otherMes = mock(Message.class);
        when(otherMes.getNickname()).thenReturn("diego");
        when(otherMes.getKind()).thenReturn(MessageKind.CREW_REQUEST);
        controller.handleToBeFixedAndFixingShips(otherMes, false);
        //it is still in initialization state!
        assertEquals(InitializationState.class, controller.getState().getClass());

        VirtualView sara = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", sara);

        ShipDashboard saraShip = mock(ShipDashboard.class);
        when(saraShip.getNickname()).thenReturn("sara");
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(saraShip);
        controller.setPlayers(players);
        //sara has finished the ship and wants to place it
        controller.setFixedShips(new ConcurrentHashMap<>());

        //->place request
        PlaceShipRequestMessage message=mock(PlaceShipRequestMessage.class);
        when(message.getKind()).thenReturn(MessageKind.PLACE_SHIP_REQUEST);
        when(message.getNickname()).thenReturn("sara");
        when(message.getShip()).thenReturn(saraShip);

        controller.handleToBeFixedAndFixingShips(message, false);
        //all the players finished->next state is Card Drawing
        assertEquals(CardDrawing.class, controller.getState().getClass());
        verify(sara).notifyNewGameState(controller.getState());

    }

    @Test
    void testDidPlayerFinishShip(){
        //case: players null
        controller.setPlayers(null);
        //sara hasn't finished
        assertFalse(controller.didPlayerFinishShip("sara"));

        //adding sara
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        controller.setPlayers(players);

        //sara finished
        assertTrue(controller.didPlayerFinishShip("sara"));
        //diego has not finished the ship yet
        assertFalse(controller.didPlayerFinishShip("diego"));
    }

    @Test
    void testAllShipsFixed(){
        ShipDashboard saraShip = mock(ShipDashboard.class);
        ShipDashboard diegoShip = mock(ShipDashboard.class);
        when(saraShip.getNickname()).thenReturn("sara");
        when(diegoShip.getNickname()).thenReturn("diego");
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(saraShip);
        players.add(diegoShip);
        controller.setPlayers(players);
        //all ships are fixed
        ConcurrentHashMap<String,Boolean> map = new ConcurrentHashMap<>();
        controller.setFixedShips(map);
        map.put(controller.getPlayers().get(0).getNickname(),true);
        map.put(controller.getPlayers().get(1).getNickname(),true);
        assertTrue(controller.testAllShipsAreFixed());

        map.clear();
        //there's a ship not fixed yet
        map.put(controller.getPlayers().get(0).getNickname(),true);
        map.put(controller.getPlayers().get(1).getNickname(),false);
        assertFalse(controller.testAllShipsAreFixed());
    }

    @Test
    void testIsGameBlocked(){
        //not blocked di default
        assertFalse(controller.isGameBlocked());

        //blocked
        controller.setBlockGame(true);
        assertTrue(controller.isGameBlocked());
        //testing also setBlockGame to be sure
        controller.setBlockGame(false);
        assertFalse(controller.isGameBlocked());
    }

    //test both cases
    @Test
    void testHandleMotorPowerChoice(){
        ShipDashboard sara = mock(ShipDashboard.class);
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        controller.setPlayers(players);

        //updated ship
        ShipDashboard saraShip = mock(ShipDashboard.class);
        UpdateServerShipMessage updateServerShipMessage = mock(UpdateServerShipMessage.class);
        when(updateServerShipMessage.getUpdatedShip()).thenReturn(saraShip);

        controller.handleMotorPowerChoice(updateServerShipMessage, false);
        verify(sara).updateShip(saraShip.getShip());

        //case dynamic power mess
        Card card =mock(Card.class);
        controller.setCurCard(card);
        DynamicMotorPowerMessage dynamicMotorPowerMessage = mock(DynamicMotorPowerMessage.class);

        NextGameStateAndMessages nextGameStateAndMessages = mock(NextGameStateAndMessages.class);
        when(card.apply(any(CardVisProg_dynamicMotorPower.class))).thenReturn(nextGameStateAndMessages);

        controller.handleMotorPowerChoice(dynamicMotorPowerMessage, false);
        //visitor applied?
        verify(card).apply(any(CardVisProg_dynamicMotorPower.class));

        //exception
        Message notValidMess= mock(Message.class);
        when(notValidMess.getKind()).thenReturn(MessageKind.PLANET_LAND_REQUEST);
        assertThrows(WrongCardException.class, () -> controller.handleMotorPowerChoice(notValidMess, false));

    }

    @Test
    void testHandleFirePower(){
        ShipDashboard sara = mock(ShipDashboard.class);
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        controller.setPlayers(players);

        //update ship mess
        ShipDashboard saraShip = mock(ShipDashboard.class);
        UpdateServerShipMessage updateServerShipMessage = mock(UpdateServerShipMessage.class);
        when(updateServerShipMessage.getUpdatedShip()).thenReturn(saraShip);

        controller.handleFirePowerChoice(updateServerShipMessage, false);
        verify(sara).updateShip(saraShip.getShip());

        //dynamic fire mess
        Card card=mock(Card.class);
        controller.setCurdCard(card);
        DynamicFirePowerMessage dynamicFirePowerMessage = mock(DynamicFirePowerMessage.class);
        NextGameStateAndMessages nextGameStateAndMessages = mock(NextGameStateAndMessages.class);
        when(card.apply(any(CardVisProg_dynamicFirePower.class))).thenReturn(nextGameStateAndMessages);

        controller.handleFirePowerChoice(dynamicFirePowerMessage, false);
        //visitor applied
        verify(card).apply(any(CardVisProg_dynamicFirePower.class));

        //exception
        Message notValidMess= mock(Message.class);
        when(notValidMess.getKind()).thenReturn(MessageKind.PLANET_LAND_REQUEST);
        assertThrows(UnexpectedMessageException.class, () -> controller.handleFirePowerChoice(notValidMess, false));
    }

    @Test
    void testHandleManageProjectiles(){
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        controller.setPlayers(players);

        Card card = mock(Card.class);
        controller.setCurdCard(card);

        //update of ship sara
        ShipDashboard saraShip = mock(ShipDashboard.class);
        UpdateServerShipMessage updateServerShipMessage = mock(UpdateServerShipMessage.class);
        when(updateServerShipMessage.getUpdatedShip()).thenReturn(saraShip);

        NextGameStateAndMessages nextGameStateAndMessages = mock(NextGameStateAndMessages.class);
        when(card.apply(any(CardVisProg_manageProjectile.class))).thenReturn(nextGameStateAndMessages);

        controller.handleManageProjectile(updateServerShipMessage, false);
        //correctly updated!
        verify(sara).updateShip(saraShip.getShip());
        //visitor applied!
        verify(card).apply(any(CardVisProg_manageProjectile.class));

        //exception
        Message notValidMess= mock(Message.class);
        //type of message not correct
        when(notValidMess.getKind()).thenReturn(MessageKind.PLANET_LAND_REQUEST);
        assertThrows(WrongCardException.class, () -> controller.handleManageProjectile(notValidMess, false));

    }

    @Test
    void testHandleGiveUpCrewChoice(){
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        controller.setPlayers(players);

        Card card = mock(Card.class);
        controller.setCurdCard(card);

        UpdateServerShipMessage message= mock(UpdateServerShipMessage.class);
        NextGameStateAndMessages nextGameStateAndMessages = mock(NextGameStateAndMessages.class);
        when(card.apply(any(CardVisProg_giveUpCrewChoice.class))).thenReturn(nextGameStateAndMessages);

        controller.handleGiveUpCrewChoice(message, false);
        verify(card).apply(any(CardVisProg_giveUpCrewChoice.class));
    }

    @Test
    void testHandlePlanetsLandingChoice() throws IOException {
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        controller.setPlayers(players);
        VirtualView view = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", view);

        Planets planets = mock(Planets.class);
        controller.setCurCard(planets);
        PlanetLandRequestMessage mes= mock(PlanetLandRequestMessage.class);
        when(mes.getPlanetId()).thenReturn(3);
        when(mes.getKind()).thenReturn(MessageKind.PLANET_LAND_REQUEST);

        controller.handlePLANETS_LandingChoice(mes, false);
        //sara lands on planet 3
        verify(planets).land(3, sara);

        ShipDashboard giulio = mock(ShipDashboard.class);
        when(giulio.getNickname()).thenReturn("giulio");
        players.add(giulio);
        VirtualView viewGiulio = mock(VirtualView.class);
        controller.getVirtualViews().put("giulio", viewGiulio);
        PlanetLandRequestMessage mess= mock(PlanetLandRequestMessage.class);
        //check not landing in case of negative index
        when(mess.getPlanetId()).thenReturn(-2);
        when(mess.getKind()).thenReturn(MessageKind.PLANET_LAND_REQUEST);
        controller.handlePLANETS_LandingChoice(mess, false);
        verify(planets, never()).land(-2, giulio);

        //case not free planets available
        when(planets.getFreePlanets()).thenReturn(null);
        Game game = mock(Game.class);
        controller.setGame(game);
        controller.testPLANETSCheckIfCardsEnd(planets);
        verify(view).notifyTurn();


        //exception
        Message notValidMess=mock(Message.class);
        //not message of landing
        when(notValidMess.getKind()).thenReturn(MessageKind.GENERIC);
        assertThrows(WrongCardException.class, ()->controller.handlePLANETS_LandingChoice(notValidMess, false));
    }

    @Test
    void testHandleInitializationState() throws IOException {
        //response of number of players
        PlayersNumResponseMessage message = mock(PlayersNumResponseMessage.class);
        //1 player
        when(message.getNumPlayersRequested()).thenReturn(1);
        when(message.getNickname()).thenReturn("sara");
        when(message.getKind()).thenReturn(MessageKind.PLAYERS_NUM_RESPONSE);

        controller.handleInitializationState(message, false);
        assertEquals(1, controller.getNumPlayers());

        //end game case->
        controller.setWeAreInTheEndGameNow(true);
        EndGameMessage endGameMessage = mock(EndGameMessage.class);
        when(endGameMessage.getKind()).thenReturn(MessageKind.END_GAME_REQUEST);
        controller.handleInitializationState(endGameMessage, false);

        //no effect-> is still 1
        assertEquals(1, controller.getNumPlayers());

        //none of the previous mess->
        Message notValidMess=mock(Message.class);
        when(notValidMess.getKind()).thenReturn(MessageKind.GENERIC);
        controller.handleInitializationState(notValidMess, false);

        //no effect-> is still 1
        assertEquals(1, controller.getNumPlayers());

        //case in the same match there's two time the message of choosing num of players
        //num players is set
        controller.setNumPlayers(2);
        PlayersNumResponseMessage mess= mock(PlayersNumResponseMessage.class);
        //new request of setting it!
        when(mess.getNumPlayersRequested()).thenReturn(1);
        when(mess.getNickname()).thenReturn("sara");
        VirtualView view = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", view);
        controller.handleInitializationState(mess, false);

        //is still 2
        assertEquals(2, controller.getNumPlayers());
    }

    @Test
    void testHandlePlayerNumStolen() throws IOException {
        controller.setNumPlayers(2);
        PlayersNumResponseMessage mess= mock(PlayersNumResponseMessage.class);
        //new request of setting it!
        when(mess.getNumPlayersRequested()).thenReturn(1);
        when(mess.getNickname()).thenReturn("sara");
        VirtualView view = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", view);
        controller.handlePlayerNumChoiceStolen(mess);

        //is still 2
        assertEquals(2, controller.getNumPlayers());
        verify(view).sendGenericMessage("A player quicker than you has stolen your right to choose the number of players and picked " + controller.getNumPlayers() + ".");
    }

    @Test
    void testHandleEndGame(){
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        controller.setPlayers(players);
        VirtualView view = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", view);

        EndGameMessage message = mock(EndGameMessage.class);
        when(message.getKind()).thenReturn(MessageKind.END_GAME_REQUEST);
        //abandon match-> choice 1
        when(message.getChoice()).thenReturn(1);
        when(message.getNickname()).thenReturn("sara");

        controller.handleEndGame(message, false);

        //disconnected?
        assertFalse(controller.getVirtualViews().containsKey("sara"));

        //choice 2
        when(message.getChoice()).thenReturn(2);
        controller.handleEndGame(message, false);
        assertTrue(controller.isWeAreInTheEndGameNow());

        //not valid mess
        controller.setWeAreInTheEndGameNow(false);
        controller.getVirtualViews().put("sara", view);
        when(message.getKind()).thenReturn(MessageKind.GENERIC);
        controller.handleEndGame(message, false);
        //not in choice 2
        assertFalse(controller.isWeAreInTheEndGameNow());
        //not in choice 1-> sara still connected
        assertTrue(controller.getVirtualViews().containsKey("sara"));
    }

    @Test
    void testHandleMessageController() throws IOException {
        //case handle resilience
        ResilienceResponseMessage message = mock(ResilienceResponseMessage.class);
        when(message.getNickname()).thenReturn("sara");
        //resilience positive (response ok)
        when(message.getKind()).thenReturn(MessageKind.RES_RESP);

        controller.handleMessageController(message);
        //sara is active
        assertTrue(controller.getResilientActivation().get("sara"));

        //generic scenario
        Message mess= mock(Message.class);
        when(mess.getKind()).thenReturn(MessageKind.GENERIC);
        GameState state = mock(GameState.class);
        controller.setGameState(state);
        //valid message
        when(state.checkMessage(any())).thenReturn(true);
        controller.handleMessageController(mess);
        //->accepted
        verify(state).accept(any(ControllerStateVisitor.class));

        //not valid mess->exception
        when(controller.getState().checkMessage(any(ControllerStateCheckerVisitor.class))).thenReturn(false);
        assertThrows(UnexpectedMessageException.class, ()->controller.handleMessageController(mess));
    }

    @Test
    void testHandleClaimRewardChoice() {
        Card card = mock(Card.class);
        controller.setCurdCard(card);
        Message message = mock(Message.class);
        when(card.apply(any(CardVisProg_claimRewardChoice.class))).thenReturn(mock(NextGameStateAndMessages.class));

        controller.handleClaimRewardChoice(message, false);

        verify(card).apply(any(CardVisProg_claimRewardChoice.class));
    }

    @Test
    void testIsLost(){
        controller.getLostPlayers().add("sara");

        assertTrue(controller.isLost("sara"));
        assertFalse(controller.isLost("diego"));
    }

    @Test
    void testSetResilience(){
        ShipDashboard sara = mock(ShipDashboard.class);
        ShipConstructionState constructionState = mock(ShipConstructionState.class);
        controller.setGameState(constructionState);
        controller.setResilience("sara", sara);

        assertTrue(controller.getConstructionShip().contains(sara));
        assertTrue(controller.getResilientActivation().get("sara"));
    }

    @Test
    void testStopResilience(){
        //waiting for resilience
        controller.setResilientActivation(true);
        ScheduledFuture<?> task = mock(ScheduledFuture.class);
        //setting timer
        controller.setTaskHandle(task);

        //stop resilience-> not waiting anymore
        controller.stopResilience();

        verify(task).cancel(true);
        assertFalse(controller.getActivation());
    }

    @Test
    void testHandleDockingChoice(){
        CardActivationRequestMessage message = mock(CardActivationRequestMessage.class);
        Card card = mock(Card.class);
        controller.setCurdCard(card);

        NextGameStateAndMessages nextGameStateAndMessages = mock(NextGameStateAndMessages.class);
        //setting following state
        when(card.apply(any(CardVisProg_dockingChoice.class))).thenReturn(nextGameStateAndMessages);

        controller.handleDockingChoice(message, false);

        //visitor?
        verify(card).apply(any(CardVisProg_dockingChoice.class));
    }

    @Test
    void testHandleStocks() throws IOException {
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        ShipDashboard diego = mock(ShipDashboard.class);
        when(diego.getNickname()).thenReturn("diego");
        VirtualView view = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", view);
        VirtualView viewDiego = mock(VirtualView.class);
        controller.getVirtualViews().put("diego", viewDiego);
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        players.add(diego);
        controller.setPlayers(players);

        //updated ship with stocks
        ShipDashboard updatedSara = mock(ShipDashboard.class);
        UpdateServerShipMessage updateServerShipMessage = mock(UpdateServerShipMessage.class);
        when(updateServerShipMessage.getUpdatedShip()).thenReturn(updatedSara);

        //planets card case->
        Planets card =mock(Planets.class);
        controller.setCurCard(card);
        when(card.getFreePlanets()).thenReturn(new ArrayList<>(List.of(1,2)));

        controller.handleAddAndRearrangeStocks(updateServerShipMessage, false);

        //updates the ship correctly?
        verify(sara).updateShip(updatedSara.getShip());
        //correct new state?
        assertTrue("PLANETS_LandingChoice state" == controller.getState().toString());

        //case: stocks to discard
        DiscardStocksMessage discardStocksMessage = mock(DiscardStocksMessage.class);
        Stocks stocksDiscard = mock(Stocks.class);
        //takes stocks to discard
        when(discardStocksMessage.getStocksToDiscard()).thenReturn(stocksDiscard);

        Game game = mock(Game.class);
        controller.setGame(game);

        controller.handleAddAndRearrangeStocks(discardStocksMessage, false);
        //gives back stocks
        verify(game).returnStocks(stocksDiscard);

        //->exception
        Message excMsg = mock(Message.class);
        when(excMsg.getKind()).thenReturn(MessageKind.GENERIC);
        assertThrows(UnexpectedMessageException.class, () -> controller.handleAddAndRearrangeStocks(excMsg, false));
    }

    @Test
    void testPlayerEliminated(){
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");

        VirtualView view = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", view);
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        controller.setPlayers(players);

        //eliminates player based on nickname (differently from the following method that works for index)
        controller.eliminatePlayerTester("sara");

        assertFalse(controller.getVirtualViews().containsKey("sara"));
        assertFalse(controller.getPlayers().contains(sara));

    }

    @Test
    void testEliminatedPlayers(){
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        VirtualView view = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", view);
        ShipDashboard diego = mock(ShipDashboard.class);
        when(diego.getNickname()).thenReturn("diego");
        VirtualView viewDiego = mock(VirtualView.class);
        controller.getVirtualViews().put("diego", viewDiego);
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        players.add(diego);
        controller.setPlayers(players);

        //diego is in pos 1->eliminated
        controller.eliminatePlayersIndex(new ArrayList<>(List.of(1)));

        assertFalse(controller.getVirtualViews().containsKey("diego"));
        assertFalse(controller.getPlayers().contains(diego));
        //sara is still present
        assertTrue(controller.getVirtualViews().containsKey("sara"));
        assertTrue(controller.getPlayers().contains(sara));
    }

    @Test
    void testHandleShipConstruction(){
        Message message = mock(Message.class);
        NextGameStateAndMessages nextGameStateAndMessages = mock(NextGameStateAndMessages.class);
        when(message.accept(any(MsgVisProg_shipConstructionState.class))).thenReturn(nextGameStateAndMessages);

        Persistence persistence = mock(Persistence.class);
        controller.setPersistence(persistence);

        controller.handleShipConstructionState(message, false);

        //visitor applied and persistence saved properly after the modification
        verify(message).accept(any(MsgVisProg_shipConstructionState.class));
        verify(controller.getPersistence()).save(controller);
    }

    @Test
    void testNotifyClientsTimer() throws IOException {
        VirtualView view = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", view);

        //if is last is true it works
        controller.notifyClientsTimerExpiredTest(true);
        verify(view).timerExpired(true);

        //even in the case is false clients are notified
        controller.notifyClientsTimerExpiredTest(false);
        verify(view).timerExpired(false);

        //->exception check
        doThrow(new IOException()).when(view).timerExpired(true);
        assertThrows(RuntimeException.class, () -> controller.notifyClientsTimerExpiredTest(true));
    }

    @Test
    void testHandleCardDrawing() {
        VirtualView view = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", view);
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        controller.setPlayers(players);
        Message message = mock(Message.class);
        when(message.getKind()).thenReturn(MessageKind.DRAW_CARD_REQUEST);
        when(message.getNickname()).thenReturn("sara");
        Card card = mock(Card.class);
        Deck deck = mock(Deck.class);
        Game game = mock(Game.class);
        when(game.getTotalDeck()).thenReturn(deck);
        when(deck.drawCard()).thenReturn(card);
        //mock the following state
        when(card.apply(any(CardVisProg_cardDrawingState.class))).thenReturn(mock(NextGameStateAndMessages.class));
        controller.setGame(game);
        Persistence persistence = mock(Persistence.class);
        controller.setPersistence(persistence);

        controller.handleCardDrawing(message, false);

        //draw of the card happened
        verify(view).notifyNewCardDrawn(card);
        verify(deck).drawCard();
        //saved state correctly
        verify(controller.getPersistence()).save(controller);
        //visitor applied for the card
        verify(card).apply(any(CardVisProg_cardDrawingState.class));

        Message exceptMess= mock(Message.class);
        when(exceptMess.getKind()).thenReturn(MessageKind.DRAW_CARD_REQUEST);
        when(exceptMess.getNickname()).thenReturn("giulio");
        ShipDashboard giulio = mock(ShipDashboard.class);
        //adding giulio (not first player-> not allowed to draw)
        players.add(giulio);
        when(giulio.getNickname()).thenReturn("giulio");
        controller.setPlayers(players);

        //->exception
        assertEquals("A player who isn't first has tried to draw a card.", assertThrows(RuntimeException.class, () -> controller.handleCardDrawing(exceptMess, false)).getMessage());
    }

    @Test
    void testSearchForPersistence(){
        controller.setAllPlayers(new ArrayList<>(List.of("sara", "diego")));

        Game game = mock(Game.class);
        controller.setGame(game);

        controller.searchPlayersForPersistence();

        //clone->all players are considered "lost"
        assertEquals(List.of("sara", "diego"), controller.getLostPlayers());
        //both sara and diego are still waiting to be reconnected!!
        assertFalse(controller.getResilientActivation().get("sara"));
        assertFalse(controller.getVirtualViews().containsKey("sara"));
        assertFalse(controller.getVirtualViews().containsKey("diego"));
        assertFalse(controller.getResilientActivation().get("diego"));
        //persistence and timer for persistence have started
        verify(game).setTimerPersistence();
        assertNotNull(controller.getPersistence());
    }

    @Test
    void testInsertLost() {
        Message messageLast = mock(Message.class);
        when(messageLast.getNickname()).thenReturn("sara");
        controller.getLastMessage().put("sara", messageLast);
        Card card = mock(Card.class);
        controller.setCurCard(card);
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        VirtualView view = mock(VirtualView.class);
        when(sara.clone()).thenReturn(mock(ShipDashboard.class));

        //sara normally present at the start
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        controller.setPlayers(players);

        //sara is constructing her ship when...
        ShipConstructionState shipConstructionState = mock(ShipConstructionState.class);
        controller.setGameState(shipConstructionState);
        ShipDashboard building = mock(ShipDashboard.class);
        when(building.getNickname()).thenReturn("sara");
        controller.getConstructionShip().add(building);

        //sara becomes lost
        controller.insertLost("sara", view);

        assertTrue(controller.getPlayers().contains(sara));
        assertTrue(controller.getVirtualViews().containsKey("sara"));
        assertTrue(controller.getResilientActivation().get("sara"));
        assertFalse(controller.getLostPlayers().contains("sara"));
        assertFalse(controller.getLostPlayersShip().contains(sara));

        //waiting for other players
        WaitingForPlayers waiting  = mock(WaitingForPlayers.class);
        controller.setGameState(waiting);
        VirtualView fede = mock(VirtualView.class);
        controller.getVirtualViews().put("fede", fede);
        controller.getLostPlayers().add("fede");

        //fede becomes lost
        controller.insertLost("fede", view);
        assertTrue(controller.getVirtualViews().containsKey("fede"));
        assertFalse(controller.getLostPlayers().contains("fede"));

        //adding diego
        ShipDashboard diego = mock(ShipDashboard.class);
        when(diego.getNickname()).thenReturn("diego");
        controller.getLostPlayers().add("diego");
        controller.getLostPlayersShip().put("diego", diego);
        VirtualView diegoView = mock(VirtualView.class);

        controller.insertLost("diego", diegoView);

        //-> diego is resumed
        assertTrue(controller.getPlayers().contains(diego));
    }

    @Test
    void testRemovePlayerByNickname(){
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        ShipDashboard diego = mock(ShipDashboard.class);
        when(diego.getNickname()).thenReturn("diego");
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        players.add(diego);
        controller.setPlayers(players);

        controller.removePlayerByNick("sara");

        //sara is removed
        assertTrue(controller.getLostPlayers().contains("sara"));
        //->not present in players
        assertFalse(controller.getPlayers().contains(sara));
        //->present in lost ship
        assertTrue(controller.getLostPlayersShip().contains(sara));
        //diego is still correctly present with no problems
        assertTrue(controller.getPlayers().contains(diego));
        assertFalse(controller.getLostPlayersShip().contains(diego));
        assertFalse(controller.getLostPlayers().contains("diego"));
    }

    @Test
    void testEndCardAndForfeit() throws IOException {
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        VirtualView viewSa = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", viewSa);
        ShipDashboard diego = mock(ShipDashboard.class);
        when(diego.getNickname()).thenReturn("diego");
        VirtualView viewDiego = mock(VirtualView.class);
        controller.getVirtualViews().put("diego", viewDiego);
        ArrayList<ShipDashboard> players = new ArrayList<>();
        players.add(sara);
        players.add(diego);
        controller.setPlayers(players);
        Game game = mock(Game.class);
        controller.setGame(game);
        //simulates sara has insufficient crew->loser!
        when(game.checkForfeitForInsufficientCrew(players)).thenReturn(new ArrayList<>(List.of(0)));

        controller.testEndCardAndCheckForfeit();

        //diego is not damaged-> no message!
        verify(viewDiego, never()).sendGenericMessage(any());
        //sara receives the sad communication :(
        verify(viewSa, times(1)).sendGenericMessage(contains("Your trip ends here"));
        //sara effectively eliminated
        assertFalse(controller.getVirtualViews().containsKey("sara"));
        assertFalse(controller.getPlayers().contains(sara));

        //simulates diego has been doubled->loser!
        when(game.checkForfeitForDoubledPosition(any())).thenReturn(new ArrayList<>(List.of(0)));
        controller.testEndCardAndCheckForfeit();
        //diego receives the sad communication :(
        verify(viewDiego, times(1)).sendGenericMessage(contains("Your trip ends here"));
    }

    @Test
    void testSendWinners() throws IOException {
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        VirtualView viewSa = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", viewSa);

        controller.setPlayers(new ArrayList<>(List.of(sara)));

        Game game = mock(Game.class);
        controller.setGame(game);
        //sets list of winners
        when(game.endGame(any())).thenReturn(new ArrayList<>(List.of(sara)));
        controller.sendWinnersTest();
        verify(viewSa).sendWinners(new ArrayList<>(List.of(sara)));

        VirtualView viewDiego = mock(VirtualView.class);
        controller.getVirtualViews().put("diego", viewDiego);
        controller.getEliminatedVirtualViews().put("diego", viewDiego);
        controller.sendWinnersTest();
        assertEquals(EndGame.class, controller.getState().getClass());
    }


    @Test
    void testChangeStateAndSendMessage() throws IOException {
        VirtualView viewSa = mock(VirtualView.class);
        VirtualView viewDiego = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", viewSa);
        controller.getVirtualViews().put("diego", viewDiego);
        ShipDashboard sara = mock(ShipDashboard.class);
        when(sara.getNickname()).thenReturn("sara");
        ShipDashboard diego = mock(ShipDashboard.class);
        when(diego.getNickname()).thenReturn("diego");
        controller.setPlayers(new ArrayList<>(List.of(sara, diego)));

        //next state: card drawing!
        Message messageS = mock(Message.class);
        when(messageS.getNickname()).thenReturn("sara");
        when(messageS.getKind()).thenReturn(MessageKind.GENERIC);
        Message messageD = mock(Message.class);
        when(messageD.getNickname()).thenReturn("diego");
        when(messageD.getKind()).thenReturn(MessageKind.GENERIC);

        //linking each message with the nick
        Map<String, ArrayList<Message>> messages = new HashMap<>();
        messages.put("sara", new ArrayList<>(List.of(messageS)));
        messages.put("diego", new ArrayList<>(List.of(messageD)));

        NextGameStateAndMessages nextGameStateAndMessages = mock(NextGameStateAndMessages.class);
        when(nextGameStateAndMessages.getNextGameState()).thenReturn(new CardDrawing());
        when(nextGameStateAndMessages.getPlayerMessages()).thenReturn(messages);
        when(nextGameStateAndMessages.getPlayers()).thenReturn(null);

        Game game = mock(Game.class);
        controller.setGame(game);
        controller.testChangeGameStateAndSendMessages(nextGameStateAndMessages);

        verify(viewSa).sendPremadeMessage(messageS);
        verify(viewDiego).sendPremadeMessage(messageD);

        //change to not specific state
        clearInvocations(viewSa);
        clearInvocations(viewDiego);
        GameState gameState = mock(GameState.class);
        NextGameStateAndMessages nextGameState = mock(NextGameStateAndMessages.class);
        when(nextGameState.getNextGameState()).thenReturn(gameState);
        when(nextGameState.getPlayerMessages()).thenReturn(messages);
        when(nextGameState.getPlayers()).thenReturn(null);

        controller.testChangeGameStateAndSendMessages(nextGameState);
        verify(viewSa).sendPremadeMessage(messageS);
        verify(viewDiego).sendPremadeMessage(messageD);

        //edge case: next state is null
        clearInvocations(viewSa);
        NextGameStateAndMessages caseEdge= mock(NextGameStateAndMessages.class);
        when(caseEdge.getNextGameState()).thenReturn(null);
        when(caseEdge.getPlayerMessages()).thenReturn(messages);
        when(caseEdge.getPlayers()).thenReturn(null);
        controller.testChangeGameStateAndSendMessages(caseEdge);
        //sent anyway
        verify(viewSa).sendPremadeMessage(messageS);

        //except
        doThrow(IOException.class).when(viewSa).sendPremadeMessage(messageS);
        assertThrows(RuntimeException.class, () -> controller.testChangeGameStateAndSendMessages(nextGameState));

    }

    @Test
    void testCheckWaitingForPlayers() throws IOException {
        PlayersNumResponseMessage playersNumResponseMessage = mock(PlayersNumResponseMessage.class);
        when(playersNumResponseMessage.getNumPlayersRequested()).thenReturn(2);
        when(playersNumResponseMessage.getNickname()).thenReturn("sara");
        controller.handleInitializationState(playersNumResponseMessage, false);

        //1 player connected
        VirtualView viewSa = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", viewSa);
        controller.checkWaitingForPlayers();
        verify(viewSa).waitingPartners();
        //2 players desired, so sara has to wait for the second player!!!
        assertEquals("WaitingForPlayers", controller.getState().getClass().getSimpleName());

        //the second player now is connected
        VirtualView viewDiego = mock(VirtualView.class);
        controller.getVirtualViews().put("diego", viewDiego);
        controller.checkWaitingForPlayers();
        //the match is officially starting for both players
        verify(viewSa).begin(any());
        verify(viewDiego).begin(any());
        //the following state has to be the construction one!
        assertEquals("ShipConstructionState", controller.getState().getClass().getSimpleName());

        //case end game true->
        controller.setWeAreInTheEndGameNow(true);
        controller.getNewGameVirtualViews().remove("diego");
        controller.checkWaitingForPlayers();
        verify(viewSa).waitingPartners();
    }

    @Test
    void testResilienceMessAndRemoveFailedClient() {
        VirtualView viewSa = mock(VirtualView.class);
        controller.getVirtualViews().put("sara", viewSa);
        controller.getResilientActivation().put("sara", false);

        controller.resilienceMexTest();

        //resilience mess-> send resilience invoked
        verify(viewSa).sendResilience();

        controller.removeFailedClient("sara");
        assertFalse(controller.getResilientActivation().get("sara"));
        assertFalse(controller.getVirtualViews().containsKey("sara"));
        assertTrue(controller.getLostPlayers().contains("sara"));
    }

    @Test
    void setInstanceTest() throws IOException {
        Controller controllerL = new Controller();
        Controller.setInstance(controllerL);
        assertTrue(Controller.isInitialized());
        assertTrue(controllerL == Controller.getInstance());
        Controller.setInstance(null);
        controller = Controller.getInstance();
        assertNotNull(controller);
    }

}
