package View.tuiGraphics;

import View.ColorManagement.ColorRGB;
import View.ColorManagement.ConsoleColor;
import model.DifferentShipComponents.ShipComponent;
import model.DifferentShipComponents.UnavailableSlot;
import model.Projectiles.Orientation;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class used to represent the ship, using ppm images for the components
 */
public class ShipStringBuilder {

	/**
	 * Attribute used to store the matrix of the ship components
	 */
	ShipComponent[][] ship;

	/**
	 * Final attribute used to represent a character color
	 */
	private final int colorCharLength = 6;

	/**
	 * Final attribute used to represent all the character colors
	 */
	private final int pixelCharLength = colorCharLength * 2 + 1;

	/**
	 * Final attribute used to represent width and height of the components
	 */
	private final int compW, compH;

	/**
	 * Final attribute used to store the lower bounds of the ship matrix
	 */
	private final int xLowerBound, yLowerBound;

	/**
	 * Final attribute used to represent the size of indexes
	 */
	private final int indexSize = 4;

	/**
	 * Final attribute used to represent the ship width
	 */
	private final int shipW;

	/**
	 * Final attribute used to represent the ship height
	 */
    private final int shipH;

	/**
	 * Attribute used to store the string builder that builds the ship image
	 */
	StringBuilder shipString;

	/**
	 * Flag used to store if the ship is rendered for the first
	 */
	boolean firstTime = true;

	/**
	 * Attribute used to store the console type
	 */
	ConsoleType consoleType;

	/**
	 * Final attribute used to store the matrix of boolean that used to track the places of old wrong components
	 */
	private final boolean[][] oldWrongComponents;

	/**
	 * Constructor of the ship string builder
	 * @param shipH represents the height of the ship matrix
	 * @param shipW represents the width of the ship matrix
	 * @param xLowerBound represents the minimum x coordinate
	 * @param yLowerBound represents the minimum y coordinate
	 * @param compW represents the width of each component
	 * @param type represents the console type
	 */
	public ShipStringBuilder(int shipW, int shipH, int compW, int xLowerBound, int yLowerBound, ConsoleType type) {
		if(shipH == 0 ||shipW == 0 ||compW == 0)
			throw new RuntimeException("The ship or the components have a 0-sized dimension.");
		//else
		this.shipW = shipW;
		this.shipH = shipH;
		this.compW = compW;
		this.xLowerBound = xLowerBound;
		this.yLowerBound = yLowerBound;
		this.consoleType = type;
		double charBoxRatio = (new StringImage(consoleType).charBoxRatio);
		this.compH = (int)(this.compW / charBoxRatio);
		resetShipString();
		this.ship = new ShipComponent[shipH][shipW];
		this.oldWrongComponents = new boolean[shipH][shipW];
	}

	/**
	 * Method used to draw a specific component on the ship
	 * it invokes {@link #drawCompOnString(String, int, int, int, double, ColorRGB)}
	 * @param pathName is the current path
	 * @param compX is the x coordinate of the component
	 * @param compY is the y coordinate of the component
	 * @param CC90DegRotations represents the rotation
	 */
	private void drawCompOnString(String pathName, int compX, int compY, int CC90DegRotations) throws IOException {
		drawCompOnString(pathName, compX, compY, CC90DegRotations, 0, new ColorRGB(0,0,0));
	}

	/**
	 * Method used to draw a specific component on the ship with a specified position, orientation and color
	 * @param pathName is the current path
	 * @param compX is the x coordinate of the component
	 * @param compY is the y coordinate of the component
	 * @param CC90DegRotations represents the rotation
	 * @param tintAmount represents how much is tinted (0 means no tint and 1 full)
	 * @param tintColor represents the color
	 */
	private void drawCompOnString(String pathName, int compX, int compY, int CC90DegRotations, double tintAmount, ColorRGB tintColor) throws IOException {
		int rowLength = shipString.indexOf("\n") + 1;
		String component;
		StringImage stringImg = new StringImage(consoleType, compW);
		component = stringImg.computeAndGetStringImage(pathName, CC90DegRotations, tintAmount, tintColor);
						         //skip line for top indexes
		int compFirstY = 1 + ((compY) * compH);
		//					number of "console pixels" in width per component * number of characters per console pixel +
		//					length of a console color for the final RESET
		int compCharLengthWithReset = compW * pixelCharLength + colorCharLength;
		int compFirstX = (compX * (compCharLengthWithReset)) + indexSize; // if there was a RESET because the component was the last of the row, we write over it
		int compLastX = ((compX + 1) * (compCharLengthWithReset)) + indexSize; // we write a new RESET at the

		if(component.indexOf('\n') + 1 != compCharLengthWithReset + 1){
			throw new RuntimeException("Unexpected component sizes:" + (component.indexOf('\n') + 1) + " and " + (compW * pixelCharLength));
		}

		for(int  y = 0; y < compH; y++){
			shipString.replace((compFirstY + y) * rowLength + compFirstX,(compFirstY + y) * rowLength + compLastX,
					//																  +1 to keep track of '\n' vvv
					component.substring(y * (compCharLengthWithReset + 1), (y + 1) * (compCharLengthWithReset + 1) - 1));
			//													  -1 because we don't want to COPY the \n   ^^^
		}
	}

	/**
	 * Method used to reset the ship string
	 */
	public void resetShipString(){
		shipString = new StringBuilder();
		for(int i = 0; i < shipH * compH + 2; i++){
			shipString.append(" ".repeat(indexSize));
			for(int j = 0; j < shipW; j++){
				for(int k = 0; k < compW; k++){
					shipString.append("\u001B[030m\u001B[040m ");
				}
				shipString.append("\u001B[000m");
			}
			shipString.append(" ".repeat(indexSize));
			shipString.append("\n");
		}
	}

	/**
	 * Getter of the Orientation enum represented by the number of clockwise rotations of 90 degree
	 * @param orientation is the orientation to convert
	 * @return the corresponding integer
	 */
	private int getCounterClockwise90DegRotationFromOrientation(Orientation orientation){
		return switch (orientation) {
			case NORTH -> 0;
			case EAST -> 3;
			case SOUTH -> 2;
			case WEST -> 1;
			default -> throw new RuntimeException("Unexpected orientation: " + orientation);
		};
	}

	/**
	 * Method used to check if two components are equal
	 * @param comp1 is the first component considered
	 * @param comp2 is the second component considered
	 * @return a boolean that is true if the two components are equal
	 */
	private boolean componentsAreEqual(ShipComponent comp1, ShipComponent comp2){
		return (comp1 == null && comp2 == null) || (comp1 != null && comp1.equals(comp2));
	}

	/**
	 * Method used to draw the horizontal coordinate indexes of the ship
	 * @param firstLine indicates if the indexes have to be drawn on the top
	 */
	private void drawHorizontalIndexes(boolean firstLine){
		StringBuilder indexes = new StringBuilder();
		int rowLength = shipString.indexOf("\n") + 1;

		indexes.append(" ".repeat((compW - (int)(Math.log10((xLowerBound == 0 ? 1 : xLowerBound)) + 1) - 1) / 2));
		for(int i = 0; i < shipW; i++){
			int index = i + xLowerBound;
			indexes.append((index)).append(":");
			if(i != shipW - 1)
				indexes.append(" ".repeat(compW - (int)(Math.log10((index == 0 ? 1 : index)) + 1) - 1));
		}

		indexes.append(" ".repeat(pixelCharLength - ((indexes.length()) % pixelCharLength)));

		if(firstLine)
			shipString.replace(indexSize,indexSize + indexes.length(), indexes.toString());
		else
			shipString.replace((shipH * compH + 1) * rowLength + indexSize,
					(shipH * compH + 1) * rowLength + indexSize + indexes.length(), indexes.toString());
	}

	/**
	 * Method used to draw the vertical coordinate indexes of the ship
	 */
	private void drawBothVerticalIndexes(){
		String curIndex;
		int rowLength = shipString.indexOf("\n") + 1;
		for(int i = 0; i < shipH; i++) {
			curIndex = (yLowerBound + i) + ":";
			shipString.replace((int)(compH / 2.0 + i * compH) * rowLength,
					(int)(compH / 2.0 + i * compH) * rowLength + curIndex.length(), curIndex);

			curIndex = ":" + (yLowerBound + i);
			shipString.replace((int)(compH / 2.0 + i * compH + 1) * rowLength - 5,
					(int)(compH / 2.0 + i * compH + 1) * rowLength - 5 + curIndex.length(), curIndex);
		}
	}

	/**
	 * Method used to draw the full string ship
	 * @param ship represents the matrix of ship components to draw
	 * @return the string representation
	 */
	public String buildShipString(ShipComponent[][] ship) throws IOException {
		String curFilename;
		int curCompID = -1;
		Orientation curCompOrientation;
	//	resetShipString();
		drawHorizontalIndexes(true);

		for(int y = 0; y < ship.length; y++){
			for(int x = 0; x < ship[y].length; x++){
				if(!firstTime && this.ship != null && componentsAreEqual(this.ship[y][x], ship[y][x]))
					continue;

				//else, the string needs to be updated there:
				curCompID = ship[y][x] == null ? (-1 + 1) : ship[y][x].getID() + 1;
				curFilename = "/components/comp" + curCompID + ".ppm";
				curCompOrientation = ship[y][x] == null ? Orientation.random() : ship[y][x].getOrientation();
				if(ship[y][x] != null && ship[y][x] instanceof UnavailableSlot)
					drawCompOnString(curFilename, x, y, getCounterClockwise90DegRotationFromOrientation(curCompOrientation),
							0.2, new ColorRGB(127, 127, 127));
				else
					drawCompOnString(curFilename, x, y, getCounterClockwise90DegRotationFromOrientation(curCompOrientation));
			}
		}

		drawHorizontalIndexes(false);

		drawBothVerticalIndexes();

		if(firstTime)
			firstTime = false;

		for(int y = 0; y < this.shipH; y++){
			for(int x = 0; x < this.shipW; x++){
				if(!componentsAreEqual(this.ship[y][x], ship[y][x]))
					this.ship[y][x] = ship[y][x] == null ? null : ship[y][x].clone();
			}
		}


		return shipString.toString();
	}

	/**
	 * Method used to draw the full string ship with highlighted errors
	 * @param ship represents the matrix of ship components to draw
	 * @param wrongComponents represents the boolean matrix that indicates which components are wrong, in order to mark them in red
	 * @return the string representation
	 */
	public String buildShipToBeFixedString(ShipComponent[][] ship, boolean[][] wrongComponents) throws IOException {
		String curFilename;
	//	resetShipString();
		drawHorizontalIndexes(true);

		for(int y = 0; y < ship.length; y++){
			for(int x = 0; x < ship[y].length; x++){
				curFilename = "/components/comp" + (ship[y][x].getID() + 1) + ".ppm";
				if(wrongComponents[y][x])
					drawCompOnString(curFilename, x, y, getCounterClockwise90DegRotationFromOrientation(ship[y][x].getOrientation()),
							0.2, new ColorRGB(215,42,0));
				else if(this.ship == null || !componentsAreEqual(this.ship[y][x], ship[y][x]) || wrongComponents[y][x] != oldWrongComponents[y][x])
					drawCompOnString(curFilename, x, y, getCounterClockwise90DegRotationFromOrientation(ship[y][x].getOrientation()));
			}
		}

		drawHorizontalIndexes(false);
		drawBothVerticalIndexes();

		for(int y = 0; y < this.shipH; y++){
			for(int x = 0; x < this.shipW; x++){
				this.ship[y][x] = ship[y][x] == null ? null : ship[y][x].clone();
				this.oldWrongComponents[y][x] = wrongComponents[y][x];
			}
		}
		return shipString.toString();
	}

	/**
	 * Method used to draw the full string ship with a colored cabin
	 * @param ship represents the matrix of ship components to draw
	 * @param cabinX represents the X coordinate of the cabin to color
	 * @param cabinY represents the Y coordinate of the cabin to color
	 * @return the string representation
	 */
	public String buildShipWithColoredCabin(ShipComponent[][] ship, int cabinX, int cabinY) throws IOException {
		String curFilename;
		//	resetShipString();
		drawHorizontalIndexes(true);

		for(int y = 0; y < ship.length; y++){
			for(int x = 0; x < ship[y].length; x++){
				curFilename = "/components/comp" + (ship[y][x].getID() + 1) + ".ppm";
				if(cabinX == x && cabinY == y)
					drawCompOnString(curFilename, x, y, getCounterClockwise90DegRotationFromOrientation(ship[y][x].getOrientation()),
							0.2, new ColorRGB(0,215,0));
				else if(this.ship == null || !componentsAreEqual(this.ship[y][x], ship[y][x]))
					drawCompOnString(curFilename, x, y, getCounterClockwise90DegRotationFromOrientation(ship[y][x].getOrientation()));
			}
		}

		drawHorizontalIndexes(false);
		drawBothVerticalIndexes();

		for(int y = 0; y < this.shipH; y++){
			for(int x = 0; x < this.shipW; x++){
				this.ship[y][x] = ship[y][x] == null ? null : ship[y][x].clone();
			}
		}
		return shipString.toString();
	}

	/**
	 * Method used to build a ship string from the array of ship components
	 * @param ship represents the matrix of ship components to build
	 * @return the string representation of the ship
	 */
	public String buildShipString(ArrayList<ArrayList<ShipComponent>> ship) throws IOException {
		String curFilename;

		drawHorizontalIndexes(true);

		for(int y = 0; y < ship.size(); y++){
			for(int x = 0; x < ship.get(y).size(); x++){
				curFilename = "/components/comp" + (ship.get(y).get(x).getID() + 1) + ".ppm";
				drawCompOnString(curFilename, x, y, getCounterClockwise90DegRotationFromOrientation(ship.get(y).get(x).getOrientation()));
			}
		}

		drawHorizontalIndexes(false);
		drawBothVerticalIndexes();

		return shipString.toString();
	}

}
