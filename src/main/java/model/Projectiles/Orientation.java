package model.Projectiles;

import java.io.Serializable;

/**
 * Enumeration useful to indicate the orientation of components or projectiles
 */
public enum Orientation implements Serializable {

	/**
	 * Different orientations
	 */
	NORTH, EAST, SOUTH, WEST;

	/**
	 * Method called to have a clockwise rotation of 90 degrees
	 * @param orientation current orientation, which has to be rotated
	 * @return new orientation rotated
	 */
	public static Orientation clockwise90degRotation(Orientation orientation) {
		if (orientation == null)
			return null;

		return switch (orientation) {
			case NORTH -> Orientation.EAST;
			case EAST -> Orientation.SOUTH;
			case SOUTH -> Orientation.WEST;
			case WEST -> Orientation.NORTH;
		};
	}

	/**
	 * Method called to have a counterclockwise rotation of 90 degrees
	 * @param orientation current orientation, which has to be rotated
	 * @return new orientation rotated
	 */
	public static Orientation counterClockwise90degRotation(Orientation orientation) {
		if (orientation == null)
			return null;

		return switch (orientation) {
			case NORTH -> Orientation.WEST;
			case WEST -> Orientation.SOUTH;
			case SOUTH -> Orientation.EAST;
			case EAST -> Orientation.NORTH;
		};
	}

	/**
	 * Method called to have a clockwise rotation of 180 degrees
	 * @param orientation current orientation, which has to be rotated
	 * @return new orientation rotated
	 */
	public static Orientation rotation180degrees(Orientation orientation) {
		if (orientation == null)
			return null;

		return switch (orientation) {
			case NORTH -> Orientation.SOUTH;
			case EAST -> Orientation.WEST;
			case SOUTH -> Orientation.NORTH;
			case WEST -> Orientation.EAST;
		};
	}

	/**
	 * It returns the number of rotations applied on an object orientation
	 * @param orientation object orientation to evaluate
	 * @return number of rotations
	 */
	public static int getCounterClockwise90DegRotationFromOrientation(Orientation orientation){
		return switch (orientation) {
			case NORTH -> 0;
			case EAST -> 3;
			case SOUTH -> 2;
			case WEST -> 1;
        };
	}

	/**
	 * Method which applies a specific number of rotations
	 * @param orientation object orientation to update
	 * @param rotations90degrees number of rotations
	 * @return object orientation with rotations applied
	 */
	public static Orientation clockwiseRotation(Orientation orientation, int rotations90degrees) {
		int size = 4;
		//gives the number of clockwise 90 degree rotations in between 0 and 3 (90,180,270, and 0)
		rotations90degrees = (size + (rotations90degrees) % size) % size;

		switch (rotations90degrees) {
			case 0: return orientation;
			//90 degrees
			case 1: return clockwise90degRotation(orientation);
			//180 degrees
			case 2: return rotation180degrees(orientation);
			//270 degrees clockwise, corresponds to a 90 degree COUNTER clockwise rotation
			case 3: return counterClockwise90degRotation(orientation);

			default: throw new AssertionError("This should never happen");
		}
	}

	/**
	 * It returns a random orientation
	 * @return random orientation object
	 */
	public static Orientation random() {
		int randVal = (int)(Math.random() * 4);
		return switch (randVal) {
			case 0 -> Orientation.NORTH;
			case 1 -> Orientation.EAST;
			case 2 -> Orientation.SOUTH;
			case 3 -> Orientation.WEST;
			default -> throw new AssertionError("This should never happen");
		};
	}
}
