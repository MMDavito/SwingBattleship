package Model;

/**
 * This class represents an gameboard for the game of "Battleship", it is indexed from top left corner.
 */
public class GameBoard {
    private final Coordinate coordinateHelper = new Coordinate(null, null);
    int width = coordinateHelper.width;
    int height = coordinateHelper.height;
    private boolean isGameStarted = false;
    private boolean isGameOver = false;

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void startGame() {
        isGameStarted = true;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Sets game over to true
     */
    public void setGameOver() {
        isGameStarted = true;
        isGameOver = true;
    }

    public BoardSquare boardSquares[][] = new BoardSquare[width][height];

    /**
     * Verifies if the coordinates exists on the board.
     *
     * @param coordinate Horizontal coordinate
     * @return True if coordinate is valid (not null)
     */
    boolean isOnBoard(Coordinate coordinate) {
        if (coordinate.getX() == null) return false;
        else return true;
    }

    /**
     * Returns the last coordinate, based on the start coordinate, size of ship and orientation of ship.
     *
     * @param ship
     * @param coordinate Starting coordinate.
     * @param isHor      Orientation of ship (horizontal if true, else vertical).
     * @return Null if invalid coordinates, start or end. Else the coordinates at the right bottom of the ship.
     */
    Coordinate getEnd(Ship ship, Coordinate coordinate, boolean isHor) {
        if (coordinate.getX() == null) return null;
        int endX = coordinate.getX();
        int endY = coordinate.getY();
        if (isHor) {
            endX += ship.getSize() - 1;
        } else {//Is vertical
            endY += ship.getSize() - 1;
        }
        Coordinate end = new Coordinate(endX, endY);
        if (!isOnBoard(end)) return null;
        return end;
    }

    /**
     * Checks if it is possible to place ship in specified area.
     *
     * @param ship       Ship to place.
     * @param coordinate Starting coordinate.
     * @param isHor      True if horizontal placement, else vertical.
     * @return False if square/squares is occupied or trying to place ship outside of board.
     */
    public boolean isDeployable(Ship ship, Coordinate coordinate, boolean isHor) {
        if (coordinate.getX() == null)
            return false;
        int startX = coordinate.getX();
        int startY = coordinate.getY();
        Coordinate end = getEnd(ship, coordinate, isHor);
        if (end == null) return false;
        int endX = end.getX();
        int endY = end.getY();

        if (endX >= width || endY >= height)
            return false;

        int x = startX;
        while (true) {//Iterate x-plane
            int y = startY;
            while (y <= endY) {//Iterate y-plane
                if (boardSquares[x][y] != null && boardSquares[x][y].getShip() != null) return false;
                if (y == endY) break;
                y++;
            }
            if (x == endX) break;
            x++;
        }
        return true; //Area is free.
    }

    /**
     * Deploys ship if it is not deployed, or if the area is not occupied.
     *
     * @param ship
     * @param coordinate Starting coordinate
     * @param isHor      The orientation of the ship, horizontal if true, else vertical
     * @return False if ship is undeployable (coordinate illegal, or another ship already exists at given coordinates).
     * True if it succeeds at deployment.
     * @throws IllegalArgumentException If ship is already deployed, one must undeploy ship before redeploying it.
     */
    public boolean deploy(Ship ship, Coordinate coordinate, boolean isHor) throws IllegalArgumentException {
        if (ship.isDeployed) throw new IllegalArgumentException("Ship is already deployed");
        if (!isDeployable(ship, coordinate, isHor)) return false;

        Coordinate end = getEnd(ship, coordinate, isHor);
        if (end == null) return false;
        int endX = end.getX();
        int endY = end.getY();

        //Iterate the coordinates given and place/deploy the ship.
        int x = coordinate.getX();
        while (true) {//Iterate x-plane
            int y = coordinate.getY();
            while (y <= endY) {//Iterate y-plane
                boardSquares[x][y] = new BoardSquare();
                boardSquares[x][y].setShip(ship);
                if (y == endY) break;
                y++;
            }
            if (x == endX) break;
            x++;
        }
        ship.deploy(coordinate, isHor);
        return true; //Area is free.
    }

    /**
     * Undeploy ship, does however not change startX,startY and isHor in <code>ship</code>
     *
     * @param ship
     * @return False if not deployed or if game has started, true if success.
     */
    boolean undeploy(Ship ship) {
        if (isGameStarted) return false;
        if (!ship.isDeployed) return false;
        int startX = ship.getStartX();
        int startY = ship.getStartY();
        Coordinate coordinate = new Coordinate(startX, startY);
        boolean isHor = ship.isHor();

        Coordinate end = getEnd(ship, coordinate, isHor);
        /*
        Could avoid following by only allowing ships to deploy at valid coordinates, with reference to length and orientation.
        But the information expert is not the ship, so one should not use the boat directly outside game-board
        */
        if (end == null) throw new IllegalStateException("This should never happen. \n" +
                "Somebody sat boat to deployed without placing it on the game-board.");

        int endX = end.getX();
        int endY = end.getY();

        int x = startX;
        while (true) {//Iterate x-plane
            int y = startY;
            while (y <= endY) {//Iterate y-plane
                boardSquares[x][y].setShip(null);
                if (y == endY) break;
                y++;
            }
            if (x == endX) break;
            x++;
        }
        ship.setUnDeployed();
        return true; //Boat is undeployed.
    }

    /**
     * @param coordinate Starting coordinate
     * @return True if previously coordinate is previously shot.
     * @throws IllegalArgumentException If coordinate not valid.
     */
    public boolean isPreviouslyHit(Coordinate coordinate) throws IllegalArgumentException {
        if (!isOnBoard(coordinate)) throw new IllegalArgumentException("Coordinates outside of board.");
        int x = coordinate.getX();
        int y = coordinate.getY();
        if (boardSquares[x][y] == null) return false;
        return boardSquares[x][y].isHit();
    }

    /**
     * Shoots a shot, decrease health of ship if hit.
     * Does not allow shooting twice in the same spot/coordinate.
     *
     * @param coordinate
     * @return True if hit, false if miss.
     * @throws IllegalStateException If coordinate is previously hit. Or if game not started, or game over.
     */
    boolean shoot(Coordinate coordinate) throws IllegalStateException {
        if (!isGameStarted() || isGameOver()) throw new IllegalStateException("" +
                "Cannot shoot if game not started, or if game over.");
        if (isPreviouslyHit(coordinate)) throw new IllegalStateException("Coordinate is previously hit.");
        int x = coordinate.getX();
        int y = coordinate.getY();
        if (boardSquares[x][y] == null) {
            boardSquares[x][y] = new BoardSquare();
            boardSquares[x][y].setHit();
        }
        if (boardSquares[x][y].getShip() == null) {//Shot missed boat, but square will be marked.
            boardSquares[x][y].setHit();
            return false;
        }
        //Else there is an unhit ship.
        Ship s = boardSquares[x][y].getShip();
        s.health--;
        boardSquares[x][y].setHit();
        return true;
    }
}
