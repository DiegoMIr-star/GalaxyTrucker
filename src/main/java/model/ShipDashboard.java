package model;

import model.DifferentShipComponents.*;
import model.Projectiles.Orientation;
import model.Projectiles.Projectile;
import model.exceptions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class is the representation of the entire dashboard of the player:
 * it contains also the assembled ship.
 */
public class ShipDashboard implements Serializable, Cloneable {

	/**
	 * Name of the player during the game
	 */
	private String nickname;

	/**
	 * x bounds of the structure, which represents the ship
	 */
	final static private int xLowerBound = 4, xUpperBound = 11;

	/**
	 * y bounds of the structure, which represents the ship
	 */
	final static private int yLowerBound = 5, yUpperBound = 10;

	/**
	 * Amount of credits of the player
	 */
	private int credits;

	/**
	 * Amount of batteries of the player
	 */
	private int batteries;

	/**
	 * Amount of stocks of the player
	 */
	private Stocks stocks;

	/**
	 * Checks the presence of a purple alien on the ship
	 */
	private boolean purpleAlienPresent;

	/**
	 * Checks the presence of a brown alien on the ship
	 */
	private boolean brownAlienPresent;

	/**
	 * It counts the number of humans on the ship
	 */
	private int humans;

	/**
	 * It counts the static firepower of the ship
	 */
	private double staticFirePower;

	/**
	 * It counts the static motor power of the ship
	 */
	private int staticMotorPower;

	/**
	 * Represents the heap of discarded components
	 */
	private int garbageHeap;

	/**
	 * Matrix of ship components used to represent the ship
	 */
	private ShipComponent[][] ship;

	/**
	 * It's a list of booked ship components
	 */
	private final ArrayList<ShipComponent> bookedComponents;

	/**
	 * Current position of the player
	 */
	private int position;

	/**
	 * Amount of days used in order to update the position
	 */
	private int daysToMove;

	/**
	 * Constructor of the ship dashboard:
	 * it initializes all the attributes
	 * @param centralCabinID ID of the central cabin, which has to be different
	 *                       for all the players
	 */
	public ShipDashboard(int centralCabinID) {
		credits = 0;
		batteries = 0;
		purpleAlienPresent = false;
		brownAlienPresent = false;
		humans = 0;
		staticFirePower = 0;
		staticMotorPower = 0;
		garbageHeap = 0;
		bookedComponents = new ArrayList<>();
		position = 0;
		daysToMove = 0;
		stocks = new Stocks();

		ShipComponent l__ = new UnavailableSlot();
		ShipComponent WWW = null; //convenience variables to help visualize the ship shape
		//initial central cabin
		ShipComponent iCi = new Cabin(true, Side.UniversalConnector,Side.UniversalConnector,Side.UniversalConnector,Side.UniversalConnector,centralCabinID,"GT-new_tiles_16_for web" + (centralCabinID + 1) +".jpg");
		ship = new ShipComponent[][]{
				{l__,l__,WWW,l__,WWW,l__,l__},
				{l__,WWW,WWW,WWW,WWW,WWW,l__},
				{WWW,WWW,WWW,iCi,WWW,WWW,WWW},
				{WWW,WWW,WWW,WWW,WWW,WWW,WWW},
				{WWW,WWW,WWW,l__,WWW,WWW,WWW}};
	}

	/**
	 * Constructor which initializes the central cabin with a standard ID
	 * @see #ShipDashboard(int)
	 */
	public ShipDashboard() {this(33);}

	/**
	 * Getter of the width of the ship
	 * @return ship width
	 */
	public int getShipWidth(){return xUpperBound - xLowerBound;}

	/**
	 * Getter of the height of the ship
	 * @return ship height
	 */
	public int getShipHeight(){return yUpperBound - yLowerBound;}

	/**
	 * Getter of the x lower bound
	 * @return x lower bound
	 */
	public int get_xLowerBound(){return xLowerBound;}

	/**
	 * Getter of the x upper bound
	 * @return x upper bound
	 */
	public int get_xUpperBound(){return xUpperBound;}

	/**
	 * Getter of the y lower bound
	 * @return y lower bound
	 */
	public int get_yLowerBound(){return yLowerBound;}

	/**
	 * Getter of the y upper bound
	 * @return y upper bound
	 */
	public int get_yUpperBound(){return yUpperBound;}

	/**
	 * Setter of the nickname of the player
	 * @param nickname current nickname
	 */
	public void setNickname(String nickname) {this.nickname = nickname;}

	/**
	 * Getter of the nickname
	 * @return nickname of the player
	 */
	public String getNickname() {return this.nickname;}

	/**
	 * Getter of the number of batteries
	 * @return amount of batteries
	 */
	public int getBatteries() {return batteries;}

	/**
	 * It consumes a certain amount of batteries from the ship
	 * @param amount amount of batteries
	 */
	public void useBatteries(int amount) {
        for (ShipComponent[] shipComponents : ship) {
            for (ShipComponent shipComponent : shipComponents) {
                if (Objects.requireNonNull(shipComponent) instanceof BatteryComponent batteryComponent) {
                    if (batteryComponent.getCurrentBatteries() > 0) {
                        if (batteryComponent.getCurrentBatteries() >= amount) {
                            batteryComponent.useBatteries(amount);
                            batteries -= amount;
                            //    amount = 0;
                            return;
                        } else {
                            batteries -= batteryComponent.getCurrentBatteries();
                            amount -= batteryComponent.getCurrentBatteries();
                            batteryComponent.useAllBatteries();
                        }
                    }
                }
            }
        }

		if(amount > 0)
			throw new NotEnoughBatteriesException("There weren't enough batteries to complete the action.");

	}

	/**
	 * It removes from a certain cabin the humans or the aliens present
	 * @param x x coordinate of the cabin
	 * @param y y coordinate of the cabin
	 * @param humans number of possible humans to remove
	 * @param purpleAlien possible purple alien to remove
	 * @param brownAlien possible brown alien to remove
	 * @throws IncompatibleTargetComponent thrown when the component is not a cabin
	 */
	public void removeCrewFromCabin(int x, int y, int humans, boolean purpleAlien, boolean brownAlien) throws IncompatibleTargetComponent{
        if (Objects.requireNonNull(ship[y][x]) instanceof Cabin cabin) {
            int numHumans = cabin.getHumanEquip().size();
            boolean pAlien = cabin.getPurpleAlienEquip();
            boolean bAlien = cabin.getBrownAlienEquip();
            if ((cabin.getHumanEquip().getFirst() || cabin.getHumanEquip().getLast()) && humans == 1)
                cabin.removeHuman();
            else if (cabin.getHumanEquip().getFirst() && cabin.getHumanEquip().getLast() && humans == 2) {
                cabin.removeHuman();
                cabin.removeHuman();
            } else if (purpleAlien && cabin.getPurpleAlienEquip())
                cabin.removeAlien();
            else if (brownAlien && cabin.getBrownAlienEquip())
                cabin.removeAlien();
            else
                throw new NotEnoughCrewmatesException("There weren't enough crew mates to remove: " +
                        "Humans: " + numHumans + " - " + humans + ", " +
                        "purpleAlien: " + pAlien + " - " + purpleAlien + ", " +
                        "brownAlien: " + bAlien + " - " + brownAlien);
        } else {
            throw new IncompatibleTargetComponent("Component wasn't a cabin, crew couldn't be removed.");
        }
	}

	/**
	 * Getter of the stocks
	 * @return current stocks
	 */
	public Stocks getStocks() {return stocks;}

	/**
	 * Getter of the brown alien
	 * @return true if there's a brown alien
	 */
	public boolean isBrownAlienPresent() {return brownAlienPresent;}

	/**
	 * Getter of the purple alien
	 * @return true if there's a purple alien
	 */
	public boolean isPurpleAlienPresent() {return purpleAlienPresent;}

	/**
	 * Getter of the humans
	 * @return amount of humans
	 */
	public int getHumans() {return humans;}

	/**
	 * Getter of the entire crew present on the ship
	 * @return amount of the crew
	 */
	public int getCrew() {
		int purpleAliens = isPurpleAlienPresent() ? 1 : 0;
		int brownAliens = isBrownAlienPresent() ? 1 : 0;
		return getHumans() + purpleAliens + brownAliens;
	}

	/**
	 * Getter of the static firepower
	 * @return static firepower
	 */
	public double getStaticFirePower() {return staticFirePower;}

	/**
	 * Getter of the static motor power
	 * @return static motor power
	 */
	public int getStaticMotorPower() {return staticMotorPower;}

	/**
	 * It checks if side is an effective connector
	 * @param side current side to check
	 * @return true if it's a connector
	 */
	private boolean isSideAnyConnector(Side side) {
		return (side == Side.SingleConnector || side == Side.DoubleConnector || side == Side.UniversalConnector ||
				side == Side.ShieldAndSingleConnector || side == Side.ShieldAndDoubleConnector || side == Side.ShieldAndUniversalConnector);
	}

	/**
	 * It checks if side is a single connector
	 * @param side current side to check
	 * @return true if it's a single connector
	 */
	private boolean isSideSingleConnector(Side side) {return (side == Side.SingleConnector || side == Side.ShieldAndSingleConnector);}

	/**
	 * It checks if side is a double connector
	 * @param side current side to check
	 * @return true if it's a double connector
	 */
	private boolean isSideDoubleConnector(Side side) {return (side == Side.DoubleConnector || side == Side.ShieldAndDoubleConnector);}

	/**
	 * It checks if side is a universal connector
	 * @param side current side to check
	 * @return true if it's a universal connector
	 */
	private boolean isSideUniversalConnector(Side side) {return (side == Side.UniversalConnector || side == Side.ShieldAndUniversalConnector);}

	/**
	 * It checks if side is a blank side
	 * @param side current side to check
	 * @return true if it's a blank side
	 */
	private boolean isSideBlank(Side side){return (side == Side.BlankSide || side == Side.ShieldProtection);}

	/**
	 * It checks if there's an unavailable slot in a certain place on the ship
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if it's unavailable
	 */
	public boolean isUnavailableSlot(int x, int y) {
        return ship[y][x] instanceof UnavailableSlot;
	}

	/**
	 * Getter of the number of exposed connectors in the entire ship
	 * @return amount of exposed connectors
	 */
	public int getNumOfExposedConnectors() {
		int exposedConnectors = 0;
		boolean NisConnector, EisConnector, SisConnector, WisConnector;
		Side north, east, south, west;

		for(int y = 0; y < ship.length; y++) {
			for(int x = 0; x < ship[y].length; x++) {
				if (ship[y][x] != null) {
					north = ship[y][x].getNorthSide();
					east = ship[y][x].getEastSide();
					south = ship[y][x].getSouthSide();
					west = ship[y][x].getWestSide();
					NisConnector = isSideAnyConnector(north);
					EisConnector = isSideAnyConnector(east);
					SisConnector = isSideAnyConnector(south);
					WisConnector = isSideAnyConnector(west);
					if(NisConnector && (isUpperNeighborEmpty(x,y))) {
						exposedConnectors++;
					}
					if(EisConnector && (isRightNeighborEmpty(x,y))) {
						exposedConnectors++;
					}
					if(SisConnector && (isLowerNeighborEmpty(x,y))) {
						exposedConnectors++;
					}
					if(WisConnector && (isLeftNeighborEmpty(x,y))) {
						exposedConnectors++;
					}
				}
			}
		}

		return exposedConnectors;
	}

	/**
	 * It updates the ship, finalizing it
	 * @see #finalizeShip()
	 * @param updatedShip new version of the ship
	 */
	public void updateShip(ShipComponent[][] updatedShip) {
		this.ship = updatedShip;
		finalizeShip();
	}

	/**
	 * Getter of the garbage heap
	 * @return current garbage heap
	 */
	public int getGarbageHeap() {return garbageHeap;}

	/**
	 * Getter of the credits
	 * @return current credits
	 */
	public int getCredits() {return credits;}

	/**
	 * Getter of the position
	 * @return current position
	 */
	public int getPosition() { return position; }

	/**
	 * Getter of the days to move
	 * @return current days to move
	 */
	public int getDaysToMove() { return this.daysToMove; }

	/**
	 * Adder of credits
	 * @param creditsToAdd amount of credits to add
	 */
	public void addCredits(int creditsToAdd){this.credits += creditsToAdd;}

	/**
	 * Setter of the new position
	 * @param position new position
	 */
	public void setPosition(int position) { this.position = position; }

	/**
	 * Setter of the days to move
	 * @param daysToMove new days to move
	 */
	public void setDaysToMove(int daysToMove) {this.daysToMove = daysToMove;}

	/**
	 * Setter of the amount of batteries
	 * @deprecated used only for debugging
	 * @param n amount of batteries
	 */
	public void setBatteries(int n) {batteries += n;}

	/**
	 * Adder of stocks
	 * @param stocksToAdd stocks to add
	 */
	public void addStocks(Stocks stocksToAdd) {this.stocks.add(stocksToAdd.get());}

	/**
	 * It removes a certain amount of stocks
	 * @param stocksToRemove stocks to remove
	 * @throws OutOfStockException thrown if there are not enough stocks to remove
	 */
	public void removeStocks(Stocks stocksToRemove) throws OutOfStockException {
		try {
			for(int y = 0; y < ship.length; y++) {
				for(int x = 0; x < ship[y].length; x++) {
					if(ship[y][x] != null && ship[y][x] instanceof CargoHold cargoHold) {
						stocksToRemove = cargoHold.removeStocksAndGetLeftOvers(stocksToRemove);
						//if stocksToRemove doesn't containers in it anymore
						if(!(new Stocks(0,0,0,0).subtract(stocksToRemove).hasSubZero())) {
							break;
						}
					}
				}
			}
			stocks.remove(stocksToRemove.get());
		}
		catch(OutOfStockException _) {
			System.out.println("There aren't enough stocks to remove.");
		}
	}

	/**
	 * It removes a certain amount of stocks as a penalty
	 * @see model.Cards.Enemies.Smugglers
	 * @param penaltyStocks penalty stocks to be removed
	 * @throws OutOfStockException thrown when there aren't enough stocks
	 */
	public void removeMostPreciousStocks(int penaltyStocks) throws OutOfStockException {
		int redStocksToRemove,
				yellowStocksToRemove = 0,
				greenStocksToRemove = 0,
				blueStocksToRemove = 0,
		batteriesToRemove = 0;

		if(penaltyStocks > stocks.getSpecialRedStocks()){
			redStocksToRemove = stocks.getSpecialRedStocks();
			penaltyStocks -= redStocksToRemove;

			if(penaltyStocks > stocks.getYellowStocks()){
				yellowStocksToRemove = stocks.getYellowStocks();
				penaltyStocks -= yellowStocksToRemove;

				if(penaltyStocks > stocks.getGreenStocks()){
					greenStocksToRemove = stocks.getGreenStocks();
					penaltyStocks -= greenStocksToRemove;

					if(penaltyStocks > stocks.getBlueStocks()){
						blueStocksToRemove = stocks.getBlueStocks();
						penaltyStocks -= blueStocksToRemove;

						if(penaltyStocks > batteries){
							batteriesToRemove = batteries;
							penaltyStocks -= batteries;

						}
						else {
							batteriesToRemove = penaltyStocks;
							penaltyStocks = 0;
						}
					}
					else {
						blueStocksToRemove = penaltyStocks;
						penaltyStocks = 0;
					}
				}
				else {
					greenStocksToRemove = penaltyStocks;
					penaltyStocks = 0;
				}
			}
			else {
				yellowStocksToRemove = penaltyStocks;
				penaltyStocks = 0;
			}
		}
		else {
			redStocksToRemove = penaltyStocks;
			penaltyStocks = 0;
		}


		removeStocks(new Stocks(redStocksToRemove,yellowStocksToRemove,greenStocksToRemove,blueStocksToRemove));
		useBatteries(batteriesToRemove);



		if(penaltyStocks > 0)
			throw new OutOfStockException("There aren't enough penalty stocks or batteries to pay the penalty! However, you can keep playing :)");

	}

	/**
	 * Adder of garbage
	 * @param garbage garbage to add
	 */
	public void addGarbage(int garbage) {garbageHeap += garbage;}

	/**
	 * It checks if in a certain place there is an occupied cabin
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if it's an occupied cabin
	 */
	private boolean isComponentOccupiedCabin(int x, int y) {
		return switch(ship[y][x]) {
			case Cabin cabin -> cabin.isOccupied();
			default -> false;
		};
	}

	/**
	 * It checks if adjacent to a certain position there is an occupied cabin
	 * @see model.Cards.Epidemic
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if there is an adjacent occupied cabin
	 */
	private boolean hasAdjacentOccupiedCabin(int x, int y) {
		return  (y > 0 					 && isComponentOccupiedCabin(x,y-1)) ||
				(y < ship.length - 1 	 && isComponentOccupiedCabin(x,y+1)) ||
				(x > 0 					 && isComponentOccupiedCabin(x-1,y)) ||
				(x < ship[y].length  - 1 && isComponentOccupiedCabin(x+1,y));
	}

	/**
	 * It removes the connected crew in the cabins
	 */
	public void removeConnectedCrew() {
		//it has to remove a member of the crew (the player has to choose which one) for each cabin occupied
		ArrayList<Cabin> cabins = new ArrayList<>();

		for (int y = 0; y < ship.length; y++) {
			for (int x = 0; x < ship[y].length; x++) {
				//current component is Cabin
				switch (ship[y][x]){
					case Cabin cabin : {
						if (hasAdjacentOccupiedCabin(x, y))
							cabins.add(cabin);
					}
					default: {}
				}
            }
		}

		for (Cabin cabin : cabins) {
			if (cabin.getHumanEquip().get(0)) { //if humanEquip1 is true
				cabin.setHumans(false, cabin.getHumanEquip().get(1));//set humanEquip1, leave humanEquip2 as is
			}
			//if this code is reached, then humanEquip1 must be false
			else if (cabin.getHumanEquip().get(1)) { //if humanEquip2 is true
				cabin.setHumans(false, false); //set humanEquip2
			} else if (cabin.getBrownAlienEquip()) { //if brownAlienEquip is true
				cabin.setBrownAlien(false);//set brownAlienEquip
			} else if (cabin.getPurpleAlienEquip()) { //if purpleAlienEquip is true
				cabin.setPurpleAlien(false);//set purpleAlienEquip
			}
		}
	}
	// here a component can still be null

	/**
	 * It checks if the upper neighbor of a specific component is empty
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true il there's nothing or null or an unavailable slot
	 */
	private boolean isUpperNeighborEmpty(int x, int y) {return y - 1 < 0 || ship[y - 1][x] == null || isUnavailableSlot(x,y-1);}

	/**
	 * It checks if the right neighbor of a specific component is empty
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true il there's nothing or null or an unavailable slot
	 */
	private boolean isRightNeighborEmpty(int x, int y) {return x + 1 >= ship[y].length || ship[y][x + 1] == null || isUnavailableSlot(x+1,y);}

	/**
	 * It checks if the lower neighbor of a specific component is empty
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true il there's nothing or null or an unavailable slot
	 */
	private boolean isLowerNeighborEmpty(int x, int y) {return y + 1 >= ship.length || ship[y + 1][x] == null || isUnavailableSlot(x,y+1);}

	/**
	 * It checks if the left neighbor of a specific component is empty
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true il there's nothing or null or an unavailable slot
	 */
	private boolean isLeftNeighborEmpty(int x, int y) {return x - 1 < 0 || ship[y][x - 1] == null || isUnavailableSlot(x-1,y);}

	/**
	 * Getter of the south side of the upper neighbor
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return south side of the upper neighbor
	 */
	private Side getUpperNeighborSouthSide(int x, int y) {return (y - 1 >= 0) ? ship[y - 1][x].getSouthSide() : Side.BlankSide;}

	/**
	 * Getter of the west side of the right neighbor
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return west side of the right neighbor
	 */
	private Side getRightNeighborWestSide(int x, int y) {return (x + 1 < ship[y].length) ? ship[y][x + 1].getWestSide() : Side.BlankSide;}

	/**
	 * Getter of the north side of the lower neighbor
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return north side of the lower neighbor
	 */
	private Side getLowerNeighborNorthSide(int x, int y) {return (y + 1 < ship.length) ? ship[y + 1][x].getNorthSide() : Side.BlankSide;}

	/**
	 * Getter of the east side of the left neighbor
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return east side of the left neighbor
	 */
	private Side getLeftNeighborEastSide(int x, int y) {return (x - 1 >= 0) ? ship[y][x - 1].getEastSide() : Side.BlankSide;}

	/**
	 * It checks if the ShipComponent component would have a legal upper connection in the slot
	 * at the coordinates (destinationX, destinationY); it evaluates ONLY connector-connector.
	 * @param destinationX x coordinate of the component to evaluate
	 * @param destinationY y coordinate of the component to evaluate
	 * @param component component to evaluate
	 * @return true if in the north side there's a legal connector
	 */
	private boolean isNorthConnectionLegal(int destinationX, int destinationY, ShipComponent component){
		Side upperCompSouthSide = getUpperNeighborSouthSide(destinationX, destinationY);
		Side compNorthSide = component.getNorthSide();

		return (isSideUniversalConnector(compNorthSide) && isSideAnyConnector(upperCompSouthSide)) ||
				(isSideAnyConnector(compNorthSide) && isSideUniversalConnector(upperCompSouthSide)) ||
				(isSideSingleConnector(compNorthSide) && isSideSingleConnector(upperCompSouthSide)) ||
				(isSideDoubleConnector(compNorthSide) && isSideDoubleConnector(upperCompSouthSide));
	}

	/**
	 * It checks if the ShipComponent component would have a legal right connection in the slot
	 * at the coordinates (destinationX, destinationY); it evaluates ONLY connector-connector.
	 * @param destinationX x coordinate of the component to evaluate
	 * @param destinationY y coordinate of the component to evaluate
	 * @param component component to evaluate
	 * @return true if in the east side there's a legal connector
	 */
	private boolean isEastConnectionLegal(int destinationX, int destinationY, ShipComponent component){
		Side rightCompWestSide = getRightNeighborWestSide(destinationX, destinationY);
		Side compEastSide = component.getEastSide();

		return (isSideUniversalConnector(compEastSide) && isSideAnyConnector(rightCompWestSide)) ||
				(isSideAnyConnector(compEastSide) && isSideUniversalConnector(rightCompWestSide)) ||
				(isSideSingleConnector(compEastSide) && isSideSingleConnector(rightCompWestSide)) ||
				(isSideDoubleConnector(compEastSide) && isSideDoubleConnector(rightCompWestSide));
	}

	/**
	 * It checks if the ShipComponent component would have a legal lower connection in the slot
	 * at the coordinates (destinationX, destinationY); it evaluates ONLY connector-connector.
	 * @param destinationX x coordinate of the component to evaluate
	 * @param destinationY y coordinate of the component to evaluate
	 * @param component component to evaluate
	 * @return true if in the south side there's a legal connector
	 */
	private boolean isSouthConnectionLegal(int destinationX, int destinationY, ShipComponent component){
		Side lowerCompNorthSide = getLowerNeighborNorthSide(destinationX, destinationY);
		Side compSouthSide = component.getSouthSide();

		return (isSideUniversalConnector(compSouthSide) && isSideAnyConnector(lowerCompNorthSide)) ||
				(isSideAnyConnector(compSouthSide) && isSideUniversalConnector(lowerCompNorthSide)) ||
				(isSideSingleConnector(compSouthSide) && isSideSingleConnector(lowerCompNorthSide)) ||
				(isSideDoubleConnector(compSouthSide) && isSideDoubleConnector(lowerCompNorthSide));
	}

	/**
	 * It checks if the ShipComponent component would have a legal left connection in the slot
	 * at the coordinates (destinationX, destinationY); it evaluates ONLY connector-connector.
	 * @param destinationX x coordinate of the component to evaluate
	 * @param destinationY y coordinate of the component to evaluate
	 * @param component component to evaluate
	 * @return true if in the west side there's a legal connector
	 */
	private boolean isWestConnectionLegal(int destinationX, int destinationY, ShipComponent component){
		Side leftCompEastSide = getLeftNeighborEastSide(destinationX, destinationY);
		Side compWestSide = component.getWestSide();

		return (isSideUniversalConnector(compWestSide) && isSideAnyConnector(leftCompEastSide)) ||
				(isSideAnyConnector(compWestSide) && isSideUniversalConnector(leftCompEastSide)) ||
				(isSideSingleConnector(compWestSide) && isSideSingleConnector(leftCompEastSide)) ||
				(isSideDoubleConnector(compWestSide) && isSideDoubleConnector(leftCompEastSide));
	}

	/**
	 * It checks if the ShipComponent component would have a legal upper adjacency in the slot
	 * at the coordinates (destinationX, destinationY); it evaluates connector-connector, blank-blank
	 * and connector-empty slot
	 * @param destinationX x coordinate of the slot to evaluate
	 * @param destinationY y coordinate of the slot to evaluate
	 * @param component component to evaluate
	 * @return true if in the north side there's a legal connector or blank-blank or connector-empty slot
	 */
	private boolean isNorthAdjacencyLegal(int destinationX, int destinationY, ShipComponent component) {
		Side upperCompSouthSide = getUpperNeighborSouthSide(destinationX, destinationY);
		Side compNorthSide = component.getNorthSide();

		return (isNorthConnectionLegal(destinationX, destinationY, component) ||
				(isSideBlank(compNorthSide) && isSideBlank(upperCompSouthSide)) ||
				isUpperNeighborEmpty(destinationX, destinationY));
	}

	/**
	 * It checks if the ShipComponent component would have a legal right adjacency in the slot
	 * at the coordinates (destinationX, destinationY); it evaluates connector-connector, blank-blank
	 * and connector-empty slot
	 * @param destinationX x coordinate of the slot to evaluate
	 * @param destinationY y coordinate of the slot to evaluate
	 * @param component component to evaluate
	 * @return true if in the east side there's a legal connector or blank-blank or connector-empty slot
	 */
	private boolean isEastAdjacencyLegal(int destinationX, int destinationY, ShipComponent component) {
		Side rightCompWestSide = getRightNeighborWestSide(destinationX, destinationY);
		Side compEastSide = component.getEastSide();

		return (isEastConnectionLegal(destinationX, destinationY, component) ||
				(isSideBlank(compEastSide) && isSideBlank(rightCompWestSide)) ||
				isRightNeighborEmpty(destinationX, destinationY));
	}

	/**
	 * It checks if the ShipComponent component would have a legal lower adjacency in the slot
	 * at the coordinates (destinationX, destinationY); it evaluates connector-connector, blank-blank
	 * and connector-empty slot
	 * @param destinationX x coordinate of the slot to evaluate
	 * @param destinationY y coordinate of the slot to evaluate
	 * @param component component to evaluate
	 * @return true if in the south side there's a legal connector or blank-blank or connector-empty slot
	 */
	private boolean isSouthAdjacencyLegal(int destinationX, int destinationY, ShipComponent component) {
		Side lowerCompNorthSide = getLowerNeighborNorthSide(destinationX, destinationY);
		Side compSouthSide = component.getSouthSide();

		return (isSouthConnectionLegal(destinationX, destinationY, component) ||
				(isSideBlank(compSouthSide) && isSideBlank(lowerCompNorthSide)) ||
				isLowerNeighborEmpty(destinationX, destinationY));
	}

	/**
	 * It checks if the ShipComponent component would have a legal left adjacency in the slot
	 * at the coordinates (destinationX, destinationY); it evaluates connector-connector, blank-blank
	 * and connector-empty slot
	 * @param destinationX x coordinate of the slot to evaluate
	 * @param destinationY y coordinate of the slot to evaluate
	 * @param component component to evaluate
	 * @return true if in the west side there's a legal connector or blank-blank or connector-empty slot
	 */
	private boolean isWestAdjacencyLegal(int destinationX, int destinationY, ShipComponent component) {
		Side leftCompEastSide = getLeftNeighborEastSide(destinationX, destinationY);
		Side compWestSide = component.getWestSide();

		return (isWestConnectionLegal(destinationX, destinationY, component) ||
				(isSideBlank(compWestSide) && isSideBlank(leftCompEastSide)) ||
				isLeftNeighborEmpty(destinationX, destinationY));
	}

	/**
	 * It checks if the ShipComponent component would have a legal left, right, upper and lower adjacency in the slot
	 * at the coordinates (destinationX, destinationY); it evaluates connector-connector, blank-blank
	 * and connector-empty slot
	 * @param destinationX x coordinate of the slot to evaluate
	 * @param destinationY y coordinate of the slot to evaluate
	 * @param component component to evaluate
	 * @return true if in the north, south, west and east side there's a legal connector or blank-blank or connector-empty slot
	 */
	public boolean is_ADJACENCY_legal(int destinationX, int destinationY, ShipComponent component) {
		if(component.isType(ComponentType.UNAVAILABLE_SLOT)) return true;


		boolean northSideLegal = isNorthAdjacencyLegal(destinationX, destinationY, component);
		boolean eastSideLegal = isEastAdjacencyLegal(destinationX, destinationY, component);
		boolean southSideLegal = isSouthAdjacencyLegal(destinationX, destinationY, component);
		boolean westSideLegal = isWestAdjacencyLegal(destinationX, destinationY, component);

		return northSideLegal && eastSideLegal && southSideLegal && westSideLegal;
	}

	/**
	 * It removes all nulls with unavailable slots, it initializes all the attribute and
	 * it finalizes the cabins if they are connected to a life support
	 * @see #connectLifeSupports()
	 * @see #initializeShipAttributesFromComponents()
	 * @see #fillTheGaps()
	 */
	public void finalizeShip(){
		fillTheGaps();
		connectLifeSupports();
		initializeShipAttributesFromComponents();
	}

	/**
	 * It removes all the nulls with unavailable slots
	 */
	public void fillTheGaps(){
		for(int  y = 0; y < ship.length; y++) {
			for(int  x = 0; x < ship[y].length; x++) {
				if(ship[y][x] == null) {
					ship[y][x] = new UnavailableSlot();
					ship[y][x].setOrientation(Orientation.random());
				}
			}
		}
	}

	/**
	 * It checks if in the upper position there is a life support
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return 0 if there's no life support, 1 if there's a purple and 2 if there's a brown one
	 */
	private int isUpperNeighborLifeSupport(int x, int y) {
		if(y - 1 < 0) return 0;
		switch(ship[y-1][x]){
			case LifeSupport lifeSupport: {
				if (isNorthConnectionLegal(x, y, ship[y][x]))
					return lifeSupport.isColorPurple() ? 1 : 2;
				else
					return 0;
			}
			default: return 0;
		}
    }

	/**
	 * It checks if in the right position there is a life support
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return 0 if there's no life support, 1 if there's a purple and 2 if there's a brown one
	 */
	private int isRightNeighborLifeSupport(int x, int y) {
		if(x + 1 >= xUpperBound - xLowerBound) return 0;
		switch(ship[y][x+1]){
			case LifeSupport lifeSupport: {
				if (isEastConnectionLegal(x, y, ship[y][x]))
					return lifeSupport.isColorPurple() ? 1 : 2;
				else
					return 0;
			}
			default: return 0;
		}
    }

	/**
	 * It checks if in the lower position there is a life support
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return 0 if there's no life support, 1 if there's a purple and 2 if there's a brown one
	 */
	private int isLowerNeighborLifeSupport(int x, int y) {
		if(y + 1 >= yUpperBound - yLowerBound) return 0;
		switch(ship[y+1][x]){
			case LifeSupport lifeSupport: {
				if (isSouthConnectionLegal(x, y, ship[y][x]))
					return lifeSupport.isColorPurple() ? 1 : 2;
				else
					return 0;
			}
			default: return 0;
		}
    }

	/**
	 * It checks if in the left position there is a life support
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return 0 if there's no life support, 1 if there's a purple and 2 if there's a brown one
	 */
	private int isLeftNeighborLifeSupport(int x, int y) {
		if(x - 1 < 0) return 0;
		switch(ship[y][x - 1]){
			case LifeSupport lifeSupport: {
				if (isWestConnectionLegal(x, y, ship[y][x]))
					return lifeSupport.isColorPurple() ? 1 : 2;
				else
					return 0;
			}
			default: return 0;
		}
    }

	/**
	 * It checks if there is a purple life support connected to the position
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if there is a purple life support connected
	 */
	public boolean hasConnectedPurpleLifeSupport(int x, int y){
		return (isUpperNeighborLifeSupport(x,y) == 1 || isRightNeighborLifeSupport(x,y) == 1 ||
				isLowerNeighborLifeSupport(x,y) == 1 || isLeftNeighborLifeSupport(x,y) == 1);
	}

	/**
	 * It checks if there is a brown life support connected to the position
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if there is a brown life support connected
	 */
	public boolean hasConnectedBrownLifeSupport(int x, int y){
		return (isUpperNeighborLifeSupport(x,y) == 2 || isRightNeighborLifeSupport(x,y) == 2 ||
				isLowerNeighborLifeSupport(x,y) == 2 || isLeftNeighborLifeSupport(x,y) == 2);
	}

	/**
	 * It checks for all the cabins if they are connected to a life support,
	 * it marks them as with a life support
	 */
	public void connectLifeSupports(){
		for(int  y = 0; y < ship.length; y++) {
			for(int  x = 0; x < ship[y].length; x++) {
                if (Objects.requireNonNull(ship[y][x]) instanceof Cabin cabin) {
                    cabin.setBrownLifeSupport(hasConnectedBrownLifeSupport(x, y));
                    cabin.setPurpleLifeSupport(hasConnectedPurpleLifeSupport(x, y));
                }
			}
		}
	}

	/**
	 * It checks the ship, returning a matrix of boolean, indicating true the components
	 * which are not well-placed
	 * @return boolean matrix indicating positions of components not well-placed
	 */
	public boolean[][] checkShip() {
		boolean[][] wrongComponents = new boolean[ship.length][ship[0].length];
		//all null by default
		ShipComponent[][] componentsConnectedToCentralCabin = new ShipComponent[ship.length][ship[0].length];
		findComponentsConnectedTo(ship, 3,2, componentsConnectedToCentralCabin);

		for(int y = 0; y < ship.length; y++) {
			for(int x = 0; x < ship[y].length; x++) {
				if (ship[y][x] != null) {
					//set false as default
					wrongComponents[y][x] = !is_ADJACENCY_legal(x, y, ship[y][x]); //if the connection is LEGAL, wrongComponent[y][x] will be false and vice versa
					switch (ship[y][x]) {
						case Cannon cannon:
							if ((cannon.getNorthSide() == Side.CannonSpace && !isUpperNeighborEmpty(x, y)) ||
									(cannon.getEastSide() == Side.CannonSpace && !isRightNeighborEmpty(x, y)) ||
									(cannon.getSouthSide() == Side.CannonSpace && !isLowerNeighborEmpty(x, y)) ||
									(cannon.getWestSide() == Side.CannonSpace && !isLeftNeighborEmpty(x, y))) {
								wrongComponents[y][x] = true;
							}
							break;
						case Engine engine:
							if((engine.getNorthSide()==Side.MotorSpace) ||
									(engine.getEastSide()==Side.MotorSpace) ||
									(engine.getWestSide()==Side.MotorSpace) ||
									(engine.getSouthSide()==Side.MotorSpace && !isLowerNeighborEmpty(x,y))) {
								wrongComponents[y][x] = true;
							}
							break;
						default: break;
					}

					if(!isUnavailableSlot(x, y) && componentsConnectedToCentralCabin[y][x] == null) {
						wrongComponents[y][x] = true;
					}
				}
				else {
					wrongComponents[y][x] = false;
				}
			}
		}

		return wrongComponents;
	}

	/**
	 * It checks a matrix of boolean, which represents the ship
	 * the true places are not well-placed components
	 * @param wrongComponents matrix of boolean, which represents with true
	 *                        the positions of wrong components
	 * @return true if the ship has to be fixed
	 */
	public boolean shipNeedsFixing(boolean[][] wrongComponents) {
		for(int y = 0; y < ship.length; y++) {
			for(int x = 0; x < ship[y].length; x++) {
				if(wrongComponents[y][x]) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Getter of present double motors in the entire ship
	 * @return number of present double motors
	 */
	public int getDoubleMotors(){
		int doubleMotors = 0;
        for (ShipComponent[] shipComponents : ship) {
            for (ShipComponent shipComponent : shipComponents) {
                if (Objects.requireNonNull(shipComponent) instanceof Engine engine) {
                    if (engine.isDouble())
                        doubleMotors++;
                }
            }
        }

		return doubleMotors;
	}

	/**
	 * Getter of present double cannons in the entire ship
	 * @return number of present double cannons
	 */
	public int getDoubleCannons(){
		int doubleCannons = 0;
        for (ShipComponent[] shipComponents : ship) {
            for (ShipComponent shipComponent : shipComponents) {
                if (Objects.requireNonNull(shipComponent) instanceof Cannon cannon) {
                    if (cannon.isDouble())
                        doubleCannons++;
                }
            }
        }

		return doubleCannons;
	}

	/**
	 * In presence of a big meteor it scrolls all the components in order to find eligible
	 * cannons in the four sides according to the trajectory of the big meteor
	 * @param bigMeteor current big meteor projectile
	 * @param trajectory projectile trajectory
	 * @return list of current eligible cannons
	 */
	public ArrayList<Cannon> getEligibleCannons(Projectile bigMeteor, int trajectory) {
		ArrayList<Cannon> eligibleCannons = new ArrayList<>();

		for(int y = 0; y < ship.length; y++){
			for(int x = 0; x < ship[y].length; x++){
                if (Objects.requireNonNull(ship[y][x]) instanceof Cannon curCannon) {
                    switch (bigMeteor.getDirection()) {
                        case SOUTH:
                            if (x == trajectory && curCannon.getNorthSide() == Side.CannonSpace)
                                eligibleCannons.add(curCannon);
                            break;
                        case WEST:
                            if (y >= trajectory - 1 && y <= trajectory + 1 && curCannon.getEastSide() == Side.CannonSpace)
                                eligibleCannons.add(curCannon);
                            break;
                        case NORTH:
                            if (x >= trajectory - 1 && x <= trajectory + 1 && curCannon.getSouthSide() == Side.CannonSpace)
                                eligibleCannons.add(curCannon);
                            break;
                        case EAST:
                            if (y >= trajectory - 1 && y <= trajectory + 1 && curCannon.getWestSide() == Side.CannonSpace)
                                eligibleCannons.add(curCannon);
                            break;
                    }
                }
			}
		}

		return eligibleCannons;
	}

	/**
	 * It initializes all the attributes in the ship dashboard according to the components in it
	 */
	public void initializeShipAttributesFromComponents(){
		this.batteries = 0;
		this.staticFirePower = 0;
		this.staticMotorPower = 0;
		this.humans = 0;
		this.purpleAlienPresent = false;
		this.brownAlienPresent = false;
		this.stocks = new Stocks(0,0,0,0);
        for (int y = 0; y < ship.length; y++) {
            for (int x = 0; x < ship[y].length; x++) {
                if (ship[y][x] == null)
                    break;
                //else
                switch (ship[y][x]) {
                    case Cabin cabin:
                        humans += cabin.getHumans();
						if(!hasConnectedPurpleLifeSupport(x, y))
							cabin.setPurpleAlien(false);
						if(!hasConnectedBrownLifeSupport(x, y))
							cabin.setBrownAlien(false);
						if(cabin.getPurpleAlienEquip()){
							if(purpleAlienPresent)
								throw new TooManyAliensException("Tried to add another purple alien when there was one already in the ship.");
							//else
							purpleAlienPresent = cabin.getPurpleAlienEquip();
						}

						if(cabin.getBrownAlienEquip()){
							if(brownAlienPresent)
								throw new TooManyAliensException("Tried to add another brown alien when there was one already in the ship.");
							//else
							brownAlienPresent = cabin.getBrownAlienEquip();
						}
                        break;
                    case Cannon cannon:
                        if (cannon.isDouble()) {
                            if (cannon.getNorthSide() == Side.CannonSpace)
                                staticFirePower += 0;
                            else if (cannon.getEastSide() == Side.CannonSpace || cannon.getWestSide() == Side.CannonSpace ||
                                    cannon.getSouthSide() == Side.CannonSpace)
                                staticFirePower += 0;
                        } else {
                            if (cannon.getNorthSide() == Side.CannonSpace)
                                staticFirePower += 1;
                            else if (cannon.getEastSide() == Side.CannonSpace || cannon.getWestSide() == Side.CannonSpace ||
                                    cannon.getSouthSide() == Side.CannonSpace)
                                staticFirePower += 0.5;
                        }
                        break;
                    case Engine engine:
                        if (engine.isDouble())
                            staticMotorPower += 0;
                        else
                            staticMotorPower += 1;
                        break;
                    case BatteryComponent batteryComponent:
                        batteries += batteryComponent.getCurrentBatteries();
                        break;
                    case CargoHold cargoHold:
                        stocks.add(cargoHold.getStocks().get());
                        break;
                    default:
                        break;
                }
            }
        }

		if(purpleAlienPresent)
			staticFirePower += 2;
		if(brownAlienPresent)
			staticMotorPower += 2;
	}

	/**
	 * It used to throw a floating component exception for a floating component which
	 * wants to be added
	 * @see #addComponent(ShipComponent, int, int, boolean)
	 * @param comp current component
	 * @param x x component coordinate
	 * @param y y component coordinate
	 */
	public void addComponent(ShipComponent comp, int x, int y){addComponent(comp, x, y, true);}

	/**
	 * It adds the current component, when the exceptions are not thrown
	 * @param comp current component
	 * @param x current x coordinate
	 * @param y current y coordinate
	 * @param throwsFloatingComponentException true if the component is a floating one
	 * @throws IllegalPositionException thrown when the component is not part of the ship
	 * @throws SlotTakenException thrown if the slot is already taken
	 * @throws FloatingComponentException thrown when the component is not attached to anything
	 */
	public void addComponent(ShipComponent comp, int x, int y, boolean throwsFloatingComponentException) throws IllegalPositionException, SlotTakenException, FloatingComponentException {
		switch (ship[y][x]) {
			case UnavailableSlot _ -> throw new IllegalPositionException("Slot isn't part of the ship");
			case null -> {
				if(throwsFloatingComponentException && isUpperNeighborEmpty(x,y) && isRightNeighborEmpty(x,y) && isLowerNeighborEmpty(x,y) && isLeftNeighborEmpty(x,y))
					throw new FloatingComponentException("The component wasn't attached to anything");
				else
					ship[y][x] = comp;
			}
			default -> throw new SlotTakenException("Slot is already taken");
		}
	}

	/**
	 * Method used to scan every component and search if the component is one of the pieces
	 * of the broken ship
	 * @return list of components attached to the broken part of a ship
	 */
	public ArrayList<ShipComponent[][]> getBrokenPieces(){
		boolean componentAlreadyScanned;
		ArrayList<ShipComponent[][]> brokenShipPieces = new ArrayList<>();

		//scan every component left
		for(int cy = 0; cy < ship.length; cy++) {
			for (int cx = 0; cx < ship[cy].length; cx++) {
				//check if it's already part of one of the pieces of the broken ship
				componentAlreadyScanned = false;
				for (ShipComponent[][] brokenPiece : brokenShipPieces) {
					//purposely check for instance correspondence
					if (brokenPiece[cy][cx] == ship[cy][cx]) {
						componentAlreadyScanned = true;
						break;
					}
				}
				if (!componentAlreadyScanned && !isUnavailableSlot(cx, cy)) {
					//initialized to all nulls by default
					ShipComponent[][] newBrokenPiece = new ShipComponent[ship.length][ship[0].length];
					findComponentsConnectedTo(ship, cx, cy, newBrokenPiece);
					brokenShipPieces.add(newBrokenPiece);
				}
				//else go to next component
			}
		}

		return brokenShipPieces;
	}

	// /!\ WARNING: In this method the "null" state is used to check if a component has been visited or not,
	//				due to this if the ship[][] contains null components it WILL cause a LOOP.
	//				Remember to set all null components to UnavailableSlot once the ship is finalized.

	/**
	 * If the adjacent component is 1) within index range, 2) connected to the current component,
	 * and 3) hasn't been visited yet, recursively find the components connected to that one
	 * /!\ if indexes are outside of range then the Side is initialized as blank and the condition will be false,
	 * avoiding the check on connectedComponents[...][...].
	 * When all nearby components either have been visited already or aren't connected to the current one, return
	 * @param ship current ship
	 * @param startingX first x component coordinate
	 * @param startingY first y component coordinate
	 * @param connectedComponents matrix of connected components
	 */
	public void findComponentsConnectedTo(ShipComponent[][] ship, int startingX, int startingY, ShipComponent[][] connectedComponents) {
		//purposely copy reference instead of data
		connectedComponents[startingY][startingX] = ship[startingY][startingX];

		// by this point in the game the connections are already legal so there's no need to re-check them specifically
		boolean curNsideIsConnector= isSideAnyConnector(ship[startingY][startingX].getNorthSide());
		boolean curEsideIsConnector= isSideAnyConnector(ship[startingY][startingX].getEastSide());
		boolean curSsideIsConnector= isSideAnyConnector(ship[startingY][startingX].getSouthSide());
		boolean curWsideIsConnector= isSideAnyConnector(ship[startingY][startingX].getWestSide());

		Side UpCompSside, RightCompWside, DownCompNside, LeftCompEside;
		boolean UpCompSsideIsConnector, RightCompWsideIsConnector, DownCompNsideIsConnector, LeftCompEsideIsConnector;

		//initialize convenience variables based on index range to simplify "if" statements and readability
		UpCompSside =    getUpperNeighborSouthSide(startingX, startingY); // 1)
		RightCompWside = getRightNeighborWestSide(startingX, startingY); // 1)
		DownCompNside =  getLowerNeighborNorthSide(startingX, startingY); // 1)
		LeftCompEside =  getLeftNeighborEastSide(startingX, startingY); // 1)

		UpCompSsideIsConnector =   isSideAnyConnector(UpCompSside);
		RightCompWsideIsConnector = isSideAnyConnector(RightCompWside);
		DownCompNsideIsConnector = isSideAnyConnector(DownCompNside);
		LeftCompEsideIsConnector = isSideAnyConnector(LeftCompEside);

		//if the adjacent component is 1) within index range, 2) connected to the current component,
		// and 3) hasn't been visited yet, recursively find the components connected to that one
		// /!\ if indexes are outside of range then the Side is initialized as blank and the condition will be false,
		// 		avoiding the check on connectedComponents[...][...]
		if(curNsideIsConnector && UpCompSsideIsConnector && // 2)
				(connectedComponents[startingY - 1][startingX] == null)) {	 // 3)
			findComponentsConnectedTo(ship, startingX, startingY - 1, connectedComponents);
		}
		if(curEsideIsConnector && RightCompWsideIsConnector && // 2)
				(connectedComponents[startingY][startingX + 1] == null)) {	 // 3)
			findComponentsConnectedTo(ship, startingX + 1, startingY, connectedComponents);
		}
		if(curSsideIsConnector && DownCompNsideIsConnector && // 2)
				(connectedComponents[startingY + 1][startingX] == null)) {	 // 3)
			findComponentsConnectedTo(ship, startingX, startingY + 1, connectedComponents);
		}
		if(curWsideIsConnector && LeftCompEsideIsConnector && // 2)
				(connectedComponents[startingY][startingX - 1] == null)) {	 // 3)
			findComponentsConnectedTo(ship, startingX - 1, startingY, connectedComponents);
		}

		//when all nearby components either have been visited already or aren't connected to the current one, return
	}

	/**
	 * Method used to adjust an out of offset trajectory to a legit one according to the ship
	 * @param unoffsettedTrajectory current trajectory to be adjusted
	 * @param projectile current projectile
	 * @return trajectory in the offset
	 */
	public int convertTrajectoryToShipSpace(int unoffsettedTrajectory, Projectile projectile) {
		return (projectile.getDirection() == Orientation.NORTH || projectile.getDirection() == Orientation.SOUTH) ?
				unoffsettedTrajectory - xLowerBound : unoffsettedTrajectory - yLowerBound;
	}

	/**
	 * According to the projectile direction, the trajectory and the coordinate it returns
	 * the hit component coordinates, in case of no hit components it returns double -1 coordinates
	 * @param projectile current projectile
	 * @param trajectory current projectile trajectory
	 * @param hitCoordinates current hit coordinates
	 * @return current hit component coordinates
	 */
	public int[] getHitComponent(Projectile projectile, int trajectory, int[] hitCoordinates) {

		switch (projectile.getDirection()) {
			case SOUTH:
				if(trajectory < 0 || trajectory >= getShipWidth())
					break;
				//else
				for(int y = 0; y < ship.length; y++){
					if(!isUnavailableSlot(trajectory, y)) {
						hitCoordinates[0] = trajectory;
						hitCoordinates[1] = y;
						return hitCoordinates;
					}
				}
				break;
			case WEST:
				if(trajectory < 0 || trajectory >= getShipHeight())
					break;
				//else
				for(int x = ship[trajectory].length - 1; x >= 0; x--){
					if(!isUnavailableSlot(x,trajectory)) {
						hitCoordinates[0] = x;
						hitCoordinates[1] = trajectory;
						return hitCoordinates;
					}
				}
				break;
			case NORTH:
				if(trajectory < 0 || trajectory >= getShipWidth())
					break;
				//else
				for(int y = ship.length - 1; y >= 0; y--){
					if(!isUnavailableSlot(trajectory, y)) {
						hitCoordinates[0] = trajectory;
						hitCoordinates[1] = y;
						return hitCoordinates;
					}
				}
				break;
			case EAST:
				if(trajectory < 0 || trajectory >= getShipHeight())
					break;
				//else
				for(int x = 0; x < ship[trajectory].length; x++){
					if(!isUnavailableSlot(x,trajectory)) {
						hitCoordinates[0] = x;
						hitCoordinates[1] = trajectory;
						return hitCoordinates;
					}
				}
				break;

		}

		hitCoordinates[0] = -1;
		hitCoordinates[1] = -1;

		return hitCoordinates;
	}

	/**
	 * Getter of the entire ship
	 * @return matrix of ship components representing the ship
	 */
	public ShipComponent[][] getShip() {return ship;}

	/**
	 * It adds a specific component to the list of booked components
	 * @param component component to book
	 * @throws BookingSlotsFullException thrown when you try to book more than 2 components
	 */
	public void bookComponent(ShipComponent component) throws BookingSlotsFullException {
		if(bookedComponents.size() < 2)
			bookedComponents.add(component);
		else throw new BookingSlotsFullException("There were already 2 booked components.");
	}

	/**
	 * It returns the entire list of booked components
	 * @return list of booked components of the player
	 */
	public ArrayList<ShipComponent> getBookedComponents() {return bookedComponents;}

	/**
	 * It returns a certain booked component
	 * @param index index of the booked component to find
	 * @return booked component
	 */
	public ShipComponent getBookedComponent(int index) {
		if (index < this.bookedComponents.size()) {
			return bookedComponents.get(index);
		}
		else
			throw new IndexOutOfBoundsException();
	}

	/**
	 * It removes a specific booked component
	 * @param index index of the component
	 */
	public void removeBookedComponent(int index){
		if (index < this.bookedComponents.size()) {
			bookedComponents.remove(index);
		}
		else
			throw new IndexOutOfBoundsException();
	}

	/**
	 * It moves the booked components to the garbage heap
	 */
	public void addBookedComponentsToGarbage(){
		addGarbage(bookedComponents.size());
		bookedComponents.clear();
	}

	/**
	 * It sets an unavailable slot, adding the garbage
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void removeComponent(int x, int y){
		ship[y][x] = new UnavailableSlot();
		addGarbage(1);
	}

	/**
	 * It adds the credits, removing the days to move
	 * @param daysLost lost days
	 * @param creditsGained amount of gained credits
	 */
	public void bonusMalus(int daysLost, int creditsGained) {
		daysToMove -= daysLost;
		credits += creditsGained;
	}

	/**
	 * For all cabins it removes the crew and it initializes as removed also
	 * the relatives attributes
	 */
	public void removeAllCrewmates(){
		for(int y = 0; y < ship.length; y++){
			for(int x = 0; x < ship[y].length; x++){
				if(ship[y][x] instanceof Cabin cabin){
					cabin.removeCrew();
				}
			}
		}
		humans = 0;
		purpleAlienPresent = false;
		brownAlienPresent = false;
	}

	/**
	 * Clone methode, used in order to have an identical cloned object
	 * @return cloned object
	 */
    @Override
    public ShipDashboard clone() {
        try {
            ShipDashboard clone = (ShipDashboard) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}