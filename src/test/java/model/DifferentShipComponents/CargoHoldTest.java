package model.DifferentShipComponents;

import model.exceptions.CapacityException;
import model.Stocks;
import model.exceptions.IllegalRedStocksException;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoHoldTest {
    private CargoHold cargoHold1;
    private CargoHold cargoHold2;
    private CargoHold cargoHold3;
    private CargoHold cargoHold4;

    @BeforeEach
    void setUp() {
        cargoHold1=new CargoHold(Side.SingleConnector,Side.DoubleConnector,Side.BlankSide,Side.BlankSide,true,2,"");
        cargoHold2=new CargoHold(Side.SingleConnector,Side.DoubleConnector,Side.BlankSide,Side.BlankSide,false,3,"");
        cargoHold3=new CargoHold(Side.SingleConnector,Side.DoubleConnector,Side.BlankSide,Side.BlankSide,true,2,"");
        cargoHold4=new CargoHold(Side.SingleConnector,Side.DoubleConnector,Side.BlankSide,Side.BlankSide,false,3,"");
    }

    @Test
    void addStocks() {
        //Test passed!
        //Just check if the objects are created respecting the rules of the game
        Exception exception1= assertThrows(CapacityException.class,()-> new CargoHold(Side.SingleConnector,Side.DoubleConnector,Side.BlankSide,Side.BlankSide,true,3,""));
        Exception exception2= assertThrows(CapacityException.class,()-> new CargoHold(Side.SingleConnector,Side.DoubleConnector,Side.BlankSide,Side.BlankSide,false,4,""));
        Exception exception3= assertThrows(CapacityException.class,()-> new CargoHold(Side.SingleConnector,Side.DoubleConnector,Side.BlankSide,Side.BlankSide,true,0,""));
        assertEquals("Cargo doesn't respect capacity of the game.",exception1.getMessage());
        assertEquals("Cargo doesn't respect capacity of the game.",exception2.getMessage());
        assertEquals("Cargo doesn't respect capacity of the game.",exception3.getMessage());
        //Now check if the exception for stocks are correctly thrown
        Stocks stocks1=new Stocks(1,1,1,1);
        exception1=assertThrows(CapacityException.class,()-> cargoHold1.addStocks(stocks1));
        assertEquals("Cargo doesn't respect capacity of the game.",exception1.getMessage());
        //Initialize again cargoHold1
        cargoHold1.removeStocksAndGetLeftOvers(stocks1);
        ArrayList<Integer> removeStocks=new ArrayList<>(4);
        removeStocks.add(1);
        removeStocks.add(1);
        removeStocks.add(1);
        removeStocks.add(0);
        stocks1.remove(removeStocks);
        exception2=assertThrows(IllegalRedStocksException.class,()-> cargoHold2.addStocks(stocks1));
        assertEquals("Normal cargo with red stocks.",exception2.getMessage());
        //Initialize again cargoHold2
        cargoHold2.removeStocksAndGetLeftOvers(new Stocks(1,0,0,0));
        Stocks stocks2=new Stocks(0,1,1,0);
        cargoHold1.addStocks(stocks1);
        cargoHold2.addStocks(stocks2);
        //With the following assertions you can test also the method getStocks()
        //Check if the cargo hold contains the right number of stocks
        assertEquals(1,cargoHold1.getStocks().getSpecialRedStocks());
        assertEquals(0,cargoHold1.getStocks().getYellowStocks());
        assertEquals(0,cargoHold1.getStocks().getGreenStocks());
        assertEquals(0,cargoHold1.getStocks().getBlueStocks());
        //Check the same for cargo hold number two
        assertEquals(0,cargoHold2.getStocks().getSpecialRedStocks());
        assertEquals(1,cargoHold2.getStocks().getYellowStocks());
        assertEquals(1,cargoHold2.getStocks().getGreenStocks());
        assertEquals(0,cargoHold2.getStocks().getBlueStocks());
    }

    @Test
    void removeStocksAndGetLeftOvers() {
        //Test passed!
        Stocks stocks1=new Stocks(1,0,0,0);
        Stocks stocks2=new Stocks(0,1,1,0);
        Stocks stocks3=new Stocks(1,1,0,0);
        Stocks stocks4=new Stocks(0,1,1,0);
        cargoHold1.addStocks(stocks1);
        cargoHold2.addStocks(stocks2);
        //Check if the stocks are correctly deleted
        cargoHold1.removeStocksAndGetLeftOvers(stocks1);
        cargoHold2.removeStocksAndGetLeftOvers(stocks2);
        assertEquals(0,cargoHold1.getStocks().getSpecialRedStocks());
        assertEquals(0,cargoHold1.getStocks().getYellowStocks());
        assertEquals(0,cargoHold1.getStocks().getGreenStocks());
        assertEquals(0,cargoHold1.getStocks().getBlueStocks());
        assertEquals(0,cargoHold2.getStocks().getSpecialRedStocks());
        assertEquals(0,cargoHold2.getStocks().getYellowStocks());
        assertEquals(0,cargoHold2.getStocks().getGreenStocks());
        assertEquals(0,cargoHold2.getStocks().getBlueStocks());

        cargoHold3.addStocks(stocks3);
        cargoHold4.addStocks(stocks4);
        Stocks result1 = cargoHold3.removeStocksAndGetLeftOvers(stocks1);
        Stocks result2 = cargoHold4.removeStocksAndGetLeftOvers(stocks3);
        assertEquals(0,cargoHold3.getStocks().getSpecialRedStocks());
        assertEquals(1,cargoHold3.getStocks().getYellowStocks());
        assertEquals(0,cargoHold3.getStocks().getGreenStocks());
        assertEquals(0,cargoHold3.getStocks().getBlueStocks());
        assertEquals(0,result1.getSpecialRedStocks());
        assertEquals(0,result1.getYellowStocks());
        assertEquals(0,result1.getGreenStocks());
        assertEquals(0,result1.getBlueStocks());
        assertEquals(0,cargoHold4.getStocks().getSpecialRedStocks());
        assertEquals(0,cargoHold4.getStocks().getYellowStocks());
        assertEquals(1,cargoHold4.getStocks().getGreenStocks());
        assertEquals(0,cargoHold4.getStocks().getBlueStocks());
        assertEquals(1,result2.getSpecialRedStocks());
        assertEquals(0,result2.getYellowStocks());
        assertEquals(0,result2.getGreenStocks());
        assertEquals(0,result2.getBlueStocks());
    }

    @Test
    void getSpecial() {
        //Test passed!
        //Check if the cargo holds are correctly initialized
        assertTrue(cargoHold1.canContainSpecial());
        assertFalse(cargoHold2.canContainSpecial());
    }

    @Test
    void getMaxCapacity() {
        //Test passed!
        //Check if the cargo holds are correctly initialized
        assertEquals(2,cargoHold1.getMaxCapacity());
        assertEquals(3,cargoHold2.getMaxCapacity());
    }
}