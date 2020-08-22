package Model;

import java.util.*;

/**
 * Currently very rudimental AI, places randomly.
 * Does not remember where previously placed
 * Shoots randomly => therefore needs to be handled by controller. (so not shoots same place twice)
 */
public class AIPlayer extends Player {
    private final Coordinate coordinateHelper = new Coordinate(null, null);
    private Queue<Coordinate> nextShotQueue;

    /**
     * Initiates an list of ships with the order and sizes specified by the enums in <code>SHIP</code>.
     *
     * @param name If null or empty the name of the player will remain empty. Which would not allow save off highscore.
     *             , else it will be used in high-score.
     */
    public AIPlayer(String name) {
        super(name);
        initNextShotQueue();
    }

    /**
     * @return Coordinate of next shot, or Null if game is not started, or queue is empty.
     */
    public Coordinate getRandomShot() {
        if (!isGameStarted()) {
            return null;
        } else if (nextShotQueue.isEmpty()) {
            return null;
        } else {
            return nextShotQueue.poll();
        }
    }

    private void initNextShotQueue() {
        /*
        Could have implemented as an method queue.getRandom().
        But this is simpler implementation without loosing too much.
         */
        nextShotQueue = new LinkedList<>();
        LinkedList<Coordinate> randomCoordinates = new LinkedList<>();
        for (int x = 0; x < coordinateHelper.width; x++) {
            for (int y = 0; y < coordinateHelper.height; y++) {
                randomCoordinates.add(new Coordinate(x, y));
            }
        }
        Collections.shuffle(randomCoordinates);
        for (Coordinate coordinate : randomCoordinates) {
            nextShotQueue.add(coordinate);
        }
    }

    public int getSizeQueue() {
        return nextShotQueue.size();
    }

    /**
     * Extremely naive/crude.
     * It randomly places ships, no strategy, and is really an loop in an loop.
     * if too many ships it starts placing them in start of grid, or if fail throws IllegalStateException.
     *
     * @throws IllegalStateException If it fails to place boat.
     *                               The recommended action on this exception is restart game.
     *                               It is VERY unlikely to occur, but this method should be refactored and
     *                               improved in any case.
     */
    public void placeAllShips() throws IllegalStateException {
        Random random = new Random();
        for (Ship ship : ships) {
            boolean isHor = new Random().nextBoolean();
            int x;
            int y;
            int numTries = 0;
            while (numTries < 5) {
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
            if (!ship.isDeployed()) {
                /*
                This really shouldn't happen, but will happen, for the sake of ods.
                It is however deamed better than a infinite loop.
                 */
                System.err.println("The ai failed to deploy ship: " + ship.getType() + "\n" +
                        "Instead tries to place it at first free space");

                isHor = !isHor;
                /*
                These loops are terribly constructed, as this whole method.
                Should have used while logic from gameController, but this was faster to implement..
                 */
                for (x = 0; x < coordinateHelper.width; x++) {
                    for (y = 0; y < coordinateHelper.height; y++) {
                        if (isHor && x == coordinateHelper.width - ship.getSize() + 1) continue;
                        else if (!isHor && y == coordinateHelper.height - ship.getSize() + 1) continue;
                        else if (gameBoard.isDeployable(ship, new Coordinate(x, y), isHor)) {
                            gameBoard.deploy(ship, new Coordinate(x, y), isHor);
                            return;
                        }//else continue until end of board.
                    }
                }
                throw new IllegalStateException("Failed to deploy ship.");
            }
        }
    }
}