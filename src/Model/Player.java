package Model;

import java.util.ArrayList;

public class Player {
    public ArrayList<Ship> ships = new ArrayList<Ship>(5);//Could be array, but modular for modularity.
    public GameBoard gameBoard = new GameBoard();
    protected boolean isGameStarted = false;
    protected boolean isGameOver = false;
    private String name = null;
    private int numShots = 0;
    /**
     * Initiates an list of ships with the index specified by the enums in <code>SHIP</code>.
     *
     * @param name If null or empty the name of the player will remain empty, else it will be used in high-score.
     */
    public Player(String name) {
        setName(name);
        SHIP[] shipEnums = SHIP.values();
        for(SHIP shipEnum: shipEnums){
            ships.add(new Ship(shipEnum));
        }
    }

    public int getNumShots() {
        return numShots;
    }

    public String getName() {
        return name;
    }

    /**
     * If name is null, empty, or longer than 20 characters it will be ignored.
     *
     * @param name
     */
    public void setName(String name) {
        if (name != null && name.length() > 0 && name.length() < 20) this.name = name;
        this.name = name;
    }


    boolean isGameStarted() {
        return isGameStarted;
    }

    public boolean canGameStart() {
        for (Ship s : ships) {
            if (!s.isDeployed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Starts game
     *
     * @throws IllegalStateException If there are ships that have not yet been deployed
     */
    public void startGame() throws IllegalStateException {
        //Else game should start, if all ships are deployed.
        for (Ship s : ships) {
            if (!s.isDeployed) {
                throw new IllegalStateException("Can't start game unless all ships are deployed." +
                        "\nShip: " + s.getClass().getName() + ", is not deployed.");//TODO: Test so it's printout works!
            }
        }
        gameBoard.startGame();
        isGameStarted = true;
    }

    boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver() {
        gameBoard.setGameOver();
        isGameOver = true;
    }

    /**
     * @param ship       Enum specifying the ship type to move.
     * @param coordinate Coordinate of first square.
     * @param isHorr     Ship is placed horizontally if true, vertically if false.
     * @throws IllegalStateException If game has already started
     */
    void setShipCoordinate(SHIP ship, Coordinate coordinate, boolean isHorr) throws IllegalStateException {
        if (isGameStarted()) {
            throw new IllegalStateException("The ship can't change location when the game has started");
        }
        throw new UnsupportedOperationException();
    }

    public ArrayList<Integer> getFleetStatus() {
        ArrayList<Integer> healths = new ArrayList<>();
        for (Ship s : ships) {
            healths.add(s.getHealth());
        }
        return healths;
    }

    /**
     * Returns true if shot is successful.
     *
     * @param coordinate Coordinate shot at.
     * @return True if hit, else false.
     * @throws IllegalStateException If trying to shoot before game is started.
     */
    public boolean isShotHit(Coordinate coordinate) throws IllegalStateException {
        if (!isGameStarted) throw new IllegalStateException("Can not shoot before game starts!");
        boolean isHit = gameBoard.shoot(coordinate);//Throws exception if boat is previously hit. TODO TEST!
        numShots++;
        return isHit;
    }
}