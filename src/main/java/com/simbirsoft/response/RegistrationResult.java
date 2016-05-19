package com.simbirsoft.response;

/**
 * Result of a player's registration.
 */
public class RegistrationResult {

    private final RegistrationState registrationState;

    private final int playerId;

    public RegistrationResult(RegistrationState registrationState, int playerId) {
        this.registrationState = registrationState;
        this.playerId = playerId;
    }

    public RegistrationState getRegistrationState() {
        return registrationState;
    }

    public int getPlayerId() {
        return playerId;
    }
}
