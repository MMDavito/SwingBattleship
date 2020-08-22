package View;

import Controller.GameController;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This is the graphical representation that also handles some of the controls for each of the two players
 * playing Battleship.
 */
public class GameGrid {
    private HelperClass helperClass = new HelperClass();
    private Coordinate currCor;
    public boolean isP1;//Is player 1.
    int shipIndex = 0;//Carrier
    public boolean isHor = true;
    JPanel grid;
    final Coordinate coordinateHelper = new Coordinate(null, null);
    //Hashtable with linked nodes would be better than following, but requires time for design
    public SquareButton[][] squareButtons = new SquareButton[coordinateHelper.width][coordinateHelper.height];
    //public GameBoard
    public GameController gameController;
    public Player thisPlayer;

    /**
     * @param grid           The panel the gaming grid should be created.
     * @param isP1           If it is player 1.
     * @param gameController Containing controller and both of the players.
     *                       it is used for coordinating the two gamegrids and  controlling the game.
     */
    public GameGrid(JPanel grid, boolean isP1, GameController gameController) {
        this.isP1 = isP1;
        this.gameController = gameController;
        setPlayer();
        this.grid = grid;
        grid.setBackground(null);
        boolean isGameDrawn = false;
        grid.setLayout(new GridLayout(coordinateHelper.height, coordinateHelper.width));
        for (int y = 0; y < coordinateHelper.height; y++) {
            for (int x = 0; x < coordinateHelper.width; x++) {
                //Initiate the board:
                squareButtons[x][y] = new SquareButton(x, y);
                SquareButton tempButton = squareButtons[x][y];
                //Set the background to default light_gray (keep it minimilistic)
                tempButton.setBackground(Color.LIGHT_GRAY);
                //The controller/mouselistener for each of the buttons in the grid:
                tempButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        if (helperClass.ISDEBUG)
                            System.out.println("Pressed x:" + tempButton.x + ", y:" + tempButton.y);
                        Coordinate start = new Coordinate(tempButton.x, tempButton.y);
                        if ((isPlayersTurn() && !gameController.isGameStarted()) ||
                                (!isPlayersTurn() && gameController.isGameStarted())) {
                            if (!gameController.isGameStarted()) deploy(start);
                            else if (!thisPlayer.gameBoard.isPreviouslyHit(start)) {
                                shot(start);
                                redraw();
                                gameController.switchTurn();
                            } else {
                                System.out.println("Cant shoot square already shot");
                                return;//cant shoot previosly shot grid
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {
                        if (isP1 && gameController.p2IsAi && gameController.isGameStarted() && !gameController.isP1Redrawn) {
                            redraw();
                            gameController.isP1Redrawn = true;
                        }
                        if (gameController.isGameOver()) {
                            if (gameController.isPlayerDrawn(isP1)) return;
                            if (helperClass.ISDEBUG)
                                System.out.println("Is game drawn?" + gameController.isPlayerDrawn(isP1));
                            drawGameOver();
                        }
                        if (!isPlayersTurn() || gameController.isGameStarted()) return;
                        Coordinate start = new Coordinate(tempButton.x, tempButton.y);
                        currCor = start;
                        Coordinate end = getEnd(getShipSize(), start, isHor);
                        if (isOnBoard(end)) {
                            draw(start, end, Color.GRAY);
                        } else {//end is outside board
                            int endX = start.getX();
                            int endY = start.getY();
                            if (isHor) {
                                endX = coordinateHelper.width - 1;
                            } else {
                                endY = coordinateHelper.height - 1;
                            }
                            end = new Coordinate(endX, endY);
                            draw(start, end, Color.RED);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {
                        if (gameController.isGameOver()) {
                            if (gameController.isPlayerDrawn(isP1)) return;
                            if (helperClass.ISDEBUG)
                                System.out.println("Is game drawn?" + gameController.isPlayerDrawn(isP1));
                            drawGameOver();
                        }
                        if (!isPlayersTurn() || gameController.isGameStarted()) return;
                        currCor = null;
                        Coordinate coordinate = new Coordinate(tempButton.x, tempButton.y);
                        Coordinate end = getEnd(getShipSize(), coordinate, isHor);
                        if (isOnBoard(end)) {
                            draw(coordinate, end, Color.LIGHT_GRAY);
                        } else {//end is outside board
                            int endX = coordinate.getX();
                            int endY = coordinate.getY();
                            if (isHor) {
                                endX = coordinateHelper.width - 1;
                            } else {
                                endY = coordinateHelper.height - 1;
                            }
                            //Clear the red used to signal being outside board.
                            end = new Coordinate(endX, endY);
                            draw(coordinate, end, Color.LIGHT_GRAY);
                        }
                    }
                });
                //Add the button to the grid.
                grid.add(squareButtons[x][y]);
            }
        }
    }

    /**
     * Shots a shot. DOES NOT INFORM THE CONTROLLER!!!!
     *
     * @param coordinate
     */
    private void shot(Coordinate coordinate) {
        thisPlayer.isShotHit(coordinate);
    }

    /**
     * Verifies the coordinate being on board or not.
     * Used for verifying that the end coordinate not returning null.
     *
     * @param coordinate
     * @return true if coordinate not null, or the coordinate not initiated.
     * If true it has nothing to do with the model, it simply is not initiated, possibly because
     * call to "getEnd()" returned null?.
     */
    boolean isOnBoard(Coordinate coordinate) {
        if (coordinate == null || coordinate.getX() == null) return false;
        else return true;
    }

    /**
     * Returns the last coordinate, based on the start coordinate, size of ship and orientation of ship.
     *
     * @param shipSize   Size of ship
     * @param coordinate Starting coordinate.
     * @param isHor      Orientation of ship (horizontal if true, else vertical).
     * @return Null if invalid coordinates, start or end. Else the coordinates at the right bottom of the ship.
     */
    Coordinate getEnd(int shipSize, Coordinate coordinate, boolean isHor) {
        if (coordinate == null || coordinate.getX() == null) return null;
        int endX = coordinate.getX();
        int endY = coordinate.getY();
        if (isHor) {
            endX += shipSize - 1;
        } else {//Is vertical
            endY += shipSize - 1;
        }
        Coordinate end = new Coordinate(endX, endY);
        if (!isOnBoard(end)) return null;
        return end;
    }

    /**
     * @return True if can it is players turn to place ships OR opponents turn to shot at the grid, else false.
     * This is further controlled in the GameController.
     */
    public boolean isPlayersTurn() {
        if (isP1) return gameController.isP1Turn();
        else return !gameController.isP1Turn();
    }

    /**
     * Deploys ship and increase the shipIndex, or switches turn if all ships are deployed.
     *
     * @param startCoord
     */
    public void deploy(Coordinate startCoord) {
        Ship ship = thisPlayer.ships.get(shipIndex);
        if (ship.isDeployed()) {
            if (helperClass.ISDEBUG) System.out.println("Not implemented undeploy in view");
            return;//Not supported TODO find time to implement?
        }
        if (!thisPlayer.gameBoard.isDeployable(ship, startCoord, isHor)) {
            if (helperClass.ISDEBUG)
                System.out.println("No real warning implemented in view regarding placing illegally\n" +
                        "simply returns.");
            return;
        }//Is deployable
        thisPlayer.gameBoard.deploy(ship, startCoord, isHor);
        //Draw the ship.
        Coordinate end = getEnd(ship.getSize(), startCoord, isHor);
        draw(startCoord, end, Color.green);
        if (shipIndex >= SHIP.values().length - 1) {
            gameController.switchTurn();
            redraw();
        } else shipIndex++;

    }

    /**
     * Draw before game is started.
     *
     * @param start
     * @param end
     * @param color
     */
    private void draw(Coordinate start, Coordinate end, Color color) {
        if (gameController.isGameStarted() && !gameController.isGameOver()) {
            if (helperClass.ISDEBUG) System.out.println("Cant draw during game");
            return;
        }
        int x = start.getX();
        while (true) {//Iterate x-plane
            int y = start.getY();
            while (y <= end.getY()) {//Iterate y-plane
                if (thisPlayer.gameBoard.boardSquares[x][y] != null && thisPlayer.gameBoard.boardSquares[x][y].getShip() != null) {
                    squareButtons[x][y].setBackground(Color.GREEN);
                } else {
                    squareButtons[x][y].setBackground(color);
                }
                if (y == end.getY()) break;
                y++;
            }
            if (x == end.getX()) break;
            x++;
        }
    }

    /**
     * Use this <code>redraw()</code> during game.
     * Prints hits in red. and misses in black. All else light_gray
     */
    public void redraw() {
        BoardSquare[][] playerSquares = thisPlayer.gameBoard.boardSquares;
        for (int x = 0; x < coordinateHelper.width; x++) {
            for (int y = 0; y < coordinateHelper.height; y++) {
                if (playerSquares[x][y] == null) {
                    squareButtons[x][y].setBackground(Color.LIGHT_GRAY);
                } else if (!playerSquares[x][y].isHit()) {
                    squareButtons[x][y].setBackground(Color.LIGHT_GRAY);
                } else {//Is hit
                    if (playerSquares[x][y].getShip() == null) squareButtons[x][y].setBackground(Color.BLUE);//MISSED
                    else squareButtons[x][y].setBackground(Color.RED);//HIT
                }
            }
        }
    }

    /**
     * Returns the size of the ship with current <code>shipIndex</code>
     *
     * @return
     */
    public int getShipSize() {
        SHIP[] ships = SHIP.values();
        return ships[shipIndex].getSize();
    }

    /**
     * Sets thisPlayer to the correct player from the provided <code>GameController</code> and based
     * on the boolean <code>isP1</code>.
     */
    private void setPlayer() {
        Player player = null;
        if (isP1) player = gameController.player1;
        else player = gameController.player2;
        thisPlayer = player;
    }

    /**
     * Inverts isHor if is current players turn.
     * Is called from GameBoardView since it proved best for binding keys.
     */
    public void invertHor() {
        if (!isPlayersTurn() || gameController.isGameStarted()) return;
        isHor = !isHor;
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(coordinateHelper.width - 1, coordinateHelper.width - 1);
        draw(start, end, Color.LIGHT_GRAY);
        start = currCor;
        if (start == null) return;
        drawShip(start);
    }

    /**
     * Draws ship when mouseHover over a button,
     * also controls if valid, and in these cases sets background red instead of GRAY
     *
     * @param start
     */
    public void drawShip(Coordinate start) {
        Coordinate end = getEnd(SHIP.values()[shipIndex].getSize(), start, isHor);
        if (isOnBoard(end)) {
            draw(start, end, Color.GRAY);
        } else {//end is outside board
            int endX = start.getX();
            int endY = start.getY();
            if (isHor) {
                endX = coordinateHelper.width - 1;
            } else {
                endY = coordinateHelper.height - 1;
            }
            end = new Coordinate(endX, endY);
            draw(start, end, Color.RED);
        }
    }

    public void drawGameOver() {
        if (!thisPlayer.isGameOver()) return;
        draw(new Coordinate(0, 0), new Coordinate(coordinateHelper.width - 1, coordinateHelper.height - 1), Color.GRAY);
        BoardSquare[][] boardSquares = thisPlayer.gameBoard.boardSquares;

        int x = 0;
        while (true) {//Iterate x-plane
            int y = 0;
            while (true) {//Iterate y-plane
                if (boardSquares[x][y] != null && boardSquares[x][y].getShip() != null && boardSquares[x][y].isHit()) {
                    squareButtons[x][y].setBackground(Color.RED);
                } else if (boardSquares[x][y] != null && boardSquares[x][y].isHit()) {//not boat
                    squareButtons[x][y].setBackground(Color.BLUE);
                }
                if (y == coordinateHelper.height - 1) break;
                y++;
            }
            if (x == coordinateHelper.height - 1) break;
            x++;
        }
    }
}

