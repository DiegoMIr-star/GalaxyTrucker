package model.DifferentShipComponents;

/**
 * Extension of ship component: it represents an unavailable slot
 * @see ShipComponent
 * @see model.ShipDashboard
 */
public class UnavailableSlot extends ShipComponent {

	/**
	 * Constructor which initializes the component
	 */
	public UnavailableSlot() {super(ComponentType.UNAVAILABLE_SLOT, Side.BlankSide, Side.BlankSide, Side.BlankSide, Side.BlankSide, -1,"");}

	/**
	 * It creates a copy of the specific component
	 * @return copy of the component
	 */
	@Override
	public ShipComponent clone() {return new UnavailableSlot();}

	/**
	 * It checks if the object passed in is an instance of the same class,
	 * if it is, it checks if all the attributes of the object are equal
	 * to this object
	 * @param o object to compare
	 * @return true if the object is equal
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof UnavailableSlot)) {
			return false;
		}
		boolean result;
		result = super.equals(o);
		return result;
	}
}
