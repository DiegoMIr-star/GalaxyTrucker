package Connections.Messages;

import model.NextGameStateAndMessages;
import model.Projectiles.Projectile;

import java.io.IOException;

/**
 * Message sent to the client in order to notify projectiles with trajectory
 */
public class ProjectileTrajectoryMessage extends Message {

	/**
	 * Current projectile
	 */
	Projectile projectile;

	/**
	 * Current trajectory
	 */
	int trajectory;

	/**
	 * Constructor of the message: it initializes all the attributes
	 * @param projectile current projectile
	 * @param trajectory current trajectory
	 */
	public ProjectileTrajectoryMessage(Projectile projectile, int trajectory) {
		super("Server", MessageKind.PROJECTILE_TRAJECTORY);
		this.projectile = projectile;
		this.trajectory = trajectory;
	}

	/**
	 * Getter of projectile
	 * @return current projectile
	 */
	public Projectile getProjectile() {return projectile;}

	/**
	 * Getter of trajectory
	 * @return current trajectory
	 */
	public int getTrajectory() {return trajectory;}

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
