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
     * Initiates an list of ships with the order and sizes specified by the enums in <code>SHIP</code>.
     *
     * @param name If null or empty the name of the player will remain empty. Which would not allow save off highscore.
     *             , else it will be used in high-score.
     */
    public Player(String name) {
        setName(name);
        SHIP[] shipEnums = SHIP.values();
        for (SHIP shipEnum : shipEnums) {
            ships.add(new Ship(shipEnum));
        }
    }

    /**
     * @return Number of shots the player have been shot with.
     */
    public int getNumShots() {
        return numShots;
    }

    public String getName() {
        return name;
    }

    /**
     * If name is null, empty, it will be ignored.
     *
     * @param name
     */
    public void setName(String name) {
        if (name != null && name.length() > 0) this.name = name;
        else this.name = null;
    }


    public boolean isGameStarted() {
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
                        "\nShip: " + s.getClass().getName() + ", is not deployed.");
            }
        }
        gameBoard.startGame();
        isGameStarted = true;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Sets game over in gameboard and locally in player.
     */
    public void setGameOver() {
        gameBoard.setGameOver();
        isGameOver = true;
    }

    /**
     * Returns ArrayList with the health of the ships in the order they where initiated in the constructor.
     * I.e. in the same order they are listed in the enum <code>SHIP</code>
     *
     * @return
     */
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
        HelperClass helperClass = new HelperClass();
        if (helperClass.CHEAT) {
            System.out.println("YOU BLOODY CHEATER!!!!");
            for (Ship ship : ships) {
                ship.health = 0;
            }
            return true;
        }
        if (!isGameStarted) throw new IllegalStateException("Can not shoot before game starts!");
        boolean isHit = gameBoard.shoot(coordinate);//Throws exception if boat is previously hit. TODO TEST!
        numShots++;
        return isHit;
    }
}