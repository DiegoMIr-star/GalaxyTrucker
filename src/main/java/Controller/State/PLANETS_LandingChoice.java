package Controller.State;

import java.io.IOException;

/**
 * Class for representation of planets landing choice
 */
public class PLANETS_LandingChoice extends GameState {

    /**
     * Methode used to dispatch the effect of the states
     * @param visitor visitor object
     * @throws IOException input output exception thrown
     */
    @Override
    public void accept(GameStateVisitor visitor) throws IOException {visitor.visit(this);}

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
    public String toString() {return "PLANETS_LandingChoice state";}

    /**
     * Method used to clone the object
     * @return cloned object
     */
    @Override
    public PLANETS_LandingChoice clone() {return (PLANETS_LandingChoice) super.clone();}
}
