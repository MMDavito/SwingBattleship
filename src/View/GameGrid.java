package View;

import Controller.Controller;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameGrid {
    private HelperClass helperClass = new HelperClass();
    //private boolean isGameStarted = false;//can place || click if false, and it is player turn.
    //public boolean isGameOver = false; //will probably not be used TODO: Remove
    public boolean isP1;//Is player 1.
    int shipIndex = 0;//Carrier
    boolean isHor = true;
    JPanel grid;
    final Coordinate coordinateHelper = new Coordinate(null, null);
    //Hashtable with linked nodes would be better than following, but requires time for design
    public SquareButton[][] squareButtons = new SquareButton[coordinateHelper.width][coordinateHelper.height];
    //public GameBoard
    public Controller gameController;
    public Player thisPlayer;

    public GameGrid(JPanel grid, boolean isP1, Controller gameController) {
        grid.setFocusable(true);
        InputMap inputMap = grid.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke('z'), "Invert Hor");
        ActionMap actionMap = grid.getActionMap();
        actionMap.put("Invert Hor", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isHor = !isHor;//invert
            }
        });

        this.isP1 = isP1;
        this.gameController = gameController;
        setPlayer();
        this.grid = grid;
        grid.setBackground(null);
        grid.setLayout(new GridLayout(coordinateHelper.height, coordinateHelper.width));
        for (int y = 0; y < coordinateHelper.height; y++) {
            for (int x = 0; x < coordinateHelper.width; x++) {
                squareButtons[x][y] = new SquareButton(x, y);
                SquareButton tempButton = squareButtons[x][y];
                tempButton.setBackground(Color.LIGHT_GRAY);
                tempButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        System.out.println("Pressed x:" + tempButton.x + ", y:" + tempButton.y);
                        Coordinate start = new Coordinate(tempButton.x, tempButton.y);
                        if (isPlayersTurn()) {
                            if (!gameController.isGameStarted()) deploy(start);
                            else {
                                shot(start);
                                redraw();
                                gameController.switchTurn();
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
                        if (!isPlayersTurn()) return;
                        Coordinate start = new Coordinate(tempButton.x, tempButton.y);
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
                        if (!isPlayersTurn()) return;
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
                            /*
                            int x = coordinate.getX();

                            while (true) {//Iterate x-plane
                                int y = coordinate.getY();
                                while (y <= endY) {//Iterate y-plane
                                    squareButtons[x][y].setBackground(Color.LIGHT_GRAY);
                                    if (y == endY) break;
                                    y++;
                                }
                                if (x == endX) break;
                                x++;
                            }*/
                        }
                    }
                });
                //Add the button to the grid.
                squareButtons[x][y].setFocusable(false);
                grid.add(squareButtons[x][y]);
            }
        }
    }

    private void shot(Coordinate coordinate) {
        thisPlayer.isShotHit(coordinate);
    }

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

    public boolean isPlayersTurn() {
        if (isP1) return gameController.isP1Turn();
        else return !gameController.isP1Turn();
    }

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
     * Draw before game.
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
     * Draw during game.
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

    public void clearAllUndeployed() {
        System.out.println("Not implemented");
    }

    public int getShipSize() {
        SHIP[] ships = SHIP.values();
        return ships[shipIndex].getSize();
    }

    private void setPlayer() {
        System.out.println(gameController.player1.getName());
        System.out.println(gameController.player2.getName());

        //if (!isPlayersTurn()) return;
        Player player = null;
        if (isP1) player = gameController.player1;
        else player = gameController.player2;
        thisPlayer = player;
        System.out.println("Meaning this player is: "+thisPlayer.getName());
    }
}

