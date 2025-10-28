package model.DifferentShipComponents;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.exceptions.*;
import model.Stocks;

/**
 * Extension of ship component: it represents the cargo hold
 * @see ShipComponent
 */
public class CargoHold extends ShipComponent{

	/**
	 * True if the cargo hold is for red stocks
	 */
	private final boolean isSpecial;

	/**
	 * Object with different stocks on the cargo hold
	 */
	private final Stocks stocks;

	/**
	 * Max amount of stocks
	 */
	private final int maxCapacity;

	/**
	 * Constructor of the specific component: it initializes the attributes
	 * @param northSide current north side
	 * @param eastSide current east side
	 * @param southSide current south side
	 * @param westSide current west side
	 * @param special true if it's for red stocks
	 * @param maxCapacity current max capacity
	 * @param ID specific ID
	 * @param image path to the image
	 * @throws CapacityException thrown if the cargo doesn't respect the capacity
	 */
	@JsonCreator
	public CargoHold(
			@JsonProperty("north_side") Side northSide,
			@JsonProperty("east_side") Side eastSide,
			@JsonProperty("south_side") Side southSide,
			@JsonProperty("west_side") Side westSide,
			@JsonProperty("special") boolean special,
			@JsonProperty("max_capacity") int maxCapacity,
			@JsonProperty("ID") int ID,
			@JsonProperty("Image") String image
	) throws CapacityException{
		super(ComponentType.CARGO_HOLD, northSide, eastSide, southSide, westSide, ID, image);
		//Check the capacity of the component: special holds cannot have more than 2 stocks
		//normal holds cannot have more than 3 stocks
		//the capacity has to be more than 0, if not it's useless
		if((maxCapacity<=0) || (special && maxCapacity>2) || (!special && maxCapacity>3)) throw new CapacityException("Cargo doesn't respect capacity of the game.");
		this.isSpecial =special;
		this.maxCapacity=maxCapacity;
		stocks= new Stocks(0,0,0,0);
	}

	/**
	 * Constructor of the specific component: it initializes the attributes without ID
	 * @param northSide current north side
	 * @param eastSide current east side
	 * @param southSide current south side
	 * @param westSide current west side
	 * @param special true if it's for red stocks
	 * @param maxCapacity current max capacity
	 * @param image path to the image
	 * @throws CapacityException thrown if the cargo doesn't respect the capacity
	 */
	public CargoHold(
			@JsonProperty("north_side") Side northSide,
			@JsonProperty("east_side") Side eastSide,
			@JsonProperty("south_side") Side southSide,
			@JsonProperty("west_side") Side westSide,
			@JsonProperty("special") boolean special,
			@JsonProperty("max_capacity") int maxCapacity,
			@JsonProperty("Image") String image
	) throws CapacityException{
		super(ComponentType.CARGO_HOLD, northSide, eastSide, southSide, westSide, -1,image);
		//Check the capacity of the component: special holds cannot have more than 2 stocks
		//normal holds cannot have more than 3 stocks
		//the capacity has to be more than 0, if not it's useless
		if((maxCapacity<=0) || (special && maxCapacity>2) || (!special && maxCapacity>3)) throw new CapacityException("Cargo doesn't respect capacity of the game.");
		this.isSpecial =special;
		this.maxCapacity=maxCapacity;
		stocks= new Stocks(0,0,0,0);
	}

	/**
	 * It adds some stocks into the cargo hold
	 * @param newStocks object with different stocks
	 * @throws CapacityException thrown if the cargo doesn't respect the capacity
	 * @throws IllegalRedStocksException thrown if the cargo it's normal but want
	 *                                   to contain some red stocks
	 */
	public void addStocks(Stocks newStocks) throws CapacityException,IllegalRedStocksException{
		stocks.add(newStocks.get());
		//Check if after the add there's a violation of the capacity
		if(stocks.numberOfStocks()>maxCapacity) throw new CapacityException("Cargo doesn't respect capacity of the game.");
		//Check if after the add there are red stocks in a normal hold
		if(!isSpecial && stocks.getSpecialRedStocks()>0) throw new IllegalRedStocksException("Normal cargo with red stocks.");
	}

	/**
	 * It adds some stocks into the cargo hold
	 * @param blueStocks amount of blue stocks
	 * @param greenStocks amount of green stocks
	 * @param yellowStocks amount of yellow stocks
	 * @param redStocks amount of red stocks
	 * @throws CapacityException thrown if the cargo doesn't respect the capacity
	 * @throws IllegalRedStocksException thrown if the cargo it's normal but want
	 *                                   to contain some red stocks
	 */
	public void addStocks(int blueStocks, int greenStocks, int yellowStocks, int redStocks) throws CapacityException,IllegalRedStocksException{
		stocks.add(redStocks, yellowStocks, greenStocks, blueStocks);
		//Check if after the add there's a violation of the capacity
		if(stocks.numberOfStocks()>maxCapacity) throw new CapacityException("Cargo doesn't respect capacity of the game.");
		//Check if after the add there are red stocks in a normal hold
		if(!isSpecial && stocks.getSpecialRedStocks()>0) throw new IllegalRedStocksException("Normal cargo with red stocks.");
	}

	/**
	 * It adds a stock of a specific kind
	 * @param containerIndex index of the specific kind of stock to add
	 */
	public void addContainer(int containerIndex){
		switch (containerIndex){
			case 0:
				stocks.add(0,0,0,1);
			case 1:
				stocks.add(0,0,1,0);
			case 2:
				stocks.add(0,1,0,0);
			case 3:
				stocks.add(1,0,0,0);
			default:
				throw new RuntimeException("Invalid container index.");
		}
	}

	/**
	 * It removes the available stocks from the cargo hold and returns the remaining stocks
	 * @param garbageStocks object with the stocks which has to be removed
	 * @return the remaining stocks that should've been removed but couldn't
	 */
	public Stocks removeStocksAndGetLeftOvers(Stocks garbageStocks){
		return stocks.removeAndGetLeftOver(garbageStocks);
	}

	/**
	 * It says if the cargo is special
	 * @return ture if it's special
	 */
	public boolean canContainSpecial(){return isSpecial;}

	/**
	 * Getter of the stocks
	 * @return object stocks
	 */
	public Stocks getStocks(){return stocks;}

	/**
	 * Getter of the max capacity
	 * @return max capacity
	 */
	public int getMaxCapacity(){return maxCapacity;}

	/**
	 * It creates a copy of the specific component
	 * @return copy of the component
	 */
	@Override
	public ShipComponent clone() {
		CargoHold newComp = new CargoHold(this.getNorthSide(), this.getEastSide(), this.getSouthSide(),
				this.getWestSide(), this.isSpecial, this.maxCapacity, this.getID(), this.image);
		newComp.setOrientation(orientation);
		newComp.addStocks(stocks);


		return newComp;
	}

	/**
	 * It checks if the object passed in is an instance of the same class,
	 * if it is, it checks if all the attributes of the object are equal
	 * to this object
	 * @param o object to compare
	 * @return true if the object is equal
	 */
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof CargoHold comp)){ return false; }
		boolean result = true;
		result &= super.equals(o);
		result &= this.isSpecial == comp.isSpecial;
		result &= this.maxCapacity == comp.maxCapacity;
		result &= this.stocks.equals(comp.stocks);
		return result;
	}
}