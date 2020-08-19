package View;

import Model.Coordinate;

import javax.swing.*;
import java.awt.*;

public class GameGrid {
    JPanel grid;
    final Coordinate coordinateHelper = new Coordinate(null, null);
    //Hashtable with linked nodes would be better than following, but requires time for design
    SquareButton [][]squareButtons = new SquareButton[coordinateHelper.width][coordinateHelper.height];


    public GameGrid(JPanel grid) {
        this.grid = grid;
        grid.setBackground(null);
        grid.setLayout(new GridLayout(coordinateHelper.width, coordinateHelper.height));
        for(int x = 0;x<coordinateHelper.width;x++){
            for(int y = 0;y<coordinateHelper.height;y++){
                squareButtons[x][y] = new SquareButton();
                grid.add(squareButtons[x][y]);
            }

        }
    }
}
