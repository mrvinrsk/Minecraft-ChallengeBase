package de.mrvinrsk.challengebase.util;


/**
 * This interface represents an event which contains point transactions.
 */
public interface PointEarningEvent extends ChallengeEvent {

    /**
     * Get the points for this event.
     *
     * @return the amount of points.
     */
    int getPoints();

    /**
     * Get the action to do with the amount of points.
     *
     * @return the action.
     */
    PointEventType getPointEventType();

}
