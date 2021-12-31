package de.mrvinrsk.challengebase.util;

public interface PointEarningEvent extends ChallengeEvent {

    int getPoints();
    PointEventType getPointEventType();

}
