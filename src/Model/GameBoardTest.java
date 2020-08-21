package Model;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    GameBoard gameBoard;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        gameBoard = new GameBoard();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void isGameStarted() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.startGame();
    }

    @org.junit.jupiter.api.Test
    void startGame() {
    }

    @org.junit.jupiter.api.Test
    void isGameOver() {
    }

    @org.junit.jupiter.api.Test
    void setGameOver() {
    }

    @org.junit.jupiter.api.Test
    void isOnBoard() {
    }

    @org.junit.jupiter.api.Test
    void getEnd() {
    }

    @org.junit.jupiter.api.Test
    void isDeployable() {
        Ship ship = new Ship(SHIP.Carrier);
        Coordinate coordinateHelper = new Coordinate(null, null);
        assertFalse(gameBoard.deploy(ship, new Coordinate(coordinateHelper.width - ship.getSize() + 1, 0), true));
        assertFalse(gameBoard.deploy(ship, new Coordinate(0, coordinateHelper.height - ship.getSize() + 1), false));

    }

    @org.junit.jupiter.api.Test
    void deploy() {

        Ship ship = new Ship(SHIP.Carrier);
        Coordinate coordinateHelper = new Coordinate(null, null);
        assertFalse(gameBoard.deploy(ship, new Coordinate(coordinateHelper.width - ship.getSize() + 1, 0), true));//Is outside board
        assertFalse(gameBoard.deploy(ship, new Coordinate(0, coordinateHelper.height - ship.getSize() + 1), false));//is outside board
        //Test success place ship:
        assertTrue(gameBoard.deploy(ship, new Coordinate(coordinateHelper.width - ship.getSize(), 0), true));
        Coordinate coordinatePlacedSuccess = new Coordinate(coordinateHelper.width - ship.getSize(), 0);
        boolean shouldTryHor = false;
        //Test deploy without undeploy:
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gameBoard.deploy(ship, new Coordinate(0, gameBoard.height - ship.getSize()), true);//already deployed
        });
        assertEquals(exception.getClass(), IllegalArgumentException.class);
        //I have no controller on gameboard to check two boats of same type is placed.
        //Should not allow two of same kind on board, but the controll for that is in player.
        //And one should use player to deploy. (even though i hacked it and used both board and player?)
        Ship ship2 = new Ship(SHIP.Carrier);//Will try to see if this allows placing two ships in parrallell
        //Following should probably not be allowed for above mentioned reasons.
        assertTrue(gameBoard.isDeployable(ship2, new Coordinate
                (coordinatePlacedSuccess.getX() - 1, coordinatePlacedSuccess.getY()), shouldTryHor));

        assertFalse(gameBoard.deploy(ship2, coordinatePlacedSuccess, shouldTryHor));//Only one square intersect, it should still throw exception
        /*
        //TODO remove:
        exception = assertThrows(IllegalArgumentException.class, () -> {
            gameBoard.deploy(ship2, coordinatePlacedSuccess, shouldTryHor);//Only one square intersect, it should still throw exception
        });
        assertEquals(exception.getClass(), IllegalArgumentException.class);*/

        assertTrue(gameBoard.deploy(ship2, new Coordinate
                (coordinatePlacedSuccess.getX() - 1, coordinatePlacedSuccess.getY()), shouldTryHor));
    }

    @org.junit.jupiter.api.Test
    void undeploy() {
    }

    @org.junit.jupiter.api.Test
    void isPreviouslyHit() {
    }

    @org.junit.jupiter.api.Test
    void shoot() {
    }
}