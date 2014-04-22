package net.as93.minesweeper.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.as93.minesweeper.CustomGameDialog;
import net.as93.minesweeper.R;
import net.as93.minesweeper.UserLostDialog;
import net.as93.minesweeper.UserWonDialog;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alicia
 * Class that contains all the methods that will be used throughout the gameplay
 */
public class Minesweeper {

    /* Class Variables */
    private Activity a;
    private Context context;
    private int gridX;
    private int gridY;
    private LinearLayout mainGrid;
    private MinesweeperSettings settings;


    /* Constructor */
    public Minesweeper(Activity a, Context context, int gridX, int gridY, LinearLayout mainGrid){
        this.a = a;
        this.context = context;
        this.gridX = gridX;
        this.gridY = gridY;
        this.mainGrid = mainGrid;
        this.settings = new MinesweeperSettings();
    }


    /* Returns the activity */
    public Activity getActivity(){ return a;}

    /**
     * Calculates how many mines are touching a square, given it's coordinates.
     * Precondition: the clicked square is not a mine.
     * @param xCordinate - the xCoordinate of the square
     * @param yCordinate - the yCoordinate of the square
     * @return numMines  - the number of mines touching square (0-8)
     */
    public int howManyMinesTouching(int xCordinate, int yCordinate){
        int mineCount = 0;

        /* Upper row */
        if(yCordinate<gridY-1){
            View topHorizontal = mainGrid.getChildAt(yCordinate+1);
            if(xCordinate<gridX-1){
                mineCount = incrementMineCount(mineCount, ((ViewGroup)topHorizontal).getChildAt(xCordinate+1)); // Upper left
            }
            mineCount = incrementMineCount(mineCount, ((ViewGroup)topHorizontal).getChildAt(xCordinate)); // Upper center
            if(xCordinate>0){
                mineCount = incrementMineCount(mineCount, ((ViewGroup)topHorizontal).getChildAt(xCordinate-1)); // Upper right
            }
        }

        /* Middle row */
        View middleHorizontal = mainGrid.getChildAt(yCordinate);
        if(xCordinate>0){
            mineCount = incrementMineCount(mineCount, ((ViewGroup)middleHorizontal).getChildAt(xCordinate-1)); // middle left
        }
        if(xCordinate<gridX-1){
            mineCount = incrementMineCount(mineCount, ((ViewGroup)middleHorizontal).getChildAt(xCordinate+1)); // middle right
        }

        /* Lower row */
        if(yCordinate>0){
            View bottomHorizontal = mainGrid.getChildAt(yCordinate-1);
            if(xCordinate<gridX-1){
                mineCount = incrementMineCount(mineCount, ((ViewGroup)bottomHorizontal).getChildAt(xCordinate+1)); // Lower left
            }

            mineCount = incrementMineCount(mineCount, ((ViewGroup)bottomHorizontal).getChildAt(xCordinate)); // Lower center

            if(xCordinate>0){
                mineCount = incrementMineCount(mineCount, ((ViewGroup)bottomHorizontal).getChildAt(xCordinate-1)); // Lower right
            }
        }


        return mineCount;
    }

    /**
     * When given a square Button object, this function checks weather it's a mine
     * If square is a mine, the mine count in incremented
     * @param mineCount - the current number of mines touching a square
     * @param square - the Button to check weather it's a mine or not
     * @return mineCount - the new  (possibly the same) value for mine count
     */
    private int incrementMineCount(int mineCount, View square){
        Button b = (Button)square;
        if(b.getId()==2){
            mineCount ++;
        }
        return mineCount;
    }

    /**
     * This method calculates the positions where the mines will be placed
     * PRECONDITION: numMines must be in range (6 < numMines < numSquares/2)
     * @param numSquares - the total number of squares in grid (x * y)
     * @param numMines   - the number of mines to be displayed on grid
     * @return minePositions in an ArrayList
     */
    public List<Integer> findMinePositions(int numSquares, int numMines){
        List<Integer> minePositions = new ArrayList<Integer>();
        for(int i = 0; i < numSquares; i++){
            minePositions.add(i);
        }
        Collections.shuffle(minePositions);
        minePositions = minePositions.subList(0, numMines);
        return minePositions;
    }

    /**
     * Function returns what the font colour for the revealed marker should be
     * Precondition: touchingMines should be between 1-8
     * @param touchingMines - the number that is displayed in the square
     * @return hex colour in String format
     */
    public String findMarkerColour(String touchingMines){
        if(touchingMines.equals("1"))       { return "#006871"; }
        else if(touchingMines.equals("2"))  { return "#506402"; }
        else if(touchingMines.equals("3"))  { return "#860000"; }
        else if(touchingMines.equals("4"))  { return "#690086"; }
        else if(touchingMines.equals("5"))  { return "#863600"; }
        else if(touchingMines.equals("6"))  { return "#128600"; }
        else if(touchingMines.equals("7"))  { return "#860086"; }
        else if(touchingMines.equals("8"))  { return "#FF0000"; }
        return "006871";
    }

    /**
     * When a blank square touching zero mines is revealed the surrounding squares up until one
     * touching a mine is revealed.
     * The function recursively calls itself for each surrounding square of the surrounding square
     * @param xCoordinate - the x coordinate of the current square
     * @param yCoordinate - the y coordinate of the current square
     */
    public void clearSurroundingBlanks(int xCoordinate, int yCoordinate){
        /* Upper row */
        if(yCoordinate>0){
            if(xCoordinate<gridX-1){ clearBlankSquare(xCoordinate+1,yCoordinate-1); } // Upper right
            clearBlankSquare(xCoordinate,yCoordinate-1); // Upper center
            if(xCoordinate>0){ clearBlankSquare(xCoordinate-1,yCoordinate-1); } // Upper left
        }

        /* Middle row */
        if(xCoordinate<gridX-1){clearBlankSquare(xCoordinate+1,yCoordinate); }// middle right
        if(xCoordinate>0){clearBlankSquare(xCoordinate-1,yCoordinate); }// middle left

        /* Lower row */
        if(yCoordinate<gridY-1){
            if(xCoordinate<gridX-1){ clearBlankSquare(xCoordinate+1,yCoordinate+1); }// Lower right
            clearBlankSquare(xCoordinate,yCoordinate+1); // Lower center
            if(xCoordinate>0){ clearBlankSquare(xCoordinate-1,yCoordinate+1); } // Lower left
        }
    }

    /**
     * This function is called when any square other than a mine need to be revealed
     * If the square is not touching any mines it will also call the clearSurroundingSquares function
     * If not, the number of mines touching will appear in the square (1-8)
     * @param xCoordinate - the x coordinate of the current square
     * @param yCoordinate - the y coordinate of the current square
     */
    private void clearBlankSquare(int xCoordinate, int yCoordinate){
        Button b = getButtonFromCoordinates(xCoordinate,yCoordinate);
        if(b.getId()!=0 && howManyMinesTouching(xCoordinate,yCoordinate)==0){
            b.setBackground(context.getResources().getDrawable(R.drawable.square_clicked));
            b.setClickable(false);
            b.setId(0);
            clearSurroundingBlanks(xCoordinate,yCoordinate);
        }
        else if(b.getId()!=0 && b.getId()!=2 && howManyMinesTouching(xCoordinate, yCoordinate)>0){
            b.setBackground(context.getResources().getDrawable(R.drawable.square_clicked));
            b.setClickable(false);
            b.setId(1);
            String touchingMines = Integer.toString(howManyMinesTouching(xCoordinate, yCoordinate));
            b.setText(touchingMines);
            b.setTextColor(Color.parseColor(findMarkerColour(touchingMines)));
        }
        b.setEnabled(false);

    }

    /**
     * This method finds the Button object for a square at given coordinates
     * @param xCoordinate - the x coordinate of the current square
     * @param yCoordinate - the y coordinate of the current square
     * @return Button object from View of given square
     */
    private Button getButtonFromCoordinates(int xCoordinate, int yCoordinate){
        ViewGroup row = (ViewGroup)mainGrid.getChildAt(yCoordinate);
        Button square = (Button)row.getChildAt(xCoordinate);
        return square;
    }

    /**
     * Clears the entire gird, indicating: clicked and un-clicked squares, the mines and the markers
     */
    public void revealGrid(){
        for(int rn = 0; rn< gridY; rn++){
            for(int cn = 0; cn< gridX; cn++){
                Button square = getButtonFromCoordinates(cn, rn);
                square.setClickable(false);
                if(square.getId()==2){
                    square.setBackground(context.getResources().getDrawable(R.drawable.mine));
                }
                else if(square.getId()!= 0 && square.getId()!= 1 && square.getId() != 2){
                    String touchingMines = Integer.toString(howManyMinesTouching(cn, rn));
                    if(!touchingMines.equals("0")){
                        square.setText(touchingMines);
                        square.setTextColor(Color.parseColor(findMarkerColour(touchingMines)));
                    }
                }
                square.setEnabled(false);
            }

        }
    }

    /**
     * Checks if there are any un-clicked non-mine squares left on the grid.
     * Called every time a square is revealed
     * @return boolean, true if won, false if still playing
     */
    public boolean checkIfWon(){
        boolean gameFinished = true;
        LinearLayout mainGrid = settings.getMainGrid();
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            View row = mainGrid.getChildAt(i);
            for(int k = 0; k<((ViewGroup)row).getChildCount(); k++){
                Button b = (Button) ((ViewGroup)row).getChildAt(k);
                if(b.getId()!=1 & b.getId()!=0&&b.getId()!=2){
                    gameFinished = false;
                }
            }
        }
        return gameFinished;
    }

    /**
     * Called if the player has won (no un-checked non-mine squares left)
     * Displays the won message with navigation options to other screens
     */
    public void won(){
      settings.getBtnSmiley().setBackground(context.getResources().getDrawable(R.drawable.face_winner));
        UserWonDialog uwd =new UserWonDialog(a);
        uwd.show();
    }

    /**
     * Called if the player has lost (hit a mine)
     * Displays the game over message and provides navigation options to further screens
     */
    public void gameOver(){
        settings.getBtnSmiley().setImageResource(R.drawable.face_dead);
        revealGrid();
        settings.setGameState("died");
        settings.getTimer().stopTiming();
        settings.getTxtTimer().setVisibility(View.GONE);
        UserLostDialog uld =new UserLostDialog(a);
        uld.show();
    }

    /**
     * Takes a Score object and calls Async task to save score, the displays suitable message
     * @param scoreObj - and object containing users name, time, game mode, timed mode
     * @return boolean true if score saved okay, or false otherwise
     * @throws IOException
     */
    public boolean saveScore(ScoreObj scoreObj) throws IOException {
        String usersname = scoreObj.getUsersName();
        String time = Integer.toString(scoreObj.getTime());
        String mode = scoreObj.getGameMode();
        String timed = ((scoreObj.isTimed()) ? "true" : "false");

        String myUri = "http://android.as93.net/minesweeper/setscore.php?usersname="+usersname+"&time="+time+"&timed="+timed+"&mode="+mode;
        try{
            Eventupdate eu = new Eventupdate();
            eu.execute(myUri);
//            Toast.makeText(a.getBaseContext(), "Page Contents: "+eu.getPageContents(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(a, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    } // end Minesweeper class
