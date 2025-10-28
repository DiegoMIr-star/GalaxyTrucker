package Controller.State;

import java.io.IOException;

/**
 * Class for representation of waiting for players state
 */
public class WaitingForPlayers extends GameState {

    /**
     * Methode used to dispatch the effect of the states
     * @param visitor visitor object
     */
    @Override
    public void accept(GameStateVisitor visitor) throws IOException {
        visitor.visit(this);
    }

    /**
     * Methode used to check if a message is received in the right moment
     * @param visitor visitor object
     * @return true if the message comes in the right state
     * @throws IOException input output exception thrown
     */
    @Override
    public boolean checkMessage(GameStateCheckerVisitor visitor) throws IOException {return visitor.visit(this);}

    /**
     * Method used to print the class
     * @return string to print
     */
    @Override
    public String toString(){return "WaitingForPlayers state";}

    /**
     * Method used to clone the object
     * @return cloned object
     */
    @Override
    public WaitingForPlayers clone() {return (WaitingForPlayers) super.clone();}
}
