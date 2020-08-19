package Model;

public class Coordinate {
    public final int width = 10;//Width of game board.
    public final int height = 10;//Height of game board.

    private Integer x;
    private Integer y;

    /**
     * Integers x is horizontal coordinate, indexed from the left hand corner.
     * y is vertical, Indexed from the top.
     *
     * @param x Between 0 and not including <code>width</code>, else null
     * @param y Between 0 and not including <code>height</code>, else null
     */
    public Coordinate(Integer x, Integer y) {
        if (x == null || y == null) return;
        if (x < 0 || x >= width) return;
        if (y < 0 || y >= height) return;
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
