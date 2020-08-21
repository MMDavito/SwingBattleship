package Controller;

import Model.HighScore;
import Model.Player;
import Model.SHIP;
import Model.Ship;
import View.HighScoreScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Grew sick of access controll the moment I realised i was devolopeing desktop, and not API.
 */
public class Controller {
    public Player player1;
    public Player player2;
    private boolean isP1Turn = true;//p1 always starts
    private boolean isGameStarted = false;//Deploy ships
    private boolean isGameOver = false;
    private DefaultListModel<String> p1ScoreBoard;
    private DefaultListModel<String> p2ScoreBoard;
    int gameRound = 0;//Indexes each player2 round, player2 shots first.
    public boolean p2IsAi = false;

    public Controller(DefaultListModel<String> p1ScoreBoard, DefaultListModel<String> p2ScoreBoard, Player player1, Player player2) {
        //player1 = new Player("Player_1");
        //player2 = new Player("Player_2");
        this.player1 = player1;
        this.player2 = player2;
        this.p1ScoreBoard = p1ScoreBoard;
        this.p2ScoreBoard = p2ScoreBoard;
        ArrayList<String> p1StringScores = new ArrayList<>(10);
        ArrayList<String> p2StringScores = new ArrayList<>(10);
        ArrayList<Integer> p1Scores = player1.getFleetStatus();
        ArrayList<Integer> p2Scores = player2.getFleetStatus();
        for (int i = 0; i < p1Scores.size(); i++) {
            p1StringScores.add(SHIP.values()[i].toString() + ":");
            p1StringScores.add(p1Scores.get(i).toString());
            p2StringScores.add(SHIP.values()[i].toString() + ": ");
            p2StringScores.add(p2Scores.get(i).toString());
        }
        /*
        for (int i = 0; i < 5; i++) {
            String temp = "Bajs" + i;
            arrayList.add(temp);
        }
        */
        this.p1ScoreBoard.addAll(p1StringScores);
        this.p2ScoreBoard.addAll(p2StringScores);
        //ListModel<String> p1LModel = this.p1ScoreBoard.getModel();

        //ListModel<String> p2LModel = this.p2ScoreBoard.getModel();
    }

    public boolean isP1Turn() {
        return isP1Turn;
    }

    private void setGameOver() {
        isGameOver = true;
        player1.setGameOver();
        player2.setGameOver();

        updateList();//TODO open highscore screen.
        HighScore highScore = new HighScore(player1, player2, gameRound, p2IsAi);
        highScore.writeToFile();
        HighScoreScreen highScoreScreen = new HighScoreScreen();
        highScoreScreen.open();
    }

    private void saveHighScore() {

    }

    /**
     * Inverts players turn.
     * Allows skipping turns, so view need to control shooting.
     */
    public void switchTurn() {
        if (isGameOver()) {
            System.out.println("isGamover");
            updateList();
        } else if (isP1Turn) {
            if (!isGameStarted) {
                if (!player1.canGameStart()) return;
                isP1Turn = false;
            } else if (isPlayerLoser(player2)) {
                setGameOver();
            } else {
                updateList();
                isP1Turn = false;
            }
        }
        //Else is p2 turn
        else if (!isGameStarted) {
            if (!player2.canGameStart()) return;
            isP1Turn = false;//Lets p2 guess first since already at keyboard
            isGameStarted = true;
            player1.startGame();
            player2.startGame();
            gameRound = 1;
            System.out.println("Round: " + gameRound);


        } else if (isPlayerLoser(player1)) {
            setGameOver();
        } else {
            gameRound++;
            System.out.println("Round: " + gameRound);
            updateList();
            isP1Turn = true;
        }
    }

    public boolean isPlayerLoser(Player player) {
        List<Integer> shipsHealth = player.getFleetStatus();
        for (int health : shipsHealth) {
            if (health > 0) return false;
        }
        return true;
    }

    public void updateList() {
        ArrayList<Integer> p1Scores = player1.getFleetStatus();
        ArrayList<Integer> p2Scores = player2.getFleetStatus();
        for (int i = 0; i < p1Scores.size(); i++) {
            p1ScoreBoard.set((i * 2) + 1, p1Scores.get(i).toString());
            p2ScoreBoard.set((i * 2) + 1, p2Scores.get(i).toString());
        }
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
