package net.as93.minesweeper.util;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alicia on 11/04/14.
 */
public class MinesweeperSettings {

    private static int gridX;                  // Number of squares X axis
    private static int gridY;                  // Number of squares Y axis
    private static int numMines;               // Number of mines in game
    private static boolean timed = true;       // Is game being timed?
    private static boolean sound = true;       // Is there sound effects?
    private static String gameMode;            // (easy|medium|hard|custom)
    private static String gameState;           // (notStarted|playing|died|won|gaveUp)
    private static MinesweeperTimings timer;   // The timer object
    private static List<Integer> minePositions;// The positions of mines
    private static LinearLayout mainGrid;      // The element selector for minesweeper grid
    private static TextView txtTimer;          // The timer element
    private static ImageButton btnSmiley;      // The smiley face at the top
    private static Minesweeper minesweeperObj; // The class containing common minesweeper functions



    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public boolean isTimed() {
        return timed;
    }

    public void setTimed(boolean timed) {
        this.timed = timed;
    }

    public static boolean isSound() {
        return sound;
    }

    public static void setSound(boolean sound) {
        MinesweeperSettings.sound = sound;
    }

    public List<Integer> getMinePositions() {
        return minePositions;
    }

    public void setMinePositions(List<Integer> minePositions) {
        this.minePositions = minePositions;
    }

    public LinearLayout getMainGrid() {
        return mainGrid;
    }

    public void setMainGrid(LinearLayout mainGrid) {
        this.mainGrid = mainGrid;
    }

    public TextView getTxtTimer() {
        return txtTimer;
    }

    public void setTxtTimer(TextView txtTimer) {
        this.txtTimer = txtTimer;
    }

    public int getNumMines() {
        return numMines;
    }

    public void setNumMines(int numMines) {
        this.numMines = numMines;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public MinesweeperTimings getTimer() {
        return timer;
    }

    public void setTimer(MinesweeperTimings timer) {
        this.timer = timer;
    }

    public static ImageButton getBtnSmiley() {
        return btnSmiley;
    }

    public static void setBtnSmiley(ImageButton btnSmiley) {
        MinesweeperSettings.btnSmiley = btnSmiley;
    }

    public static String getGameMode() {
        return gameMode;
    }

    public static void setGameMode(String gameMode) {
        MinesweeperSettings.gameMode = gameMode;
    }

    public static Minesweeper getMinesweeperObj() {
        return minesweeperObj;
    }

    public static void setMinesweeperObj(Minesweeper minesweeperObj) {
        MinesweeperSettings.minesweeperObj = minesweeperObj;
    }

    public int getNumSqures(){
        return this.gridX*this.gridY;
    }
}
