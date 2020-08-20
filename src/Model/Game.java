package Model;

import java.util.ArrayList;

public class Game {
    //TODO: Bestämm om jag skall använda seperate game for AI and human, eller om det är en senare abstraktion. //Undersök detta gentemot UML-kursen.
    public Player player1;
    public Player player2;
    private boolean isPlayer1sTurn = true;
    private boolean isGameStarted = false;
    private boolean isGameOver = false;

    public Game(Player player1, Player player2) throws IllegalArgumentException {
        if (player1 == null || player2 == null) throw new IllegalArgumentException("Players cannot be null");
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * It is recommended to call the method "canGameStart()" on the players to avoid any exceptions.
     *
     * @throws IllegalStateException If fail to start game (likely some player have not initiated all its ships).
     */
    public void startGame() throws IllegalStateException {
        player1.startGame();//Throws exception
        player2.startGame();//Throws exception
        isGameStarted = true;
    }

    private void setGameOver() {
        player1.setGameOver();
        player2.setGameOver();
        isGameOver = true;

        ArrayList<Integer> p1Health = player1.getFleetStatus();
        int p1TotalHealth = 0;
        for (int h : p1Health) {
            p1TotalHealth += h;
        }
        ArrayList<Integer> p2Health = player2.getFleetStatus();
        int p2TotalHealth = 0;
        for (int h : p2Health) {
            p2TotalHealth += h;
        }
        int p1Shots = player1.getNumShots();
        int p2Shots = player2.getNumShots();
    }
}
