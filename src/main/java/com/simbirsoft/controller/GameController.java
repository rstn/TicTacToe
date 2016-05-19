package com.simbirsoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.model.Board;
import com.simbirsoft.model.Player;
import com.simbirsoft.request.PlayerMove;
import com.simbirsoft.request.PlayerName;
import com.simbirsoft.response.GameResult;
import com.simbirsoft.response.MoveResult;
import com.simbirsoft.response.RegistrationResult;
import com.simbirsoft.service.GameService;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping(path = "/reset", method = RequestMethod.POST)
    public void reset() {
        gameService.reset();
    }

    @RequestMapping(path = "/player/register", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
    public RegistrationResult registerPlayer(@RequestBody PlayerName playerName) {
        return gameService.registerPlayer(playerName.getName());
    }

    @RequestMapping(path = "/move", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public MoveResult move(@RequestBody PlayerMove playerMove) {
        Player player = gameService.getPlayerById(playerMove.getPlayerId());
        return gameService.move(player, playerMove.getPoint());
    }

    @RequestMapping(path = "/gameBoard", method = RequestMethod.GET)
    public Board gameBoard() {
        return gameService.getBoard();
    }

    @RequestMapping(path = "/gameResult", method = RequestMethod.GET)
    public GameResult gameResult() {
        return gameService.getGameResult();
    }
}
