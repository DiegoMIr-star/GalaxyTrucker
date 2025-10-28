package model;

import model.Cards.Card;
import model.Cards.Stardust;
import model.DifferentShipComponents.ShipComponent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    @Test
    void rollTheDiceTestValue(){
        int value;
        //try 100000 values to check if the interval of the two dices is respected or not
        for(int i=0; i<100000; i++){
            value = Game.rollTheDice();
            assertTrue(value>1 && value<13);
        }
    }

    @Test
    void createdDecksTest(){
        ArrayList<Card> deck = new ArrayList<>();
        for(int i=0; i<40; i++){
            deck.add(new Stardust(1, ""));
            deck.add(new Stardust(2, ""));
        }
        Deck gameDeck = new Deck(deck, 10, 10);
        Bank bank = new Bank(0,0,0,0,0,0);
        //Hourglass hourglass = new Hourglass();
        ArrayList<ShipComponent> shipComponents = new ArrayList<>();
        int path = 20;
        Game game = new Game(bank, path, gameDeck, shipComponents);

        assertEquals(0, game.getSmallDeck1().getSize());
        assertEquals(0, game.getSmallDeck2().getSize());
        assertEquals(0, game.getSmallDeck3().getSize());
        assertEquals(0, game.getSmallDeck4().getSize());
        assertEquals(0, game.getTotalDeck().getSize());

        game.initializeDecks();

        ArrayList<Deck> decks = game.getDecks();
        //4 decks
        assertEquals(4, decks.size());
        assertTrue(decks.contains(game.getSmallDeck1()));
        assertTrue(decks.contains(game.getSmallDeck2()));
        assertTrue(decks.contains(game.getSmallDeck3()));
        assertTrue(decks.contains(game.getSmallDeck4()));
    }

    @Test
    void updatePositionTest1(){
        //objects needed to initialize the class Game
        Bank bank = new Bank(0,0,0,0,0,0);
        //Hourglass hourglass = new Hourglass();
        Deck deck = new Deck(0, 0);
        ArrayList<ShipComponent> shipComponents = new ArrayList<>();
        Game game = new Game(bank, 24, deck, shipComponents);

        ArrayList<ShipDashboard> players = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            players.add(new ShipDashboard());
        }

        players.get(0).setNickname("player1");
        players.get(1).setNickname("player2");
        players.get(2).setNickname("player3");
        players.get(3).setNickname("player4");

        //starting positions
        players.get(0).setPosition(4);
        players.get(1).setPosition(3);
        players.get(2).setPosition(2);
        players.get(3).setPosition(1);

        players.get(0).setDaysToMove(0);
        players.get(1).setDaysToMove(0);
        players.get(2).setDaysToMove(0);
        players.get(3).setDaysToMove(0);


        Map<String, Integer> oldDaysToMove = new HashMap<>(players.size());
        Map<String, Integer> oldPositions = new HashMap<>(players.size());
        for (ShipDashboard player : players) {
            oldDaysToMove.put(player.getNickname(), player.getDaysToMove());
            oldPositions.put(player.getNickname(), player.getPosition());
        }

        //all players already sorted, DaysToMove = 0
        game.updatePositions(players);

        for (ShipDashboard player : players) {
            //daysToMove is 0 after the update
            assertEquals(0, player.getDaysToMove());
            //the current position equals to the old position plus the days to move (positive or negative)
            assertEquals(player.getPosition(), oldPositions.get(player.getNickname()) + oldDaysToMove.get(player.getNickname()));
        }
        for (int i = 1; i < players.size(); i++) {
            //the players are sorted from the highest position value to the lowest
            assertTrue(players.get(i-1).getPosition() > players.get(i).getPosition());
        }
    }

    @Test
    void updatePositionTestPositiveDays(){
        //objects needed to initialize the class Game
        Bank bank = new Bank(0,0,0,0,0,0);
        //Hourglass hourglass = new Hourglass();
        Deck deck = new Deck(0, 0);
        ArrayList<ShipComponent> shipComponents = new ArrayList<>();
        Game game = new Game(bank, 24, deck, shipComponents);

        ArrayList<ShipDashboard> players = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            players.add(new ShipDashboard());
        }

        players.get(0).setNickname("player1");
        players.get(1).setNickname("player2");
        players.get(2).setNickname("player3");
        players.get(3).setNickname("player4");

        //starting positions. the hypothesis is that they are sorted from highest to lowest value
        players.get(0).setPosition(4);
        players.get(1).setPosition(3);
        players.get(2).setPosition(2);
        players.get(3).setPosition(1);

        players.get(0).setDaysToMove(1);
        players.get(1).setDaysToMove(5);
        players.get(2).setDaysToMove(1);
        players.get(3).setDaysToMove(2);

        Map<String, Integer> oldDaysToMove = new HashMap<>(players.size());
        Map<String, Integer> oldPositions = new HashMap<>(players.size());
        Map<String, Integer> expectedPositions = new HashMap<>(players.size());
        int expectedPosition;
        for (ShipDashboard player : players) {
            oldDaysToMove.put(player.getNickname(), player.getDaysToMove());
            oldPositions.put(player.getNickname(), player.getPosition());
        }
        expectedPositions.put(players.getFirst().getNickname(), oldPositions.get(players.getFirst().getNickname()) + oldDaysToMove.get(players.getFirst().getNickname()));
        expectedPositions.put(players.get(1).getNickname(), players.get(1).getPosition());
        expectedPositions.put(players.get(2).getNickname(), players.get(2).getPosition());
        expectedPositions.put(players.get(3).getNickname(), players.get(3).getPosition());
        for (int i = 1; i < players.size(); i++) {
            expectedPosition = players.get(i).getPosition();
            while (oldDaysToMove.get(players.get(i).getNickname()) > 0) {
                expectedPosition++;
                int k = 0;
                while (k < i) {
                    int pos = expectedPositions.get(players.get(k).getNickname());
                    //if the new position is occupied by a player, it isn't counted
                    if (pos == expectedPosition) {
                        break;
                    }
                    k++;
                }
                if (k == i) {
                    oldDaysToMove.compute(players.get(i).getNickname(), (_, days) -> days - 1);
                }
            }
            expectedPositions.put(players.get(i).getNickname(), expectedPosition);
        }
        game.updatePositions(players);

        for (ShipDashboard player : players) {
            //daysToMove is 0 after the update
            assertEquals(0, player.getDaysToMove());
            //the current position equals to the expected position
            assertEquals(expectedPositions.get(player.getNickname()), player.getPosition());
        }
        for (int i = 1; i < players.size(); i++) {
            //the players are sorted from the highest position value to the lowest
            assertTrue(players.get(i-1).getPosition() > players.get(i).getPosition());
        }
    }

    @Test
    void updatePositionTestNegativeDays(){
        //objects needed to initialize the class Game
        Bank bank = new Bank(0,0,0,0,0,0);
        //Hourglass hourglass = new Hourglass();
        Deck deck = new Deck(0, 0);
        ArrayList<ShipComponent> shipComponents = new ArrayList<>();
        Game game = new Game(bank, 24, deck, shipComponents);

        ArrayList<ShipDashboard> players = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            players.add(new ShipDashboard());
        }

        players.get(0).setNickname("player1");
        players.get(1).setNickname("player2");
        players.get(2).setNickname("player3");
        players.get(3).setNickname("player4");

        //starting positions. the hypothesis is that they are sorted from highest to lowest value
        players.get(0).setPosition(4);
        players.get(1).setPosition(3);
        players.get(2).setPosition(2);
        players.get(3).setPosition(1);

        players.get(0).setDaysToMove(-9);
        players.get(1).setDaysToMove(-5);
        players.get(2).setDaysToMove(-3);
        players.get(3).setDaysToMove(-1);

        Map<String, Integer> oldDaysToMove = new HashMap<>(players.size());
        Map<String, Integer> oldPositions = new HashMap<>(players.size());
        Map<String, Integer> expectedPositions = new HashMap<>(players.size());
        int expectedPosition;
        for (ShipDashboard player : players) {
            oldDaysToMove.put(player.getNickname(), player.getDaysToMove());
            oldPositions.put(player.getNickname(), player.getPosition());
        }
        expectedPositions.put(players.getLast().getNickname(), oldPositions.get(players.getLast().getNickname()) + oldDaysToMove.get(players.getLast().getNickname()));
        expectedPositions.put(players.get(2).getNickname(), players.get(2).getPosition());
        expectedPositions.put(players.get(1).getNickname(), players.get(1).getPosition());
        expectedPositions.put(players.get(0).getNickname(), players.get(0).getPosition());
        for (int i = players.size() - 2; i >= 0; i--) {
            expectedPosition = players.get(i).getPosition();
            while (oldDaysToMove.get(players.get(i).getNickname()) < 0) {
                expectedPosition--;
                int k = oldDaysToMove.size() - 1;
                while (k > i) {
                    int pos = expectedPositions.get(players.get(k).getNickname());
                    if (pos == expectedPosition) {
                        break;
                    }
                    k--;
                }
                if (k == i) {
                    oldDaysToMove.compute(players.get(i).getNickname(), (_, days) -> days + 1);
                    //DEBUG
                    //System.out.println("old days " + oldDaysToMove.get(players.get(i).getNickname()));
                }
            }
            expectedPositions.put(players.get(i).getNickname(), expectedPosition);
        }
        game.updatePositions(players);

        for (ShipDashboard player : players) {
            //daysToMove is 0 after the update
            assertEquals(0, player.getDaysToMove());
            //the current position equals to the expected position
            assertEquals(expectedPositions.get(player.getNickname()), player.getPosition());
        }
        for (int i = 1; i < players.size(); i++) {
            //the players are sorted from the highest position value to the lowest
            assertTrue(players.get(i-1).getPosition() > players.get(i).getPosition());
        }
    }

    private static class TestShipDashboard extends ShipDashboard {
        private final Stocks stocks;
        private final int exposedConnectors;
        private final int garbage;
        private int credits;

        public TestShipDashboard(int exposedConnectors, int garbage, Stocks stocks, int credits) {
            this.stocks = stocks;
            this.exposedConnectors = exposedConnectors;
            this.garbage = garbage;
            this.credits = credits;
        }

        @Override
        public Stocks getStocks() {
            return stocks;
        }

        @Override
        public int getGarbageHeap(){
            return garbage;
        }

        @Override
        public int getNumOfExposedConnectors() {
            return exposedConnectors;

        }

        @Override
        public int getCredits() {
            return credits;
        }

        @Override
        public void removeStocks(Stocks stocks) {

        }

        @Override
        public void bonusMalus(int daysLoss, int creditsGained) {
            this.credits += creditsGained;
        }
    }


    @Test
    void endGameTest() {
        Game game = new Game(new Bank(0,0,0,0,0,0), 20, new Deck(0, 0), null);

        ShipDashboard ship1 = new TestShipDashboard(2,  50, new Stocks(1, 2, 3, 4), 3);
        ShipDashboard ship2 = new TestShipDashboard(1,  0, new Stocks(2, 3, 4, 5), 4);
        ShipDashboard ship3 = new TestShipDashboard(4,  1, new Stocks(3, 4, 5, 6), 5);
        ShipDashboard ship4 = new TestShipDashboard(2,  21, new Stocks(4, 5, 6, 7), 1);

        ArrayList<ShipDashboard> players= new ArrayList<>();
        players.add(ship1);
        players.add(ship2);
        players.add(ship3);
        players.add(ship4);

        ArrayList<ShipDashboard> winners;
        winners=game.endGame(players);

        assertEquals(-19, ship1.getCredits());
        assertEquals(44, ship2.getCredits());
        assertEquals(48, ship3.getCredits());
        assertEquals(32, ship4.getCredits());
        assertEquals(3, winners.size());
    }

    private static class TestShipDashboardCrew extends ShipDashboard {
        private final int crew;

        public TestShipDashboardCrew(int crew) {
            this.crew = crew;
        }

        @Override
        public int getCrew() {
            return crew;
        }
    }

    @Test
    void checkCrewTest() {
        Game game = new Game(new Bank(0,0,0,0,0,0), 20, new Deck(0, 0), null);

        ShipDashboard ship1 = new TestShipDashboardCrew(2);
        ShipDashboard ship2 = new TestShipDashboardCrew(3);
        ShipDashboard ship3 = new TestShipDashboardCrew(0);
        ShipDashboard ship4 = new TestShipDashboardCrew(4);

        ArrayList<ShipDashboard> players= new ArrayList<>();
        players.add(ship1);
        players.add(ship2);
        players.add(ship3);
        players.add(ship4);

        ArrayList<Integer> notEnoughCrew = game.checkForfeitForInsufficientCrew(players);

        assertEquals(1, notEnoughCrew.size());
        assertFalse(notEnoughCrew.contains(0));
        assertFalse(notEnoughCrew.contains(1));
        assertTrue(notEnoughCrew.contains(2));
        assertFalse(notEnoughCrew.contains(3));
    }

    private static class TestShipDashboardPositions extends ShipDashboard {
        private final int position;

        public TestShipDashboardPositions(int position) {
            this.position = position;
        }

        @Override
        public int getPosition() {
            return position;
        }
    }

    @Test
    void checkPositionsTest() {
        Game game = new Game(new Bank(0,0,0,0,0,0), 20, new Deck(0, 0), null);

        ShipDashboard ship1 = new TestShipDashboardPositions(50);
        ShipDashboard ship2 = new TestShipDashboardPositions(48);
        ShipDashboard ship3 = new TestShipDashboardPositions(12);
        ShipDashboard ship4 = new TestShipDashboardPositions(1);
        ArrayList<ShipDashboard> players= new ArrayList<>();
        players.add(ship1);
        players.add(ship2);
        players.add(ship3);
        players.add(ship4);
        ArrayList<Integer> notValidPositions = game.checkForfeitForDoubledPosition(players);

        assertEquals(2, notValidPositions.size());
        assertFalse(notValidPositions.contains(0));
        assertFalse(notValidPositions.contains(1));
        assertTrue(notValidPositions.contains(2));
        assertTrue(notValidPositions.contains(3));
    }

    @Test
    void checkPositionsTestDebugCase() {
        Game game = new Game(new Bank(0,0,0,0,0,0), 20, new Deck(0, 0), null);

        ShipDashboard ship1 = new TestShipDashboardPositions(40);
        ShipDashboard ship2 = new TestShipDashboardPositions(39);
        ShipDashboard ship3 = new TestShipDashboardPositions(20);
        ShipDashboard ship4 = new TestShipDashboardPositions(2);

        ArrayList<ShipDashboard> players= new ArrayList<>();
        players.add(ship1);
        players.add(ship2);
        players.add(ship3);
        players.add(ship4);

        ArrayList<Integer> notValidPositions = game.checkForfeitForDoubledPosition(players);

        assertEquals(2, notValidPositions.size());
        assertFalse(notValidPositions.contains(0));
        assertFalse(notValidPositions.contains(1));
        assertTrue(notValidPositions.contains(2));
        assertTrue(notValidPositions.contains(3));
    }

    @Test
    void testSetTimerPersistence(){
        Game game = new Game(new Bank(0,0,0,0,0,0), 20, new Deck(0, 0), null);
        game.setTimerPersistence();
        assertNotNull(game.getTimer());
    }

    @Test
    void testReturnStocks(){
        Bank bank = mock(Bank.class);
        Game game = new Game(bank, 20, new Deck(0, 0), null);
        Stocks stocks= new Stocks();
        game.returnStocks(stocks);

        verify(bank).addStocks(stocks);
    }

    @Test
    void testStartFirstTimer(){
        Game game = new Game(new Bank(0,0,0,0,0,0), 20, new Deck(0, 0), null);
        GameTimer gameTimer = new GameTimer();
        TimerListener timerListener = mock(TimerListener.class);
        game.startFirstTimer(timerListener);

        assertTrue(gameTimer.getCurTimerRound()==0);
    }
}