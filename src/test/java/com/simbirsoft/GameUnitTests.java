package com.simbirsoft;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.simbirsoft.model.Board;
import com.simbirsoft.model.Player;
import com.simbirsoft.model.Point;
import com.simbirsoft.response.GameState;
import com.simbirsoft.response.RegistrationResult;
import com.simbirsoft.response.RegistrationState;
import com.simbirsoft.response.StopReason;
import com.simbirsoft.service.GameService;
import com.simbirsoft.util.IdGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Unit tests of game.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class GameUnitTests {

    @Configuration
    static class ContextConfiguration {

        @Bean
        public GameService gameService() {
            return new GameService();
        }

        @Bean
        public Board board() {
            return new Board();
        }

        @Bean
        public IdGenerator idGenerator() {
            return new IdGenerator();
        }
    }

    @Autowired
    private GameService gameService;

    @Before
    public void setUp() {
        gameService.reset();
    }

    @Test
    public void testRegistration() {
        assertEquals(StopReason.PLAYER_ONE_NOT_REGISTERED, gameService.move(null, null).getStopReason());

        RegistrationResult registrationResult;
        registrationResult = gameService.registerPlayer("Molly");
        Player playerMolly = gameService.getPlayerById(registrationResult.getPlayerId());
        assertNotNull(playerMolly);
        assertEquals(RegistrationState.SUCCESS, registrationResult.getRegistrationState());

        assertEquals(StopReason.PLAYER_TWO_NOT_REGISTERED, gameService.move(null, null).getStopReason());

        registrationResult = gameService.registerPlayer("Case");
        Player playerCase = gameService.getPlayerById(registrationResult.getPlayerId());
        assertNotNull(playerCase);
        assertEquals(RegistrationState.SUCCESS, registrationResult.getRegistrationState());
        registrationResult = gameService.registerPlayer("Billy");
        assertEquals(RegistrationState.ALREADY_REGISTERED, registrationResult.getRegistrationState());
    }

    @Test
    public void testMoves() {
        Player playerMolly = gameService.getPlayerById(gameService.registerPlayer("Molly").getPlayerId());
        Player playerCase = gameService.getPlayerById(gameService.registerPlayer("Case").getPlayerId());

        assertEquals(GameState.PLAYING, gameService.getGameResult().getGameState());

        assertEquals(StopReason.PLAYER_NOT_DEFINED, gameService.move(null, null).getStopReason());
        assertEquals(StopReason.NOT_THIS_PLAYER_TURN, gameService.move(playerCase, null).getStopReason());
        assertEquals(StopReason.INVALID_BOARD_MOVE, gameService.move(playerMolly, new Point(-1, -1)).getStopReason());
    }

    @Test
    public void testWinOfCross() {
        Board board = null;
        try {
            board = gameService.getBoard();
            fail("No expected exception was thrown");
        } catch (Throwable t) {
            // expected
        }

        Player playerMollyM = gameService.getPlayerById(gameService.registerPlayer("Molly M").getPlayerId());
        Player playerCaseHD = gameService.getPlayerById(gameService.registerPlayer("Case HD").getPlayerId());

        assertEquals(GameState.PLAYING, gameService.getGameResult().getGameState());

        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(0, 0)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerCaseHD, new Point(0, 1)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(1, 1)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerCaseHD, new Point(2, 2)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(1, 0)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerCaseHD, new Point(2, 0)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(1, 2)).getStopReason());

        assertEquals(GameState.CROSS_WON, gameService.getGameResult().getGameState());
        assertEquals(true, board.hasWon(playerMollyM));
    }

    @Test
    public void testWinOfNought() {
        Player playerMollyM = gameService.getPlayerById(gameService.registerPlayer("Molly M").getPlayerId());
        Player playerCaseHD = gameService.getPlayerById(gameService.registerPlayer("Case HD").getPlayerId());

        assertEquals(GameState.PLAYING, gameService.getGameResult().getGameState());

        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(0, 1)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerCaseHD, new Point(0, 0)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(2, 1)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerCaseHD, new Point(1, 1)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(2, 2)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerCaseHD, new Point(2, 0)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(1, 0)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerCaseHD, new Point(0, 2)).getStopReason());

        assertEquals(GameState.NOUGHT_WON, gameService.getGameResult().getGameState());
        assertEquals(true, gameService.getBoard().hasWon(playerCaseHD));
    }

    @Test
    public void testDraw() {
        Player playerMollyM = gameService.getPlayerById(gameService.registerPlayer("Molly M").getPlayerId());
        Player playerCaseHD = gameService.getPlayerById(gameService.registerPlayer("Case HD").getPlayerId());

        assertEquals(GameState.PLAYING, gameService.getGameResult().getGameState());

        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(0, 0)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerCaseHD, new Point(1, 1)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(1, 0)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerCaseHD, new Point(2, 0)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(0, 2)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerCaseHD, new Point(0, 1)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(2, 1)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerCaseHD, new Point(1, 2)).getStopReason());
        assertEquals(StopReason.OK, gameService.move(playerMollyM, new Point(2, 2)).getStopReason());

        assertEquals(GameState.DRAW, gameService.getGameResult().getGameState());
        assertEquals(false, gameService.getBoard().hasEmptyCell());
    }
}
