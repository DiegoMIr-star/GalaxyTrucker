package model;

import model.exceptions.OutOfStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StocksTest {
    private Stocks stocks1;
    private Stocks stocks2;

    @BeforeEach
    void setUp() {
        //Check both kind of constructors
        stocks1=new Stocks(100,100,100,100);
        stocks2=new Stocks();
    }

    @Test
    void set() {
        //Test passed!
        //Check if stocks2 with the constructor without parameters has a good set
        stocks2.set(100,100,100,100);
        assertEquals(100,stocks2.getSpecialRedStocks());
        assertEquals(100,stocks2.getGreenStocks());
        assertEquals(100,stocks2.getYellowStocks());
        assertEquals(100,stocks2.getBlueStocks());
        //Double check with ArrayList
        assertEquals(100,stocks2.get().getFirst());
        assertEquals(100,stocks2.get().get(1));
        assertEquals(100,stocks2.get().get(2));
        assertEquals(100,stocks2.get().get(3));
    }

    @Test
    void testSet() {
        //Test passed!
        ArrayList<Integer> newStocks=new ArrayList<>(4);
        newStocks.add(100);
        newStocks.add(90);
        newStocks.add(80);
        newStocks.add(700);
        stocks2.set(newStocks);
        assertEquals(100,stocks2.getBlueStocks());
        assertEquals(90,stocks2.getGreenStocks());
        assertEquals(80,stocks2.getYellowStocks());
        assertEquals(700,stocks2.getSpecialRedStocks());
    }

    @Test
    void add() {
        //Test passed!
        ArrayList<Integer> newStocks=new ArrayList<>(4);
        newStocks.add(100);
        newStocks.add(0);
        newStocks.add(1);
        newStocks.add(3);
        stocks1.add(newStocks);
        //Check if the new stocks are correctly added
        assertEquals(200,stocks1.getBlueStocks());
        assertEquals(100,stocks1.getGreenStocks());
        assertEquals(101,stocks1.getYellowStocks());
        assertEquals(103,stocks1.getSpecialRedStocks());
    }

    @Test
    void remove() {
        //Test passed!
        ArrayList<Integer> removeStocks=new ArrayList<>(4);
        for(int i=0;i<4;i++){
            removeStocks.add(100);
        }
        stocks1.remove(removeStocks);
        //Check if the stocks are correctly removed
        assertEquals(0,stocks1.getBlueStocks());
        assertEquals(0,stocks1.getGreenStocks());
        assertEquals(0,stocks1.getYellowStocks());
        assertEquals(0,stocks1.getSpecialRedStocks());
        //Check if the exception is correctly used
        Exception exception=assertThrows(OutOfStockException.class,()-> stocks2.remove(removeStocks));
        assertEquals("Tried to remove stock of price 1 with none left",exception.getMessage());
    }

    @Test
    void numberOfStocks() {
        //Test passed!
        //Check if the method works for both kind of construction
        assertEquals(400,stocks1.numberOfStocks());
        assertEquals(0,stocks2.numberOfStocks());
    }
}