package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class SquareButton extends JButton implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        currState++;
        System.out.println("Curr state len: "+STATE.values().length);
        System.out.println("Curr state: "+currState);
        if (currState == STATE.values().length) currState = 0;
        switch (currState){
            case 0://STATE.NULL.val
                setIcon(null);
                System.out.println("Hura 0");
                break;
            case 1://STATE.BOAT.getVal()
                setIcon(B);
                System.out.println("Hura 1");

                break;
            case 2://miss
                setIcon(M);
                System.out.println("Hura 2");

                break;
            case 3://hit
                setIcon(H);
                System.out.println("Hura 3");

                break;
        }
    }

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

    ImageIcon H, M, B, N;//Hit,Miss,Boat,Null
    int currState = STATE.NULL.getVal();//null, 1 is ship/ship

    public SquareButton() {
        H = new ImageIcon(this.getClass().getResource("images/" + STATE.HIT.getVal() + ".jpg"));
        M = new ImageIcon(this.getClass().getResource("images/" + STATE.MISS.getVal() + ".png"));
        B = new ImageIcon(this.getClass().getResource("images/" + STATE.BOAT.getVal() + ".jpg"));
        N = new ImageIcon(this.getClass().getResource("images/" + STATE.NULL.getVal() + ".png"));
        this.setPreferredSize(new Dimension(50,50));
        //this.setSize(10,10);
        addActionListener(this);//Move this to GameBoard and use XObutton.addListener(this) so it triggers in controller

    }
}
