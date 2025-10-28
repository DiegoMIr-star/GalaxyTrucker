package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;

public class NotifyActionCompleted extends Message{
	Message replacingMessage;



	public Message getReplacingMessage() {
		return replacingMessage;
	}
	/**
	 * Class constructor, it initializes all the attributes
	 *
	 * @param nickname current nickname
	 */
	public NotifyActionCompleted(String nickname, Message replacingMessage) {
		super(nickname, MessageKind.NOTIFY_ACTION_COMPLETED);
		this.replacingMessage = replacingMessage;
	}

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
