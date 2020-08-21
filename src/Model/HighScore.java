package Model;

import netscape.javascript.JSObject;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class HighScore {
    private String winnerName;
    private String looserName;
    private boolean wasWinnerAI = false;
    private boolean wasAgainstAI;
    private int gameRound;
    private int winnersScore;
    private boolean isP1Winner;
    private Player player1;
    private Player player2;
    private boolean isClassNull = true;//lasy verification that it can call printToCsv.
    public boolean wasScoreWritten = false;

    /**
     * Only to be used for reading from file and using on the highscoreboard.
     * (readFromFile, get string)
     */
    public HighScore() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Throws exceptions if called before game is over.
     * Or if both players have zero in score/health.
     *
     * @param player1
     * @param player2
     * @param gameRound
     * @param p2isAI    If p2 was AI.
     */
    public HighScore(Player player1, Player player2, int gameRound, boolean p2isAI) {
        /*
        Calling get score 3 times is still O(N), O(3*N)=O(3*5), it is stupid, since it is bound to happen many times.
        But structure is generally bad, and the model needs its verifications.
        This should have been a private class that was called from an game class in model, but did not have time.
         */
        this.player1 = player1;
        this.player2 = player2;
        this.gameRound = gameRound;
        this.wasAgainstAI = p2isAI;
        if (!isGameOver()) return;
        isP1Winner = isPlayer1Winner();
        if (isP1Winner) {
            winnerName = player1.getName();
            looserName = player2.getName();
            winnersScore = getScore()[0];
        } else {//P2 was winner.
            if (wasAgainstAI) {
                wasWinnerAI = true;
                this.wasAgainstAI = false;
            }
            winnerName = player2.getName();
            looserName = player1.getName();
            winnersScore = getScore()[1];
        }
    }

    /**
     * The order of the csv columns.
     * @return
     */
    public List<String> getCsvColumns() {
        List<String> csvColumns = new LinkedList<>();
        csvColumns.add("Winner");
        csvColumns.add("Looser");
        csvColumns.add("Round");
        csvColumns.add("Remaining_Health");
        csvColumns.add("WasAgainstAI");
        csvColumns.add("wasWinnerAI");
        return getCsvColumns();
    }

    /**
     * Only used when appending to csv file.
     *
     * @return
     * @throws IllegalArgumentException If any of the two names contains comma, can catch and ask user to change name.
     *                                  But currently i do not allow name change after game start.
     */
    private String getAsString() throws IllegalArgumentException {
        if (winnerName.contains(",") || winnerName.contains(","))//Could possibly allow changes to names
            throw new IllegalArgumentException("Does not allow ',' in names");
        StringBuilder sb = new StringBuilder();
        sb.append(winnerName);
        sb.append(",");
        sb.append(looserName);
        sb.append(",");
        sb.append(gameRound);
        sb.append(",");
        sb.append(winnersScore);
        sb.append(",");
        sb.append(wasAgainstAI);
        sb.append(",");
        sb.append(wasWinnerAI);
        return sb.toString();
    }

    public void writeToFile() throws IllegalStateException {
        if (isClassNull) throw new IllegalStateException("Class was not probarly constructed");
        if (wasScoreWritten) throw new IllegalStateException("Cannot print score twice");

        String highscoreFileName = "data/high_score.csv";
        Path path = Paths.get(highscoreFileName);
        File file = path.toFile();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            StringBuilder sb = new StringBuilder();
            List<String> csvHead = getCsvColumns();
            for (String column : csvHead) {
                sb.append(column);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            try {
                appendToFile(file, sb.toString());
            } catch (IOException e) {//Ugly way to handle exceptions.
                System.err.println("Should never happen, fail to print highscore to newly created file: "
                        + e.getMessage());
            }
        }
        String highScore = getAsString();
        try {
            appendToFile(file, highScore);
        } catch (IOException e) {//Ugly way to handle exceptions.
            System.err.println("Should never happen appending following to highscorelist:\n" + highScore);
        }
    }

    /**
     * File needs to exist.
     * an '\n' will be appended after each text.
     *
     * @param file
     * @param text text to append to file.
     * @return true if success, else false
     * @throws IOException failing to create file writer.
     */
    private boolean appendToFile(File file, String text) throws IOException {
        if (!file.canWrite()) return false;
        BufferedWriter out = new BufferedWriter(
                new FileWriter(file, true));
        out.write(text);
        out.write("\n");
        out.close();
        return true;
    }

    /**
     * @param file File to read
     * @return All lines of the file provided, Null if any fail or unreadable file.
     */
    private List<String> readFile(File file) {
        if (!file.canRead()) {
            return null;
        }
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            return null;
        }
        List<String> lines = new LinkedList<>();
        String currLine;

        try {
            while (((currLine = bufferedReader.readLine()) != null)) lines.add(currLine);
        } catch (IOException e) {
            return null;
        }
        return lines;
    }


    /**
     * @return
     * @throws IllegalArgumentException If both loose at same time
     */
    public boolean isGameOver() throws IllegalArgumentException {
        int[] score = getScore();
        if (score[0] == 0 || score[1] == 0) {
            if (score[0] == score[1]) {
                throw new IllegalArgumentException("Both cant loose at same time.");
            } else return true;
        } else return false;
    }

    /**
     * @return [0]=p1Score, [1]=p2Score
     */
    private int[] getScore() {
        int[] score = new int[2];
        for (int i : player1.getFleetStatus()) {
            score[0] += i;
        }
        for (int i : player2.getFleetStatus()) {
            score[1] += i;
        }
        return score;
    }

    private boolean isPlayer1Winner() throws IllegalStateException {
        if (!isGameOver()) throw new IllegalStateException("Can't be winner since game not over.");
        int[] scores = getScore();
        if (scores[0] > scores[1]) {
            return true;
        } else return false;
    }

    public boolean isClassNull() {
        return isClassNull;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public String getLooserName() {
        return looserName;
    }

    public boolean wasWinnerAI() {
        return wasWinnerAI;
    }

    public boolean wasAgainstAI() {
        return wasAgainstAI;
    }

    public int getGameRound() {
        return gameRound;
    }

    public int getWinnersScore() {
        return winnersScore;
    }

    public boolean isP1Winner() {
        return isP1Winner;
    }
}
