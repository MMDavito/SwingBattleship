package Model;

public class BoardSquare {
    private Ship ship;
    private boolean isHit = false;

    public Ship getShip() {
        return ship;
    }
    public void setShip(Ship ship) {
        this.ship = ship;
    }

    /**
     * If square is previously hit.
     * Used when ships are hidden.
     * @return True if been hit, else False.
     */
    public boolean isHit() {
        return isHit;
    }

    /**
     * Sets isHit to true
     */
    public void setHit() {
        isHit = true;
    }
}
