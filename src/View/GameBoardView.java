package View;

import Controller.Controller;
import Model.Player;

import javax.swing.*;
import java.awt.*;

public class GameBoardView {
    boolean isPlayer1Turn = true;
    GameGrid topGrid;
    GameGrid bottomGrid;

    JPanel topPanel = new JPanel();
    JPanel bottomPanel = new JPanel();
    JPanel window = new JPanel();
    JFrame windowF = new JFrame();

    public void startGame() {
        DefaultListModel<String> p1Score = new DefaultListModel<>();
        DefaultListModel<String> p2Score = new DefaultListModel<>();
        Controller gameController = new Controller(p1Score, p2Score);
        topGrid = new GameGrid(new JPanel(), false, gameController);//player2 grid
        bottomGrid = new GameGrid(new JPanel(), true,gameController);//player 1 grid

        JList<String> topScore = new JList<>(p2Score);//p2Score
        JList<String> bottomScore = new JList<>(p1Score);//p1Score


        window.setBackground(Color.BLACK);
        topPanel.setBackground(Color.BLACK);
        bottomPanel.setBackground(Color.red);
        //JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,topPanel,bottomPanel);
        //        window.add(splitPane);
        //      splitPane.setDividerLocation(0.5);
        //        splitPane.setResizeWeight(1.0);

        //Firstly separate the two panes top and bottom into grid and score.:
        topPanel.setLayout(new GridBagLayout());
        bottomPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 0.8;
        c.gridx = 0;
        topPanel.add(topGrid.grid, c);
        bottomPanel.add(bottomGrid.grid, c);
        c.gridx = 1;
        topPanel.add(topScore, c);
        bottomPanel.add(bottomScore, c);

        //Now fix the window:
        window.setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        int top = 5, right = 0, bottom = 5, left = 0;
        Insets insets = new Insets(top, left, bottom, right);

        c.insets = insets;
        // we want the layout to stretch the components in both directions
        c.fill = GridBagConstraints.BOTH;
        // if the total X weight is 0, then it won't stretch horizontally.
        // It doesn't matter what the weight actually is, as long as it's not 0,
        // because the grid is only one component wide
        c.weightx = 1;

        // Vertical space is divided in proportion to the Y weights of the components
        c.weighty = 0.5;
        c.gridy = 0;
        window.add(topPanel, c);
        // It's fine to reuse the constraints object; add makes a copy.
        c.weighty = 0.5;
        c.gridy = 1;
        window.add(bottomPanel, c);
        windowF.add(window);
        windowF.setPreferredSize(new Dimension(500, 500));
        windowF.setSize(500, 500);
        windowF.setLocationRelativeTo(null);
        windowF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowF.setVisible(true);

    }
}
