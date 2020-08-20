package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SquareButton extends JButton {
    ImageIcon H, M, B, N;//Hit,Miss,Boat,Null
    int currState = STATE.NULL.getVal();//null, 1 is ship/ship
    public Integer x;
    public Integer y;


    public enum STATE {
        NULL(0),
        BOAT(1),
        MISS(2),
        HIT(3);

        private int val;

        STATE(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }

    public SquareButton(Integer x,Integer y) {
        if (x!=null&&y!=null){//To not allow for the one being set to a Integer without the other.
            this.x=x;
            this.y=y;
        }
        /*
        H = new ImageIcon(this.getClass().getResource("images/" + STATE.HIT.getVal() + ".jpg"));
        M = new ImageIcon(this.getClass().getResource("images/" + STATE.MISS.getVal() + ".png"));
        B = new ImageIcon(this.getClass().getResource("images/" + STATE.BOAT.getVal() + ".jpg"));
        N = new ImageIcon(this.getClass().getResource("images/" + STATE.NULL.getVal() + ".png"));

         */
        this.setPreferredSize(new Dimension(50,50));
        //this.setSize(10,10);

    }
}
