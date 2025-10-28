package model;

import model.exceptions.UnderflowBankException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    private Bank bank;
    @BeforeEach
    void setUp() {
        bank=new Bank(100,100,100,100,100,100);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetCredits() {
        //Test passed!
        bank.getCredits(10);
        assertEquals(90,bank.getCredits());
        //Try to check if the method can take away the credits correctly
        bank.getCredits(90);
        assertEquals(0,bank.getCredits());
        //Try to check if the method can take away the credits till the 0
        Exception exception=assertThrows(UnderflowBankException.class,()-> bank.getCredits(20));
        assertEquals("Trying to ask more than the credit amount in the entire bank.",exception.getMessage());
        //Try to check if the method calls rightly the exception, when needed
    }

    @Test
    void testGetBatteries() {
        //Test passed!
        bank.getBatteries(10);
        assertEquals(90,bank.getBatteries());
        //Try to check if the method can take away the batteries correctly
        bank.getBatteries(90);
        assertEquals(0,bank.getBatteries());
        //Try to check if the method can take away the batteries till the 0
        Exception exception=assertThrows(UnderflowBankException.class,()-> bank.getBatteries(20));
        assertEquals("Trying to ask more than the battery amount in the entire bank.",exception.getMessage());
        //Try to check if the method calls rightly the exception, when needed
    }

    @Test
    void getStocks() {
        //Test passed!
        Stocks stocks=bank.getStocks(100,100,100,100);
        //Check if after the request the bank has reached values 0 for the stocks
        assertEquals(0,bank.getRedStocks());
        assertEquals(0,bank.getBlueStocks());
        assertEquals(0,bank.getGreenStocks());
        assertEquals(0,bank.getYellowStocks());
        //Check if the object stocks is good initialised
        assertEquals(100,stocks.getSpecialRedStocks());
        assertEquals(100,stocks.getYellowStocks());
        assertEquals(100,stocks.getBlueStocks());
        assertEquals(100,stocks.getGreenStocks());
        //Check if the exception is correctly called
        Exception exception=assertThrows(UnderflowBankException.class,()-> bank.getStocks(10,10,10,10));
        assertEquals("Trying to ask more than the stock amount in the entire bank.",exception.getMessage());
    }

    @Test
    void addCredits() {
        //Test passed!
        //Check if the credits are correctly inserted
        bank.addCredits(100);
        assertEquals(200,bank.getCredits());
    }

    @Test
    void addBatteries() {
        //Test passed!
        //Check if the batteries are correctly inserted
        bank.addBatteries(100);
        assertEquals(200,bank.getBatteries());
    }

    @Test
    void addStocks() {
        //Test passed!
        Stocks stocks=new Stocks(100,100,100,100);
        bank.addStocks(stocks);
        //Check if the stocks are correctly inserted
        assertEquals(200,bank.getRedStocks());
        assertEquals(200,bank.getBlueStocks());
        assertEquals(200,bank.getGreenStocks());
        assertEquals(200,bank.getYellowStocks());
    }
}