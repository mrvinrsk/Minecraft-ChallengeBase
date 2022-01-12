package de.mrvinrsk.challengebase.util;


/**
 * This interface represents an event which only gets fired sometimes.
 */
public interface PercentageChallengeEvent extends ChallengeEvent {

    /**
     * Get the base possibility to trigger the event.
     * @return the base percentage.
     */
    double getBasePercentage();

    boolean isPercentageChangeable();

}
