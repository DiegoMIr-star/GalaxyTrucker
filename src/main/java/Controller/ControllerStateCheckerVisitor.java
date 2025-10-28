package Controller;

import Connections.Messages.Message;
import Connections.Messages.MessageKind;
import Controller.State.*;

import java.io.IOException;

/**
 * Visitor class used to see if a message is reached in the right game state, it's usefully
 * only for socket, because RMI calls directly the controller methods
 */
public class ControllerStateCheckerVisitor implements GameStateCheckerVisitor{

	/**
	 * Current controller
	 */
	Controller controller;

	/**
	 * Current message
	 */
	Message message;

	/**
	 * Visitor constructor used to initialize the attribute
	 * @param controller current controller
	 * @param message current message
	 */
	public ControllerStateCheckerVisitor(Controller controller, Message message) {
		this.controller = controller;
		this.message = message;
	}

	/**
	 * Methode used to check if the message is a players number request in initialization state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(InitializationState state) throws IOException {return message.getKind() == MessageKind.PLAYERS_NUM_RESPONSE;}

	/**
	 * Methode used to check if the message is an updated
	 *  ship from client or a discard stocks in add and rearrange stocks state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(AddAndRearrangeStocks state) throws IOException {return message.getKind() == MessageKind.UPDATED_SHIP_FROM_CLIENT || message.getKind() == MessageKind.DISCARD_STOCKS;}

	/**
	 * Methode used to check if the message is a draw card request or a place
	 * ship request in card drawing state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(CardDrawing state) throws IOException {return message.getKind() == MessageKind.DRAW_CARD_REQUEST || message.getKind() == MessageKind.PLACE_SHIP_REQUEST;}

	/**
	 * Methode used to check if the message is a claim reward choice in claim reward choice state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(ClaimRewardChoice state) throws IOException {return message.getKind() == MessageKind.CLAIM_REWARD_CHOICE;}

	/**
	 * Methode used to check if the message is a card activation request in docking choice state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(DockingChoice state) throws IOException {return message.getKind() == MessageKind.CARD_ACTIVATION_REQUEST;}

	/**
	 * Methode used to check if the message is an end game request in end game state
	 * @param state current state
	 * @return true if the message is in the right state
	 */
	@Override
	public boolean visit(EndGame state) {return message.getKind() == MessageKind.END_GAME_REQUEST;}

	/**
	 * Methode used to check if the message is a firepower request or an updated
	 * ship from client in firepower choice state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(FirePowerChoice state) throws IOException {return message.getKind() == MessageKind.FIRE_POWER || message.getKind() == MessageKind.UPDATED_SHIP_FROM_CLIENT;}

	/**
	 * Methode used to check if the message is an update ship
	 * from client in give up crew choice state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(GiveUpCrewChoice state) throws IOException {return message.getKind() == MessageKind.UPDATED_SHIP_FROM_CLIENT;}

	/**
	 * Methode used to check if the message is an updated ship
	 * from client in manage projectile state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(ManageProjectile state) throws IOException {return message.getKind() == MessageKind.UPDATED_SHIP_FROM_CLIENT;}

	/**
	 * Methode used to check if the message is a motor power or an updated
	 * ship from client in motor power choice state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(MotorPowerChoice state) throws IOException {return message.getKind() == MessageKind.MOTOR_POWER || message.getKind() == MessageKind.UPDATED_SHIP_FROM_CLIENT;}

	/**
	 * Methode used to check if the message is a planet land request in planet landing choice state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(PLANETS_LandingChoice state) throws IOException {return message.getKind() == MessageKind.PLANET_LAND_REQUEST;}

	/**
	 * Methode used to check if the message is a start timer request, get available small
	 * decks, get small deck, return small deck, draw component request, pick revealed component
	 * request, return component request, place ship request or players number response
	 * in ship construction state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(ShipConstructionState state) throws IOException {
		return switch (message.getKind()) {
			case START_TIMER_REQUEST, GET_AVAILABLE_SMALL_DECKS, GET_SMALL_DECK, RETURN_SMALL_DECK, DRAW_COMP_REQUEST,
				 PICK_REVEALED_COMP_REQUEST, RETURN_COMP_REQUEST, PLACE_SHIP_REQUEST, PLAYERS_NUM_RESPONSE -> true;
			default -> false;
		};
	}

	/**
	 * Methode used to check if the message is a place ship request in
	 * to be fixed and fixing state
	 * @param state current state
	 * @return true if the message is in the right state
	 * @throws IOException input output exception thrown
	 */
	@Override
	public boolean visit(ToBeFixedAndFixingShips state) throws IOException {return message.getKind() == MessageKind.PLACE_SHIP_REQUEST;}

	/**
	 * Methode used to check if the message is a player number response
	 * in waiting for players state
	 * @param state current state
	 * @return true if the message is in the right state
	 */
	@Override
	public boolean visit(WaitingForPlayers state) {
		return (message.getKind() == MessageKind.PLAYERS_NUM_RESPONSE || message.getKind() == MessageKind.END_GAME_REQUEST);
	}
}
