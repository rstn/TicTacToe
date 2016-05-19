package com.simbirsoft.response;

/**
 * Result of game with state.
 */
public class GameResult {

    private final GameState gameState;

    public GameResult(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public String toString() {
        return gameState.toString();
    }
}
