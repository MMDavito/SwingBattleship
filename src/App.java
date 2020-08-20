import Model.Player;
import View.GameBoardView;

public class App {
    static GameBoardView board = new GameBoardView();
    public static void main(String[]args){
        System.out.println("HEllow swojs");
        Player player1 = new Player("player uno");
        Player player2 = new Player("player dos");

        board.startGame(player1,player2);
    }
}
