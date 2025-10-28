package Controller.State;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class used to implement the game state, it's abstract in order to be extended in all
 * the different game states
 */
public abstract class GameState implements Serializable, Cloneable{

    /**
     * Methode used to dispatch the different states with the visitor
     * @param visitor visitor object
     * @throws IOException input output exception thrown
     */
	public abstract void accept(GameStateVisitor visitor) throws IOException;

    /**
     * Methode used to check if a message is in the right state with visitor pattern
     * @param visitor visitor object
     * @return true if the message is in the right game state
     * @throws IOException input output exception thrown
     */
	public abstract boolean checkMessage(GameStateCheckerVisitor visitor) throws IOException;

    /**
     * Methode used to clone the class
     * @return cloned object
     */
    @Override
    public GameState clone() {
        try {
            return (GameState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
