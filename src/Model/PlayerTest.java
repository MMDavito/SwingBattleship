package Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;

    @BeforeEach
    void setUp() {
        player = new Player(null);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getNumShots() {
    }

    @Test
    void getName() {
        player = new Player(null);
        Assertions.assertNull(player.getName());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append(i);
        }
        player = new Player(sb.toString());
        Assertions.assertNotNull(player.getName());
    }

    @Test
    void setName() {
        //basicly Tested in getname.
    }

    @Test
    void isGameStarted() {
        Assertions.assertThrows(IllegalStateException.class, player::startGame);
        List<Ship> ships = player.ships;
        for (int ycoord = 0; ycoord < ships.size(); ycoord++) {
            if (ycoord > 0) {
                Assertions.assertFalse(player.gameBoard.deploy(ships.get(ycoord), new Coordinate(0, ycoord-1), true));
            }
            Assertions.assertTrue(player.gameBoard.deploy(ships.get(ycoord), new Coordinate(0, ycoord), true));
        }
        try {
            player.gameBoard.deploy(ships.get(0), new Coordinate(0, 0), true);
        }catch (IllegalArgumentException e){
            Assertions.assertEquals(e.getClass(),IllegalArgumentException.class);
            System.out.println("did not crash.");
        }
        player.startGame();
        Assertions.assertTrue(player.isGameStarted());
    }

    @Test
    void canGameStart() {
        //Kinda tested in isGameStarted
    }

    @Test
    void startGame() {
        //AlsoTested in isGameStarted
    }

    @Test
    void isGameOver() {
        player.setGameOver();//Realise now how this can be cheated..
        //But on the other hand the game is terribly borring without the controller....
        //Could possilby send an interface that controlls this?
        //But that is beyond my expertise.
        Assertions.assertTrue(player.isGameOver());
    }

    @Test
    void setGameOver() {
        //Tested in isGameOver
    }

    @Test
    void getFleetStatus() {
//Could test in isShotHit.
    }

    @Test
    void isShotHit() {
        List<Ship> ships = player.ships;
        for (int ycoord = 0; ycoord < ships.size(); ycoord++) {
            player.gameBoard.deploy(ships.get(ycoord), new Coordinate(0, ycoord), true);
        }
        try {
            Assertions.assertEquals(IllegalStateException.class, player.isShotHit(new Coordinate(0, 0)));
        }catch(IllegalStateException e){
            System.out.println("Is it coorrect?");
            Assertions.assertEquals(e.getClass(),IllegalStateException.class);//Always true LoL
        }
        System.out.println("Did not crash");
        player.startGame();
        for (int y = 0; y < ships.size(); y++) {
            for (int x = 0; x < ships.get(y).getSize(); x++) {
                assertTrue(player.isShotHit(new Coordinate(x, y)));
                if (y == ships.size() - 1 && x == ships.get(y).size - 1)//Could possibly set game over since fleetStatus ==0
                    assertFalse(player.isShotHit(new Coordinate(0, ships.size())));
            }
        }
    }
}