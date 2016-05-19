package com.simbirsoft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simbirsoft.model.Board;
import com.simbirsoft.model.Player;
import com.simbirsoft.model.PlayerType;
import com.simbirsoft.model.Point;
import com.simbirsoft.response.GameResult;
import com.simbirsoft.response.GameState;
import com.simbirsoft.response.MoveResult;
import com.simbirsoft.response.RegistrationResult;
import com.simbirsoft.response.RegistrationState;
import com.simbirsoft.response.StopReason;
import com.simbirsoft.util.IdGenerator;

/**
 * Game management service.
 */
@Service
public class GameService {

    private static final int EMPTY_ID = 0;

    @Autowired
    private IdGenerator idGenerator;

    private GameState gameState;

    private Board board;

    private Player playerOne;

    private Player playerTwo;

    private Player currentPlayer;

    public GameService() {
        board = new Board();
        reset();
    }

    /**
     * Reset game status.
     */
    public void reset() {
        gameState = GameState.PLAYING;
        board.clear();
        playerOne = null;
        playerTwo = null;
        currentPlayer = null;
    }

    private GameState getGameWinningState(Player player) {
        return PlayerType.CROSS.equals(player.getPlayerType()) ? GameState.CROSS_WON : GameState.NOUGHT_WON;
    }

    private StopReason getStopReason(Player player, Point point) {
        if (!GameState.PLAYING.equals(gameState)) {
            return StopReason.GAME_OVER;
        }
        if (playerOne == null) {
            return StopReason.PLAYER_ONE_NOT_REGISTERED;
        }
        if (playerTwo == null) {
            return StopReason.PLAYER_TWO_NOT_REGISTERED;
        }
        if (player == null) {
            return StopReason.PLAYER_NOT_DEFINED;
        }
        if (player.equals(currentPlayer)) {
            return StopReason.NOT_THIS_PLAYER_TURN;
        }
        if (!board.canMove(point)) {
            return StopReason.INVALID_BOARD_MOVE;
        }
        return StopReason.OK;
    }

    /**
     * Do player move.
     * 
     * @param player what player move
     * @param point point on board
     * @return result of move
     */
    public MoveResult move(Player player, Point point) {
        StopReason stopReason = getStopReason(player, point);
        if (StopReason.OK.equals(stopReason)) {
            board.move(player, point);
            if (board.hasWon(player)) {
                gameState = getGameWinningState(player);
            } else if (!board.hasEmptyCell()) {
                gameState = GameState.DRAW;
            }
            currentPlayer = nextPlayer();
        }
        return new MoveResult(stopReason);
    }

    private Player nextPlayer() {
        return playerOne.equals(currentPlayer) ? playerTwo : playerOne;
    }

    /**
     * Try to register player and return RegistrationResult.
     * 
     * @param name new player's name
     * @return result of registration
     */
    public RegistrationResult registerPlayer(String name) {
        if (playerOne == null) {
            playerOne = new Player(idGenerator.newId(), name, PlayerType.CROSS);
            return new RegistrationResult(RegistrationState.SUCCESS, playerOne.getId());
        }
        if (playerTwo == null) {
            playerTwo = new Player(idGenerator.newId(), name, PlayerType.NOUGHT);
            currentPlayer = playerTwo;
            return new RegistrationResult(RegistrationState.SUCCESS, playerTwo.getId());
        }
        return new RegistrationResult(RegistrationState.ALREADY_REGISTERED, EMPTY_ID);
    }

    /**
     * Return result of the game.
     * 
     * @return result of the game
     */
    public GameResult getGameResult() {
        return new GameResult(gameState);
    }

    /**
     * Find a player by id and return it.
     * 
     * @param playerId a player id
     * @return player by id or null if no registered player with this id
     */
    public Player getPlayerById(int playerId) {
        if (playerOne != null && playerOne.getId().equals(playerId)) {
            return playerOne;
        }
        if (playerTwo != null && playerTwo.getId().equals(playerId)) {
            return playerTwo;
        }
        return null;
    }

    /**
     * Return the game board.
     * 
     * @return the game board
     */
    public Board getBoard() {
        return board;
    }
}
