package View;

import Model.Player;

import javax.swing.*;
import java.awt.event.*;

public class HighScoreScreen extends JDialog {
    private JPanel contentPane;
    private JButton buttonExit;
    private JList HighScore;

    public HighScoreScreen() {
        setContentPane(contentPane);
        setModal(true);


        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();//returns and exits
    }
    //POPULATE LIST:
    private void populateList(){}

    /**
     * Do not save ai scores.
     * only when win against, and it saves "against ai" as oponent.
     * @param player1
     * @param player2
     * @param isP2AI
     */
    public void open(Player player1, Player player2, boolean isP2AI) {
        HighScoreScreen dialog = new HighScoreScreen();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
