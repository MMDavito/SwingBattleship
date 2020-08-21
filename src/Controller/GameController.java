package Controller;

import Model.*;
import View.HighScoreScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class GameController {
    /**
     * Grew sick of access control the moment I abandoned MVC pattern.
     * I Abandoned MVC when realised I was devolopeing desktop, and not API.
     * Which caused soome hacking and is
     */
    public boolean isP1GODrawn = false;//Is player1 game over drawn?
    public boolean isP2GODrawn = false;//Is player2 game over drawn?

    public Player player1;
    public Player player2;
    private boolean isP1Turn = true;//p1 always starts
    private boolean isGameStarted = false;//Deploy ships
    private boolean isGameOver = false;
    private DefaultListModel<String> p1ScoreBoard;
    private DefaultListModel<String> p2ScoreBoard;
    int gameRound = 0;//Indexes each player2 round, player2 shots first.
    public boolean p2IsAi = false;

    public GameController(DefaultListModel<String> p1ScoreBoard, DefaultListModel<String> p2ScoreBoard, Player player1, Player player2) {
        if (player2.getClass().equals(AIPlayer.class)) {
            if (new HelperClass().ISDEBUG) System.out.println("Is ai");
            p2IsAi = true;
        }
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
        this.p1ScoreBoard.addAll(p1StringScores);
        this.p2ScoreBoard.addAll(p2StringScores);
    }

    public boolean isP1Turn() {
        return isP1Turn;
    }

    /**
     * Sets game over to the both players and saves highscore to file.
     */
    private void setGameOver() {
        isGameOver = true;
        player1.setGameOver();
        player2.setGameOver();

        updateList();
        saveHighScore();
    }

    /**
     * Saves highscore of player1 and player2 to file.
     */
    private void saveHighScore() {
        HighScore highScore = new HighScore(player1, player2, gameRound, p2IsAi);
        if (highScore.isClassNull()) {
            System.out.println("Something happened when constructing highscore, probably player1 or player2 does not have any valid name");
            return;
        }
        highScore.writeToFile();
        HighScoreScreen highScoreScreen = new HighScoreScreen();
        highScoreScreen.open();
    }

    public boolean isPlayerDrawn(boolean isP1) {
        if (isP1) {
            if (!isP1GODrawn) {
                isP1GODrawn = true;
                return false;
            } else return true;
        } else {//is p2
            if (!isP2GODrawn) {
                isP2GODrawn = true;
                return false;
            } else return true;
        }
    }

    /**
     * Inverts players turn.
     * Allows skipping turns, so view need to control shooting.
     */
    public void switchTurn() {
        if (isGameOver()) {
            System.out.println("isGamover");
            updateList();
            //isP1Turn = !isP1Turn;
        }
        if (isP1Turn) {
            if (!isGameStarted) {
                if (!player1.canGameStart()) return;
                isP1Turn = false;
                if (p2IsAi) {
                    gameRound = 1;
                    ((AIPlayer) player2).placeAllShips();
                    player1.startGame();
                    player2.startGame();
                    Coordinate coordinate = null;
                    while (true) {
                        coordinate = ((AIPlayer) player2).getRandomShot();
                        if (player1.gameBoard.boardSquares[coordinate.getX()][coordinate.getY()] != null &&
                                player1.gameBoard.boardSquares[coordinate.getX()][coordinate.getY()].isHit()) {
                            continue;
                        }
                        break;
                    }
                    if (coordinate != null) player1.isShotHit(coordinate);

                    isP1Turn = true;
                    return;
                }
            } else if (isPlayerLoser(player2)) {
                setGameOver();
            } else {
                if (p2IsAi) {
                    Coordinate coordinate = null;
                    while (true) {
                        coordinate = ((AIPlayer) player2).getRandomShot();
                        if (player1.gameBoard.boardSquares[coordinate.getX()][coordinate.getY()] != null &&
                                player1.gameBoard.boardSquares[coordinate.getX()][coordinate.getY()].isHit()) {
                            continue;
                        }
                        break;
                    }
                    if (coordinate != null) player1.isShotHit(coordinate);
                    return;
                }
                updateList();
                isP1Turn = false;
            }
        }
        //Else is p2 turn
        else if (!isGameStarted) {
            // Is not ai
            if (!player2.canGameStart()) return;
            isP1Turn = false;//Lets p2 guess first since already at keyboard
            isGameStarted = true;
            player1.startGame();
            player2.startGame();
            gameRound = 1;
            System.out.println("Round: " + gameRound);
        } else if (
                isPlayerLoser(player1)) {
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

    /**
     * Updates the two scoreboards given to the constructor..
     */
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
