package View;

import Controller.GameController;
import Model.HighScore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class HighScoreScreen extends JDialog {
    private JPanel contentPane;
    private JButton buttonExit;
    private JTable table1;
    DefaultTableModel defaultTableModel;
    private GameController gameController;
    private boolean isP2AI;

    public HighScoreScreen() {
        contentPane.setPreferredSize(new Dimension(600, 200));
        setContentPane(contentPane);
        setModal(true);
        defaultTableModel = new DefaultTableModel();
        table1.setModel(defaultTableModel);
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
        HighScore highScoreHelper = new HighScore();
        LinkedList<HighScore> highscores = highScoreHelper.getHighscores();
        /*
        Could SORT more advanced, like when two with same amount of rounds, it could sort based on
        remaining health. But could also have an sort when pressing the list.
        But since not javascript it would just be awful and it would save time to translate the entire project to
        javascript.
        */
        highscores.sort((h1, h2) -> (h1.getGameRound() - h2.getGameRound()));
        LinkedList<String> columns = highScoreHelper.getCsvColumns();
        for (String column : columns) {
            defaultTableModel.addColumn(column);
        }
        for (HighScore highScore : highscores) {
            defaultTableModel.addRow(highScore.toArray());
        }
        defaultTableModel.fireTableDataChanged();
        return;
    }

    public void open() {
        HighScoreScreen dialog = new HighScoreScreen();
        dialog.populateList();
        dialog.pack();
        dialog.setVisible(true);

    }
}
