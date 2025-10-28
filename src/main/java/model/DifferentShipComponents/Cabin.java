package model.DifferentShipComponents;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.exceptions.NotAllowedAlienException;
import model.exceptions.NotAllowedHumanException;

/**
 * Extension of ship component: it represents the cabin
 * @see ShipComponent
 */
public class Cabin extends ShipComponent{

	/**
	 * True if there's the first human on the cabin
	 */
	private boolean humanEquip1;

	/**
	 * True if there's the second human on the cabin
	 */
	private boolean humanEquip2;

	/**
	 * True if there's the brown alien life support
	 */
	private boolean brownLifeSupport;

	/**
	 * True if there's the purple alien life support
	 */
	private boolean purpleLifeSupport;

	/**
	 * True if there's a brown alien on the cabin
	 */
	private boolean brownAlienEquip;

	/**
	 * True if there's a purple alien on the cabin
	 */
	private boolean purpleAlienEquip;

	/**
	 * True if the cabin is central
	 */
	private final boolean isCentral;

	/**
	 * Constructor of the specific component: it initializes the attributes
	 * @param northSide current north side
	 * @param eastSide current east side
	 * @param southSide current south side
	 * @param westSide current west side
	 * @param ID specific ID
	 * @param image path to the image
	 */
	@JsonCreator
	public Cabin(
			@JsonProperty("central") boolean isCentral,
			@JsonProperty("north_side") Side northSide,
			@JsonProperty("east_side") Side eastSide,
			@JsonProperty("south_side") Side southSide,
			@JsonProperty("west_side") Side westSide,
			@JsonProperty("ID") int ID,
			@JsonProperty("Image") String image
	){
		super(ComponentType.CABIN,northSide,eastSide,southSide,westSide, ID, image);
		this.isCentral = isCentral;
		humanEquip1= false;
		humanEquip2=false;
		brownAlienEquip=false;
		purpleAlienEquip=false;
		brownLifeSupport =false;
		purpleLifeSupport =false;
	}

	/**
	 * Constructor of the specific component: it initializes the attributes without ID
	 * @param northSide current north side
	 * @param eastSide current east side
	 * @param southSide current south side
	 * @param westSide current west side
	 * @param image path to the image
	 */
	public Cabin(
			@JsonProperty("central") boolean isCentral,
			@JsonProperty("north_side") Side northSide,
			@JsonProperty("east_side") Side eastSide,
			@JsonProperty("south_side") Side southSide,
			@JsonProperty("west_side") Side westSide,
			@JsonProperty("Image") String image
	){
		super(ComponentType.CABIN,northSide,eastSide,southSide,westSide, -1, image);
		this.isCentral = isCentral;
		humanEquip1= false;
		humanEquip2=false;
		brownAlienEquip=false;
		purpleAlienEquip=false;
		brownLifeSupport =false;
		purpleLifeSupport =false;
	}

	/**
	 * Getter of all the humans on the cabin
	 * @return list of presences of humans on the cabin
	 */
	public ArrayList<Boolean> getHumanEquip(){
		ArrayList<Boolean> humans=new ArrayList<>(2);
		humans.add(humanEquip1);
		humans.add(humanEquip2);
		return humans;
	}

	/**
	 * It returns the amount of humans on a cabin
	 * @return amount of humans
	 */
	public int getHumans(){return ((humanEquip1 ? 1 : 0) + (humanEquip2 ? 1 : 0));}

	/**
	 * It says if there's a brown alien on the cabin
	 * @return true if there's a brown alien
	 */
	public boolean getBrownAlienEquip(){return brownAlienEquip;}

	/**
	 * It says if there's a purple alien on the cabin
	 * @return true if there's a purple alien
	 */
	public boolean getPurpleAlienEquip(){return purpleAlienEquip;}

	/**
	 * It says if there's a brown life support
	 * @return true if there's a brown life support
	 */
	public boolean hasBrownLifeSupport(){return brownLifeSupport;}

	/**
	 * It says if there's a purple life support
	 * @return true if there's a purple life support
	 */
	public boolean hasPurpleLifeSupport(){return purpleLifeSupport;}

	/**
	 * It says if the cabin is central
	 * @return true if the cabin is central
	 */
	public boolean getIsCentral(){ return isCentral; }

	/**
	 * Setter for the different humans
	 * @param human1 true if there's the first human
	 * @param human2 true if there's the second human
	 * @throws NotAllowedHumanException thrown if you try to set humans on a cabin
	 *                                  with an alien
	 */
	public void setHumans(boolean human1, boolean human2) throws NotAllowedHumanException {
		//Check if there are already aliens
		if((brownAlienEquip || purpleAlienEquip) && (human1 || human2)) {
			throw new NotAllowedHumanException("Trying to set humans where there's an alien.");
		}
		humanEquip1=human1;
		humanEquip2=human2;
	}

	/**
	 * Setter for the presence of brown aliens on the cabin
	 * @param brownAlien true if there is a brown alien
	 * @throws NotAllowedAlienException thrown if you try to put an alien
	 *                                  where there is already someone
	 */
	public void setBrownAlien(boolean brownAlien) throws NotAllowedAlienException {
		//Check if there are already aliens or humans
		if((humanEquip1||humanEquip2||purpleAlienEquip) && brownAlien){
			throw new NotAllowedAlienException("Trying to set alien where is not allowed.");
		}
		//Check if there is the specific oddBit for the alien
		brownAlienEquip=brownAlien;
		if(!brownLifeSupport && brownAlienEquip) {
			brownAlienEquip = false;
			throw new NotAllowedAlienException("Trying to set alien without life support.");

		}
	}

	/**
	 * Setter for the presence of brown aliens on the cabin
	 * @param purpleAlien true if there is a brown alien
	 * @throws NotAllowedAlienException thrown if you try to put an alien
	 *                                  where there is already someone
	 */
	public void setPurpleAlien(boolean purpleAlien) throws NotAllowedAlienException{
		//Check if there are already aliens or humans
		if((humanEquip1||humanEquip2||brownAlienEquip) && purpleAlien){
			//System.out.println("problema");
			throw new NotAllowedAlienException("Trying to set alien where is not allowed.");
		}
		//Check if there is the specific oddBit for the alien
		purpleAlienEquip=purpleAlien;
		if(!purpleLifeSupport && purpleAlienEquip) {
			purpleAlienEquip = false;
			throw new NotAllowedAlienException("Trying to set alien without life support.");
		}
	}

	/**
	 * Setter for the brown alien life support
	 * @param hasBrownLifeSupport true if there's a brown life support
	 */
	public void setBrownLifeSupport(boolean hasBrownLifeSupport){brownLifeSupport =hasBrownLifeSupport;}

	/**
	 * Setter for the purple alien life support
	 * @param hasPurpleLifeSupport true if there's a purple life support
	 */
	public void setPurpleLifeSupport(boolean hasPurpleLifeSupport){purpleLifeSupport =hasPurpleLifeSupport;}

	/**
	 * It removes all humans from the cabin
	 */
	public void removeHuman(){
		//Check if there are two humans or only one
		if(humanEquip1 && humanEquip2) humanEquip2=false;
		else if(humanEquip1|| humanEquip2){
			humanEquip1=false;
			humanEquip2=false;
		}
	}

	/**
	 * It removes the alien from the cabin independently of the colour
	 */
	public void removeAlien(){
		if(brownAlienEquip) brownAlienEquip=false;
		if(purpleAlienEquip) purpleAlienEquip=false;
	}

	/**
	 * It removes the entire crew from the cabin
	 */
	public void removeCrew(){
		brownAlienEquip = false;
		purpleAlienEquip = false;
		humanEquip1 = false;
		humanEquip2 = false;
	}
	/**
	 * It says if the cabin is occupied
	 * @return true if there's someone on the cabin
	 */
	public boolean isOccupied(){return (humanEquip1||humanEquip2||purpleAlienEquip||brownAlienEquip);}

	/**
	 * It creates a copy of the specific component
	 * @return copy of the component
	 */
	@Override
	public ShipComponent clone() {
		Cabin newComp = new Cabin(isCentral, this.getNorthSide(), this.getEastSide(), this.getSouthSide(),
				this.getWestSide(), this.getID(), this.image);
		newComp.setOrientation(orientation);
		newComp.setHumans(humanEquip1, humanEquip2);
		newComp.setBrownLifeSupport(brownLifeSupport);
		newComp.setPurpleLifeSupport(purpleLifeSupport);
		newComp.setBrownAlien(brownAlienEquip);
		newComp.setPurpleAlien(purpleAlienEquip);


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
		if(!(o instanceof Cabin comp)){ return false; }
		boolean result = true;
		result &= super.equals(o);
		result &= this.humanEquip1 == comp.humanEquip1;
		result &= this.humanEquip2 == comp.humanEquip2;
		result &= this.brownLifeSupport == comp.brownLifeSupport;
		result &= this.purpleLifeSupport == comp.purpleLifeSupport;
		result &= this.brownAlienEquip == comp.brownAlienEquip;
		result &= this.purpleAlienEquip == comp.purpleAlienEquip;
		result &= this.isCentral == comp.isCentral;
		return result;
	}
}