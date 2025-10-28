package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class message used to implement the different kind of messages
 */
public abstract class Message implements Serializable {

    /**
     * Server or player nickname
     */
    private final String nickname;

    /**
     * Kind of message
     */
    private final MessageKind kind;

    /**
     * Class constructor, it initializes all the attributes
     * @param nickname current nickname
     * @param kind message kind
     */
    public Message(String nickname,MessageKind kind){
        this.nickname=nickname;
        this.kind=kind;
    }

    /**
     * Getter of the nickname
     * @return message nickname
     */
    public String getNickname(){return nickname;}

    /**
     * Getter of the message kind
     * @return message kind
     */
    public MessageKind getKind(){return kind;}

    /**
     * Method used to be overridden
     * @return true
     */
    public boolean isClaimed(){return true;}

    /**
     * Accept method used to dispatch messages with visitor
     * @param visitor visitor object
     * @throws IOException input output exception thrown
     */
    public abstract void accept(MessageVisitor visitor) throws IOException;

    /**
     * Accept method used to update state and messages with visitors
     * @param visitor visitor object
     * @return next game state and messages object
     */
    public abstract NextGameStateAndMessages accept(MessageVisitorProgresser visitor);

    /**
     * Accept method used to check whether said message should be saved as the "lastMessage" addressed to the client
     * in case of reconnection with resilience
     * @param visitor visitor object
     * @return next game state and messages object
     */
    public abstract boolean accept(MessageVisitorChecker visitor);
}
