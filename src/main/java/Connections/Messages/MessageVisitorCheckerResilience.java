package Connections.Messages;

/**
 * Visitor class used to check if a message can be handled during resilience
 * For unexpected case, the method throws a runtime exception
 * Implements {@link MessageVisitorChecker}
 */
public class MessageVisitorCheckerResilience implements MessageVisitorChecker {

	/**
	 * Visit method for available small deck message
	 * @param message current message
	 * @return false, not accepted
	 */
	@Override
	public boolean visit(AvailableSmallDeckMessage message) {
		return false;
	}

	/**
	 * Visit method for start of the game message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(BeginMessage message) {
		return true;
	}

	/**
	 * Visit method for card activation request message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(CardActivationRequestMessage message) {
		return true;
	}

	/**
	 * Visit method for card drawn message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(CardDrawnMessage message) {
		return true;
	}

	/**
	 * Visit method for claim reward choice message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(ClaimRewardChoiceMessage message) {
		return true;
	}

	/**
	 * Visit method for credits earned message
	 * @param message current message
	 * @return false, not accepted
	 */
	@Override
	public boolean visit(CreditsEarnedMessage message) {
		return false;
	}

	/**
	 * Visit method for discard stocks message,
	 * it throws the exception
	 * @param message current message
     */
	@Override
	public boolean visit(DiscardStocksMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for draw card request message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(DrawCardRequestMessage message) {
		return true;
	}

	/**
	 * Visit method for draw component request message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(DrawComponentRequestMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for draw component response message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(DrawComponentResponseMessage message) {
		return true;
	}

	/**
	 * Visit method for draw specific component request message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(DrawSpecificComponentRequestMessage message) {
		return true;
	}

	/**
	 * Visit method for dynamic firepower message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(DynamicFirePowerMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for dynamic motor power message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(DynamicMotorPowerMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for end game message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(EndGameMessage message) {
		return true;
	}

	/**
	 * Visit method for free planets response message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(FreePlanetsResponseMessage message) {
		return true;
	}

	/**
	 * Visit method for available small decks request message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(AvailableSmallDecksRequestMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for game state message
	 * @param message current message
	 * @return false, not accepted
	 */
	@Override
	public boolean visit(GameStateMessage message) {
		return false;
	}

	/**
	 * Visit method for generic message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(GenericMessage message) {
		return true;
	}

	/**
	 * Visit method for log request message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(LogRequestMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for log response message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(LogResponseMessage message) {
		return true;
	}

	/**
	 * Visit method for pick revealed component request message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(PickRevealedComponentRequestMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for discard stocks message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(PlaceShipRequestMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for planet request message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(PlanetLandRequestMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for players number request message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(PlayersNumRequestMessage message) {
		return true;
	}

	/**
	 * Visit method for players number response message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(PlayersNumResponseMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for players positions message
	 * @param message current message
	 * @return false, not accepted
	 */
	@Override
	public boolean visit(PlayersPositionsMessage message) {
		return false;
	}

	/**
	 * Visit method for positions and turns message
	 * @param message current message
	 * @return false, not accepted
	 */
	@Override
	public boolean visit(PositionsAndTurnsMessage message) {
		return false;
	}

	/**
	 * Visit method for pre-made ship component request message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(PremadeShipComponentRequest message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for pre-made ship component response message
	 * @param message current message
	 * @return false, not accepted
	 */
	@Override
	public boolean visit(PremadeShipComponentResponse message) {
		return false;
	}

	/**
	 * Visit method for projectile trajectory message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(ProjectileTrajectoryMessage message) {
		return true;
	}

	/**
	 * Visit method for return component request message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(ReturnComponentRequestMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for return small deck message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(ReturnSmallDeckMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for revealed component message
	 * @param message current message
	 * @return false, not accepted
	 */
	@Override
	public boolean visit(RevealedComponentsMessage message) {
		return false;
	}

	/**
	 * Visit method for small deck request message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(SmallDeckRequestMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for small deck response message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(SmallDeckResponseMessage message) {
		return true;
	}

	/**
	 * Visit method for start time request message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(StartTimerRequestMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for stocks to add message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(StocksToAddMessage message) {
		return true;
	}

	/**
	 * Visit method for timer expired message
	 * @param message current message
	 * @return false, not accepted
	 */
	@Override
	public boolean visit(TimerExpiredMessage message) {
		return false;
	}

	/**
	 * Visit method for timer started message
	 * @param message current message
	 * @return false, not accepted
	 */
	@Override
	public boolean visit(TimerStartedMessage message) {
		return false;
	}

	/**
	 * Visit method for turn message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(TurnMessage message) {
		return true;
	}

	/**
	 * Visit method for update client ship message
	 * @param message current message
	 * @return false, not accepted
	 */
	@Override
	public boolean visit(UpdateClientShipMessage message) {
		return false;
	}

	/**
	 * Visit method for update server ship message,
	 * it throws the exception
	 * @param message current message
	 */
	@Override
	public boolean visit(UpdateServerShipMessage message) {
		throw new RuntimeException("Unexpected message to client. Message: " + message.getKind());
	}

	/**
	 * Visit method for waiting partners message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(WaitingPartnersMessage message) {
		return true;
	}

	/**
	 * Visit method for winners message
	 * @param message current message
	 * @return true, accepted
	 */
	@Override
	public boolean visit(WinnersMessage message) {
		return true;
	}
}
