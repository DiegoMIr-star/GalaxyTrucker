package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.exceptions.OutOfStockException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the stocks contained in the ships of the different players
 */
public class Stocks implements Serializable {

	/**
	 * List of amounts of stocks, each position represents a different kind of stocks:
	 * first for blue, second for green, third for yellow and fourth for red
	 */
	private final ArrayList<Integer> stocks;

	/**
	 * Constructor used to initialize an empty object stocks
	 */
	public Stocks()
	{
		stocks = new ArrayList<>(4);
		stocks.add(0);
		stocks.add(0);
		stocks.add(0);
		stocks.add(0);
	}

	/**
	 * Constructor used to initialize an object stocks with a specific number of
	 * stocks for each kind
	 * @param redStocks amount of red stocks
	 * @param yellowStocks amount of yellow stocks
	 * @param greenStocks amount of green stock
	 * @param blueStocks amount of blue stocks
	 */
	@JsonCreator
	public Stocks(
			@JsonProperty("red_stocks") int redStocks,
			@JsonProperty("yellow_stocks") int yellowStocks,
			@JsonProperty("green_stocks") int greenStocks,
			@JsonProperty("blue_stocks") int blueStocks
	)
	{
		//getIndex + 1 to get the price of the stock
		stocks = new ArrayList<>(4);
		stocks.add(0, blueStocks);
		stocks.add(1, greenStocks);
		stocks.add(2, yellowStocks);
		stocks.add(3, redStocks);
	}

	/**
	 * Setter of a specific amount of different kind of stocks into the
	 * object stocks
	 * @param redStocks amount of red stocks
	 * @param yellowStocks amount of yellow stocks
	 * @param greenStocks amount of green stock
	 * @param blueStocks amount of blue stocks
	 */
	public void set(int redStocks, int yellowStocks, int greenStocks, int blueStocks)
	{
		stocks.set(0, blueStocks);
		stocks.set(1, greenStocks);
		stocks.set(2, yellowStocks);
		stocks.set(3, redStocks);
	}

	/**
	 * Setter of a specific amount of different kind of stocks into the
	 * object stocks
	 * @param stocks object stocks which contains the different stocks to set
	 */
	public void set(ArrayList<Integer> stocks)
	{
		this.stocks.set(0, stocks.get(0));
		this.stocks.set(1, stocks.get(1));
		this.stocks.set(2, stocks.get(2));
		this.stocks.set(3, stocks.get(3));
	}

	/**
	 * Adder of a specific amount of different kind of stocks into the
	 * object stocks
	 * @param stocks object stocks which contains the different stocks to add
	 */
	public void add(ArrayList<Integer> stocks)
	{
		for(int i = 0; i < 4; i++)
		{
			this.stocks.set(i, this.stocks.get(i) + stocks.get(i));
		}
	}

	/**
	 * Adder of a specific amount of different kind of stocks into the
	 * object stocks
	 * @param redStocks amount of red stocks
	 * @param yellowStocks amount of yellow stocks
	 * @param greenStocks amount of green stocks
	 * @param blueStocks amount of blue stocks
	 */
	public void add(int redStocks, int yellowStocks, int greenStocks, int blueStocks) {
		Stocks stocksToAdd = new Stocks(redStocks, yellowStocks, greenStocks, blueStocks);
		this.add(stocksToAdd.stocks);
	}

	/**
	 * It removes a specific amount of stocks from the object stocks
	 * @param stocks object stocks which contains the stocks to remove
	 * @throws OutOfStockException thrown when the amount of a specific kind of
	 *                             stocks becomes negative
	 */
	public void remove(ArrayList<Integer> stocks) throws OutOfStockException
	{
		for(int i = 0; i < 4; i++)
		{
			this.stocks.set(i, this.stocks.get(i) - stocks.get(i));
			if(this.stocks.get(i) < 0)
				throw new OutOfStockException("Tried to remove stock of price " + (i + 1) + " with none left");
		}
	}

	public Stocks removeAndGetLeftOver(Stocks garbageStocks){
		Stocks remaining = new Stocks(0,0,0,0), toRemove = new Stocks(0,0,0,0);
		if(subtract(garbageStocks).hasSubZero()){
			remaining = subtract(garbageStocks);
			ArrayList<Boolean> subZeroContainers = remaining.subZeroContainers();
			for(int i = 0; i < stocks.size(); i++){
				remaining.get().set(i, subZeroContainers.get(i) ? -remaining.get().get(i) : 0);
				toRemove.get().set(i, subZeroContainers.get(i) ? stocks.get(i) : garbageStocks.get().get(i));
			}
			remove(toRemove.get());
			return remaining;
		}
		else {
			remove(garbageStocks.get());
			return remaining;
		}
	}

	/**
	 * It calculates the difference between this and stocks
	 * @param other object stocks which contains the stocks to subtract
	 */
	public Stocks subtract(Stocks other){
		return new Stocks(stocks.get(3) - other.stocks.get(3),
				stocks.get(2) - other.stocks.get(2),
				stocks.get(1) - other.stocks.get(1),
				stocks.get(0) - other.stocks.get(0));
	}

	/**
	 * It returns whether any of the containers are under 0, so invalid
	 * @return true if any of the containers are < 0
	 */
	public boolean hasSubZero(){
		return stocks.get(0) < 0 || stocks.get(1) < 0 || stocks.get(2) < 0 || stocks.get(3) < 0;
	}

	/**
	 * It returns whether any of the containers are under 0, so invalid
	 * @return list of booleans that are true if the containers of the corrisponding color are < 0
	 */
	public ArrayList<Boolean> subZeroContainers(){
		return new ArrayList<Boolean> (List.of(stocks.get(0) < 0, stocks.get(1) < 0, stocks.get(2) < 0, stocks.get(3) < 0));
	}

	/**
	 * It removes a specific amount of stocks from the object stocks
	 * @see #remove(ArrayList) this method is called to remove the stocks
	 * @param redStocks amount of red stocks
	 * @param yellowStocks amount of yellow stocks
	 * @param greenStocks amount of green stocks
	 * @param blueStocks amount of blue stocks
	 */
	public void remove(int redStocks, int yellowStocks, int greenStocks, int blueStocks) {
		Stocks stocksToRemove = new Stocks(redStocks, yellowStocks, greenStocks, blueStocks);
		this.remove(stocksToRemove.stocks);
	}

	/**
	 * Getter of red stocks
	 * @return amount of red stocks
	 */
	public int getSpecialRedStocks() {return stocks.get(3);}

	/**
	 * Getter of yellow stocks
	 * @return amount of yellow stocks
	 */
	public int getYellowStocks(){ return stocks.get(2);}

	/**
	 * Getter of green stocks
	 * @return amount of green stocks
	 */
	public int getGreenStocks(){ return stocks.get(1);}

	/**
	 * Getter of blue stocks
	 * @return amount of blue stocks
	 */
	public int getBlueStocks(){ return stocks.getFirst();}

	/**
	 * Getter of the entire attribute stocks with all the stocks of different kind
	 * @return list of different stocks
	 */
	public ArrayList<Integer> get() {return stocks;}

	/**
	 * It returns the total amount of stocks contained in the object
	 * @return amount of different stocks
	 */
	public int numberOfStocks(){
		int total=0;
		for(int i=0;i<4;i++){
			total=total+stocks.get(i);
		}
		return total;
	}

	/**
	 * Method used to print the different stocks into the TUI
	 * @see View.TUI
	 * @return the string to represent the stocks
	 */
	@Override
	public String toString(){
		String s = "";
		boolean prevComma = false;
		if(stocks.get(0) != 0){
			s += stocks.getFirst() + " blue containers";
			prevComma = true;
		}
		if(stocks.get(1) != 0){
			if(prevComma)
				s += ", ";
			s += stocks.get(1) + " green containers";
			prevComma = true;
		}
		if(stocks.get(2) != 0){
			if(prevComma)
				s += ", ";
			s += stocks.get(2) + " yellow containers";
			prevComma = true;
		}
		if(stocks.get(3) != 0){
			if(prevComma)
				s += ", ";
			s += stocks.get(3) + " red containers";
		}

		return 	s;
	}

	/**
	 * It checks if an object is identical to another of this kind
	 * @param o object to check
	 * @return true if the object is equal
	 */
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Stocks)){ return false; }
		Stocks comp = (Stocks)o;
		boolean result;
		result = this.stocks.equals(comp.stocks);
		return result;
	}

}
