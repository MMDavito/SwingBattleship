package Model;

public class Ship {
    private SHIP ship;
    private String type;

    protected int size;
    protected int health;
    protected boolean isDeployed = false;
    private int startX;
    private int startY;
    private boolean isHor;

    public Ship(SHIP ship) {
        if (ship == null) return;
        this.ship = ship;
        this.size = ship.getSize();
        this.health = ship.getSize();
        this.type = ship.toString();
    }

    public String getType() {
        return type;
    }

    public SHIP getShip() {
        return ship;
    }

    public int getSize() {
        return size;
    }

    public int getHealth() {
        return health;
    }

    public boolean isDeployed() {
        return isDeployed;
    }

    void setUnDeployed() {
        isDeployed = false;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public boolean isHor() {
        return isHor;
    }

    /**
     * Will deploy if coordinate is valid (x and y on game board). If fail, boolean "isDeployed" remains false.
     *
     * @param coordinate Start coordinate
     * @param isHor      Ship orientation, horizontal if true, else vertical.
     */
    void deploy(Coordinate coordinate, boolean isHor) {
        if (coordinate.getX() == null) return;
        this.startX = coordinate.getX();
        this.startY = coordinate.getY();
        this.isHor = isHor;
        isDeployed = true;
    }
}