package Connections.Messages;

import model.NextGameStateAndMessages;
import model.Stocks;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message sent to the client to add some stocks
 */
public class StocksToAddMessage extends Message implements Serializable {

    /**
     * Current stocks to add
     */
    private final Stocks stocks;

    /**
     * Constructor used to initialize the stocks
     * @param stocks current stocks
     */
    public StocksToAddMessage(Stocks stocks) {
        super("Server", MessageKind.STOCKS_TO_ADD);
        this.stocks = stocks;
    }

    /**
     * Getter of the stocks
     * @return current stocks to add
     */
    public Stocks getStocks() {return stocks;}

    /**
     * Method used to dispatch the messages with visitor
     * @param visitor visitor logic with the logic of the different messages
     */
    @Override
    public void accept(MessageVisitor visitor) throws IOException {visitor.visit(this);}

    /**
     * Method used to handle different messages in the controller
     * @see Controller.Controller
     * @param visitor object visitor
     * @return object which contains the next state with messages
     */
    @Override
    public NextGameStateAndMessages accept(MessageVisitorProgresser visitor) {return visitor.visit(this);}

    /**
     * Accept method used to check whether said message should be saved as the "lastMessage" addressed to the client
     * in case of reconnection with resilience
     * @param visitor visitor object
     * @return next game state and messages object
     */
    @Override
    public boolean accept(MessageVisitorChecker visitor) {
        return visitor.visit(this);
    }
}
