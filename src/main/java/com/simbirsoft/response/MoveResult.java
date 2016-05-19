package com.simbirsoft.response;

/**
 * Result of a player's move.
 */
public class MoveResult {

    private StopReason stopReason;

    public MoveResult(StopReason stopReason) {
        this.stopReason = stopReason;
    }

    public StopReason getStopReason() {
        return stopReason;
    }

    @Override
    public String toString() {
        return stopReason.toString();
    }
}
