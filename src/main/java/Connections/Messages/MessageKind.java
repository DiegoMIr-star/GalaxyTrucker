package Connections.Messages;

/**
 * Enumeration of the different kind of messages
 */
public enum MessageKind {
    //Asking the first client the number of players
    PLAYERS_NUM_REQUEST,
    PLAYERS_NUM_RESPONSE,
    //Notifies the client that his request can not be accepted
    INVALID_NUM_PLAYERS_REQUEST,
    //Waiting for the number of players declared
    WAITING,
    WAITING_FOR_TURNS_AFTER_CONSTRUCTION,
    NOTIFY_ACTION_COMPLETED,
    PICK_REVEALED_COMP_REQUEST,
    UNCOVERED_COMPONENTS,
    //The hourglass begins
    START_TIMER_REQUEST,
    TIMER_EXPIRED,
    TIMER_STARTED,
    GET_SMALL_DECK,
    RETURN_SMALL_DECK,
    GET_AVAILABLE_SMALL_DECKS,
    ACCESS_TO_HOURGLASS_DENIED,
    //Asking the connection
    LOG_REQUEST,
    //Connection state
    LOG_RESPONSE,
    //
    DRAW_COMP_REQUEST,
    DRAW_COMP_RESPONSE,
    //Leave a component
    RETURN_COMP_REQUEST,
    //Set a position
    PLACE_COMPONENT,
    //You have selected a wrong position
    INVALID_POSITION,
    //message which contains the finished ship of a player, and which has to be added to the arrayList of ship dashboards in game
    PLACE_SHIP_REQUEST,
    DRAW_CARD_REQUEST,
    CARD_DRAWN_RESPONSE,
    //If you want to land or not on a planet
    PLANET_LAND_REQUEST,
    STOCKS_TO_ADD,
    FREE_PLANETS_RESPONSE,
    //Activate a shield
    SHIELD_ACTIVATION,
    //communicate earning of credits
    CREDITS_EARNED,
    UPDATED_SHIP,
    UPDATED_SHIP_FROM_CLIENT,
    DISCARD_STOCKS,
    //declare whether they claim the reward
    CLAIM_REWARD_CHOICE,
    //Provide dynamic firepower
    FIRE_POWER,
    //Provide dynamic motor power
    MOTOR_POWER,
    PROJECTILE_TRAJECTORY,
    //Activate a double cannon
    DOUBLE_CANNON_ACTIVATION,
    //Exchange days in stocks
    DAYS_STOCKS,
    //Leave members of the crew
    CREW_REQUEST,
    //Notify errors
    REMOVE_COMPONENT_REQUEST,
    PLAYERS_STATE,
    CARD_ACTIVATION_REQUEST,
    GENERIC,
    STATE,
    TURN,
    WINNERS_MESSAGE,
    END_GAME_REQUEST,
    POSITION,
    BEGIN,
    RES_REQ,
    RES_RESP,
    RESUME
}
