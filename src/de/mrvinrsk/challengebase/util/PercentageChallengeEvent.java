package de.mrvinrsk.challengebase.util;


/**
 * This interface represents an event which only gets fired sometimes.
 */
public interface PercentageChallengeEvent extends ChallengeEvent {

    /**
     * Get the base possibility to trigger the event.
     *
     * @return the base percentage.
     */
    double getBasePercentage();

    /**
     * Get if the percentage should be changeable per player.
     *
     * @return if it's changeable.
     */
    boolean isPercentageChangeable();

}
