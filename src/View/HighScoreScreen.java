package View;

import Controller.Controller;
import Model.Player;

import javax.swing.*;
import java.awt.event.*;

public class HighScoreScreen extends JDialog {
    private JPanel contentPane;
    private JButton buttonExit;
    private JList HighScore;
    private Controller gameController;
    private boolean isP2AI;

    public HighScoreScreen(Controller gameController, boolean isP2AI) {
        if (gameController == null)
            throw new IllegalArgumentException("Controller cannot be null");//Could test so players aren't null.
        setContentPane(contentPane);
        setModal(true);
        this.gameController = gameController;
        this.isP2AI = false;

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
    private void populateList() {
    }

    /**
     * Do not save ai scores.
     * only when win against, and it saves "against ai" as oponent.
     *
     * @param player1
     * @param player2
     * @param isP2AI
     */
    public void open() {
        HighScoreScreen dialog = new HighScoreScreen();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void printHighscore()
}
