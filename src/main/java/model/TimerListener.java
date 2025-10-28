package model;

/**
 * Functional interface used to create a listener that handles timer expiration cases
 */
@FunctionalInterface
public interface TimerListener {

	/**
	 * Method to call in case of timer expired
	 * @param isLast indicates if the expired timer is the last one (true if is the last)
	 */
	void onTimerExpired(boolean isLast);
}
