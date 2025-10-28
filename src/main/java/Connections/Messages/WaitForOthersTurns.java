package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;

public class WaitForOthersTurns extends Message {
	/**
	 * Class constructor, it initializes all the attributes
	 */
	public WaitForOthersTurns() {
		super("server", MessageKind.WAITING_FOR_TURNS_AFTER_CONSTRUCTION);
	}

	//unused
	@Override
	public void accept(MessageVisitor visitor) throws IOException {
		visitor.visit(this);
	}

	//unused
	@Override
	public NextGameStateAndMessages accept(MessageVisitorProgresser visitor) {
		return null;
	}

	//unused
	@Override
	public boolean accept(MessageVisitorChecker visitor) {
		return false;
	}
}
