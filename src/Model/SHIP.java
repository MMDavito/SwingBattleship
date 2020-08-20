package Model;

public enum SHIP{
    Carrier(5),
    Battleship(4),
    Cruiser(3),
    Submarine(3),
    Destroyer(2);
    private int size;
    private SHIP(int size){
        this.size=size;
    }
    public int getSize() {
        return size;
    }
}