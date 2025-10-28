package model.Cards.CardVisitorProgresser;

/**
 * Functional interface used as callback to handle the case of elimination of a player for zero motor power
 */
@FunctionalInterface
public interface ZeroMotorPowerListener {

	/**
	 * Method to call in case of zero motor power for a player
	 * @param playerIndex is the index of the player with no motor power
	 */
    void OnZeroMotorPower(int playerIndex);
}
