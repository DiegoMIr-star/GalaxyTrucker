package model;

import model.exceptions.NullCallbackException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class used to manage a sequence of countdown timers, necessary for the ship construction phase
 */
public class GameTimer implements Serializable {

	/**
	 * Flag that indicates if the timer is currently running
	 */
	private boolean isTimerRunning = false;

	/**
	 * Attribute to store the duration of each hourglass
	 */
	private final ArrayList<Integer> timerRoundsSeconds;

	/**
	 * Attribute to store the Java Timer
	 */
	private transient Timer timer;

	/**
	 * Attribute used to save the current hourglass
	 */
	private int curTimerRound = 0;

	/**
	 * Attribute used to store the listener to be invoked when the timer expires
	 */
	private TimerListener listener;

	/**
	 * Constructor of the game timer:
	 * it creates an arraylist of three timer rounds of 10 seconds each
	 */
	GameTimer() {
		timerRoundsSeconds = new ArrayList<>();
		timerRoundsSeconds.add(90);
		timerRoundsSeconds.add(90);
		timerRoundsSeconds.add(90);
		timer = new Timer();
	}

	/**
	 * Getter of the attribute that indicates if the hourglass is running
	 * @return a boolean that is true if the timer is running
	 */
	public boolean isTimerRunning() {
		return isTimerRunning;
	}

	/**
	 * Setter of the attribute that indicates if the hourglass is running
	 * @param isTimerRunning is the boolean to set
	 */
	public void setIsTimerRunning(boolean isTimerRunning) {
		this.isTimerRunning = isTimerRunning;
	}

	/**
	 * Getter of the duration of the specified round
	 * @param round is the specific round considered
	 * @return an integer that represents the duration
	 */
	public int getTimerRoundSeconds(int round) {
		return timerRoundsSeconds.get(round);
	}

	/**
	 * Getter of the list of durations for each timer round
	 * @return an arraylist of Integer in which are stored the durations
	 */
	public ArrayList<Integer> getTimerRoundsSeconds() {
		return timerRoundsSeconds;
	}

	/**
	 * Getter of the current timer round
	 * @return an integer that represents the current timer round
	 */
	public int getCurTimerRound() {
		return curTimerRound;
	}

	/**
	 * Setter of the current timer round
	 * @param curTimerRound an integer that represents the current timer round
	 */
	public void setCurTimerRound(int curTimerRound) {
		this.curTimerRound = curTimerRound;
	}

	/**
	 * Getter of the current timer round duration
	 * @return an integer that represents the current timer round duration
	 */
	public int getCurRoundSeconds(){
		return timerRoundsSeconds.get(curTimerRound);
	}

	/**
	 * Method used to identify if the current round is the last one or not
	 * @return a boolean, true if the current is the last one
	 */
	public boolean curRoundIsLast(){
		return curTimerRound == timerRoundsSeconds.size() - 1;
	}

	/**
	 * Method used to start the following hourglass
	 * @param callback is the listener to be invoked when the timer expires
	 * @param isLast is the boolean that indicates if the current hourglass is the last
	 */
	public void startNextTimer(TimerListener callback, boolean isLast) {
		this.listener = callback;


		isTimerRunning = true;
		//start timer
		timer.schedule(new TimerTask() {
			@Override
			public void run(){

				//when timer is over, tell clients that it expired
				isTimerRunning = false;
				curTimerRound++;

				if(listener != null) {
					listener.onTimerExpired(isLast);
				}
				else
					throw new NullCallbackException("The callback was null.");
			}
			//seconds in milliseconds
		}, timerRoundsSeconds.get(curTimerRound) * 1000);
	}

	/**
	 * Method used to stop and cancel the timer
	 */
	public void stopEverything(){
		timer.cancel();
		timer.purge();
		isTimerRunning = false;
	}
}
