package Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AIPlayerTest {
    AIPlayer aiPlayer;

    @BeforeEach
    void setUp() {
        aiPlayer = new AIPlayer("Ai");
    }

    @Test
    void getRandomShot() {
        /*
        Should have used an hashset, but not worth the time invested
        instead simple TEST implementation.
         */
        Coordinate coordinateHelper = new Coordinate(null, null);
        aiPlayer.startGame();
        for (int i = 0; i < coordinateHelper.height * coordinateHelper.width; i++) {
            Coordinate shot = aiPlayer.getRandomShot();
            Assertions.assertNotNull(shot);
            Assertions.assertNull(aiPlayer.gameBoard.boardSquares[shot.getX()][shot.getY()]);
            aiPlayer.gameBoard.boardSquares[shot.getX()][shot.getY()] = new BoardSquare();
        }
    }

    @Test
    void placeAllShips() {
        int numTests = 10000;//No exception for 10000 rounds.
        //Nor does it seem to print anything using System.err.
        for (int i = 0; i < numTests; i++) {
            aiPlayer = new AIPlayer("Ai");
            System.out.println("Test: " + i);
            aiPlayer.placeAllShips();
            Assertions.assertTrue(aiPlayer.canGameStart());
        }
    }
}