package Controller;

import Model.Player;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Grew sick of access controll the moment I realised i was devolopeing desktop, and not API.
 */
public class Controller {
    public Player player1 = new Player("Player_1");
    public Player player2 = new Player("Player_2");
    private boolean isP1Turn = true;//p1 always starts
    private boolean isGameStarted = false;//Deploy ships
    private boolean isGameOver = false;
    private DefaultListModel<String> p1ScoreBoard;
    private DefaultListModel<String> p2ScoreBoard;

    public Controller(DefaultListModel<String> p1ScoreBoard, DefaultListModel<String> p2ScoreBoard) {
        this.p1ScoreBoard = p1ScoreBoard;
        this.p2ScoreBoard = p2ScoreBoard;
        ArrayList<String>arrayList = new ArrayList<>(5);
        String[] tempArray = new String[5];
        for (int i = 0; i < 5; i++) {
            String temp = "Bajs" + i;
            tempArray[i] = (temp);
            arrayList.add(temp);
        }
        this.p1ScoreBoard.addAll(arrayList);
        this.p2ScoreBoard.addAll(arrayList);
        //ListModel<String> p1LModel = this.p1ScoreBoard.getModel();

        //ListModel<String> p2LModel = this.p2ScoreBoard.getModel();
    }

    public boolean isP1Turn() {
        return isP1Turn;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
