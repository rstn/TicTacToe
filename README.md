# REST methods

## POST

1. reset - reset the game state

2. player/register - register player with next request:

{ "name": "Some Name" }

Return value: {registrationState, playerId}

*registrationState* has next values:
* SUCCESS - registration successfully completed
* ALREADY_REGISTERED - players already registered

3. move - player (found by id) mark a cell on the game board at coordinates
with next request:

{ "playerId": 1, "point": { "row": 0, "col": 0 } }

Return value: {stopReason}

*stopReason* has next values:
* OK - move was successful
* GAME_OVER - the game is completed
* PLAYER_ONE_NOT_REGISTERED - playerOne is not registered
* PLAYER_TWO_NOT_REGISTERED - playerTwo is not registered
* PLAYER_NOT_DEFINED - a player is not found by number
* NOT_THIS_PLAYER_TURN - now turn of another player
* INVALID_BOARD_MOVE - incorrect coordinates of board cell

## GET

1. gameBoard - get the board with cells state

2. gameResult - get the game state

Return value: {gameState}

*gameState* has next values:
* PLAYING - the game is started
* DRAW - the game ended in a draw
* CROSS_WON - playerOne won the game
* NOUGHT_WON - playerTwo won the game

# Game flow

1. Start the server through run.bat file (runtests.bat - run tests).

2. Register 2 players.

3. Do moves in turn until the game is not finished.

4. If you want to start the game one more time then reset it and start with (2)
point.

# Requirements

JDK 8

# Improvements which can be implement in future

* Add Swager for documentation of REST API

* Add ability to run multiple games between two players

* Possibility restart the game is the best use case (not full game reset)
