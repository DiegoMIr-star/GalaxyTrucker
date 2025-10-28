package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.exceptions.UnderflowBankException;

import java.io.Serializable;

/**
 * This class is the bank,
 * used in order to distribute credits and stocks in a game
 * @see Controller.Controller
 * @see Game
 */
public class Bank implements Serializable {

    /**
     * Number of credits contained in a game,
     * according to the rules
     */
    private int credits;

    /**
     * Number of batteries contained in a game,
     * according to the rules
     */
    private int batteries;

    /**
     * Number of red stocks contained in a game,
     * according to the rules
     */
    private int redStocks;

    /**
     * Number of yellow stocks contained in a game,
     * according to the rules
     */
    private int yellowStocks;

    /**
     * Number of green stocks contained in a game,
     * according to the rules
     */
    private int greenStocks;

    /**
     * Number of blue stocks contained in a game,
     * according to the rules
     */
    private int blueStocks;

    /**
     * Public constructor, used to initialize all the attributes
     * @param credits number of credits
     * @param batteries number of batteries
     * @param redStocks number of red stocks
     * @param yellowStocks number of yellow stocks
     * @param greenStocks number of green stocks
     * @param blueStocks number of blue stocks
     */
    public Bank(@JsonProperty("credits") int credits,@JsonProperty("batteries") int batteries,@JsonProperty("redStocks") int redStocks,
                    @JsonProperty("yellowStocks") int yellowStocks, @JsonProperty("greenStocks") int greenStocks,
                    @JsonProperty("blueStocks") int blueStocks){
        this.credits=credits;
        this.batteries=batteries;
        this.redStocks=redStocks;
        this.yellowStocks=yellowStocks;
        this.greenStocks=greenStocks;
        this.blueStocks=blueStocks;
    }

    /**
     * Method used to get the number of credits:
     * used only in the tests, not necessary for the game
     * @return number of credits
     */
    public int getCredits(){ return credits;}

    /**
     * Method used to get the number of batteries:
     * used only in the tests, not necessary for the game
     * @return number of batteries
     */
    public int getBatteries(){ return batteries;}

    /**
     * Method used to get the number of red stocks:
     * used only in the tests, not necessary for the game
     * @return number of red stocks
     */
    public int getRedStocks(){ return redStocks;}

    /**
     * Method used to get the number of yellow stocks:
     * used only in the tests, not necessary for the game
     * @return number of yellow stocks
     */
    public int getYellowStocks(){ return yellowStocks;}

    /**
     * Method used to get the number of green stocks:
     * used only in the tests, not necessary for the game
     * @return number of green stocks
     */
    public int getGreenStocks(){ return greenStocks;}

    /**
     * Method used to get the number of blue stocks:
     * used only in the tests, not necessary for the game
     * @return number of blue stocks
     */
    public int getBlueStocks(){ return blueStocks;}

    /**
     * Method used in order to collect a certain amount of credits
     * needed during the game
     * @param value the amount of credits to subtract from the number contained
     *              in the bank
     * @throws UnderflowBankException thrown when it's asked to take more credits
     *                                than the amount in the bank
     */
    public void getCredits(int value) throws UnderflowBankException {
        if(value>credits) throw new UnderflowBankException("Trying to ask more than the credit amount in the entire bank.");
        credits=credits-value;
    }

    /**
     * Method used in order to collect a certain amount of batteries
     * needed during the game
     * @param number the amount of batteries to subtract from the number contained
     *               in the bank
     * @throws UnderflowBankException thrown when it's asked to take more batteries
     *                                than the amount in the bank
     */
    public void getBatteries(int number) throws UnderflowBankException{
        if(number>batteries) throw new UnderflowBankException("Trying to ask more than the battery amount in the entire bank.");
        batteries=batteries-number;
    }

    /**
     * Method used in order to ask a specific number of stocks for the game
     * @param redStocks amount of red stocks
     * @param yellowStocks amount of yellow stocks
     * @param greenStocks amount of green stocks
     * @param blueStocks amount of blue stocks
     * @return object stocks containing the specific number of stocks
     * @throws UnderflowBankException thrown when asked more stocks than the amount
     *                                in the bank
     */
    public Stocks getStocks(int redStocks, int yellowStocks, int greenStocks, int blueStocks) throws UnderflowBankException{
        if((redStocks>this.redStocks)||(blueStocks>this.blueStocks)||(yellowStocks>this.yellowStocks)||(greenStocks>this.greenStocks)) throw new UnderflowBankException("Trying to ask more than the stock amount in the entire bank.");
        this.redStocks=this.redStocks-redStocks;
        this.blueStocks=this.blueStocks-blueStocks;
        this.yellowStocks=this.yellowStocks-yellowStocks;
        this.greenStocks=this.greenStocks-greenStocks;
        return new Stocks(redStocks,yellowStocks,greenStocks,blueStocks);
    }

    /**
     * Method used in order to add some credits into the bank
     * @param credits amount of credits to add
     */
    public void addCredits(int credits){
        this.credits=this.credits+credits;
    }

    /**
     * Method used in order to add some batteries into the bank
     * @param batteries amount of batteries to add
     */
    public void addBatteries(int batteries){
        this.batteries=this.batteries+batteries;
    }

    /**
     * Method used in order to add some batteries into the bank
     * @param stocks amount of stocks to add
     */
    public void addStocks(Stocks stocks){
        redStocks = redStocks + stocks.getSpecialRedStocks();
        blueStocks = blueStocks + stocks.getBlueStocks();
        greenStocks = greenStocks + stocks.getGreenStocks();
        yellowStocks = yellowStocks + stocks.getYellowStocks();
    }
}
