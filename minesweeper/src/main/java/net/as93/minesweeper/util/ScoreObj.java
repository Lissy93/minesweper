package net.as93.minesweeper.util;

/**
 * @author Alicia
 * Class contains everything required to add score to dataabse
 */
public class ScoreObj {

    String usersName;   // The name of the user
    String gameMode;    // The game play mode (easy|medium|hard|custom)
    int time;           // Time to complete game
    boolean timed;      // Was game timed or un-timed?

    public ScoreObj(String usersName, String gameMode, int time, boolean timed){
        this.usersName = usersName;
        this.gameMode = gameMode;
        this.time = time;
        this.timed = timed;
    }

    public String getUsersName() {
        return usersName;
    }

    public String getGameMode() {
        return gameMode;
    }

    public int getTime() {
        return time;
    }

    public boolean isTimed() {
        return timed;
    }
}
