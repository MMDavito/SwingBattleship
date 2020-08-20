package View;

import Model.Game;
import Model.Player;

import javax.swing.*;
import java.awt.event.*;

public class WelcomeScreen extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField p1NameField;
    private JTextField p2NameField;

    public WelcomeScreen() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
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

    private void onOK() {
        // Check if names are null, else create two new players.
        contentPane.setVisible(false);
        String p1Name = p1NameField.getText();
        if (p1Name == null || p1Name.length() == 0) p1Name = "Player_1";
        String p2Name = p2NameField.getText();
        if (p2Name == null || p2Name.length() == 0) p2Name = "Player_1";
        Player player1 = new Player(p1Name);
        Player player2 = new Player(p2Name);
        new GameBoardView().startGame(player1,player2);

        dispose();//TODO TEST

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        WelcomeScreen dialog = new WelcomeScreen();
        dialog.pack();
        dialog.setVisible(true);
    }
}