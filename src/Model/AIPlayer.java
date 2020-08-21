package Model;
//TODO: check player2 class in controller

import java.util.Random;

/**
 * Currently verry rudimental AI, places randomly.
 * Does not remember where previously placed
 * Shoots randomly => therefore needs to be handled by controller. (so not shoots same place twice)
 */
public class AIPlayer extends Player {
    private final Coordinate coordinateHelper = new Coordinate(null, null);

    /**
     * Initiates an list of ships with the order and sizes specified by the enums in <code>SHIP</code>.
     *
     * @param name If null or empty the name of the player will remain empty. Which would not allow save off highscore.
     *             , else it will be used in high-score.
     */
    public AIPlayer(String name) {
        super(name);
    }

    public Coordinate getRandomShot() {
        Random random = new Random();
        int x = random.nextInt(coordinateHelper.width - 1);
        int y = random.nextInt(coordinateHelper.height - 1);
        Coordinate coordinate = new Coordinate(x, y);
        return coordinate;
    }

    /**
     * Extremly naive/crude.
     * random guess, no strategy, and is really an loop in an loop.
     */
    public void placeAllShips() {
        Random random = new Random();
        for (Ship ship : ships) {
            boolean isHor = random.nextBoolean();
            int x;
            int y;
            while (true) {
                if (isHor) {//exlude shipSize horizontally
                    x = random.nextInt(coordinateHelper.width - ship.getSize());
                    y = random.nextInt(coordinateHelper.height - 1);
                    Coordinate coordinate = new Coordinate(x, y);
                    if (gameBoard.isDeployable(ship, coordinate, isHor)) {
                        gameBoard.deploy(ship, coordinate, isHor);
                        break;
                    }
                } else {//Exclude shipzise vertically
                    x = random.nextInt(coordinateHelper.width - 1);
                    y = random.nextInt(coordinateHelper.height - ship.getSize());
                    Coordinate coordinate = new Coordinate(x, y);
                    if (gameBoard.isDeployable(ship, coordinate, isHor)) {
                        gameBoard.deploy(ship, coordinate, isHor);
                        break;
                    }
                }
            }
        }
    }
}