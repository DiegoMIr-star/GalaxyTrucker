package model.Projectiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * The class is the representation of shots and meteors
 */
public class Projectile implements Serializable {

	/**
	 * Attribute which indicates the cardinal orientation of a shot or a meteor
	 */
	private final Orientation direction;

	/**
	 * Attribute which indicated the type of projectile, shot or meteor
	 */
	private final ProjectileType type;

	/**
	 * Constructor of a projectile: it sets orientation and type
	 * @param direction current orientation
	 * @param type kind of projectile
	 */
	@JsonCreator
	public Projectile(
			@JsonProperty("orientation") Orientation direction,
			@JsonProperty("kind") ProjectileType type){
		this.direction=direction;
		this.type=type;
	}

	/**
	 * Getter of the direction
	 * @return current direction
	 */
	public Orientation getDirection() {return direction;}

	/**
	 * Getter of the kind of projectile
	 * @return current kind
	 */
	public ProjectileType getType() {return type;}

	/**
	 * Method in order to print the projectile into the TUI
	 * @see View.TUI
	 * @return String to print the class
	 */
	@Override
	public String toString() {
		String proj = "";
		switch (type){
			case LIGHT_SHOT -> proj += "Light Shot";
			case HEAVY_SHOT -> proj += "Heavy Shot";
			case SMALL_ASTEROID -> proj += "Small meteor";
			case BIG_ASTEROID -> proj += "Big meteor";
		}
		proj += " heading ";
		switch (direction){
			case NORTH -> proj += "North";
			case EAST -> proj += "East";
			case SOUTH -> proj += "South";
			case WEST -> proj += "West";
		}

		return proj;
	}
}
