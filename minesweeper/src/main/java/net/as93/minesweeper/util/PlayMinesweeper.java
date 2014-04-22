package net.as93.minesweeper.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.as93.minesweeper.R;

import java.util.List;

/**
 * @author Alicia
 * Contains all the high-level methods that are called by the main method class to set up the play activity
 */
public class PlayMinesweeper {

    Context c;
    Activity a;
    Minesweeper ms;
    MinesweeperSettings settings;
    MinesweeperTimings  timings;
    private MediaPlayer mediaButtonClick;
    private MediaPlayer mediaMineExplode;

    /**
     * Constructor setts the Context and Activity to class members,
     * Then creates new settings and timings objects also assigned to class members
     * @param c - The application Context
     * @param a - An instance of the PlayGame activity
     */
    public PlayMinesweeper(Context c, Activity a){
        this.c = c;
        this.a = a;
        this.settings = new MinesweeperSettings();
        this.timings = new MinesweeperTimings();
    }

    /**
     * Determines the number of rows and columns to go on the grid from the game mode
     * Then calls the setUpGame function to create grid
     * @param gameMode (easy|medium|hard)
     */
    public void createGrid(String gameMode){

        /* Declare grid height, width and number of mines */
        int gridX = 0;      // Length of minesweeper grid
        int gridY = 0;      // Height of minesweeper grid
        int numMines = 0;   // Number of mines within grid

        /* Determine height, width and number of mines */
        if(gameMode == null){ gameMode = "";} // to stop null pointer exc
        if(gameMode.equals("easy")){
            gridX = 6;
            gridY = 8;
            numMines = 6;
        }
        else if(gameMode.equals("medium")){
            gridX = 10;
            gridY = 14;
            numMines = 18;
        }
        else if(gameMode.equals("hard")){
            gridX = 14;
            gridY = 17;
            numMines = 24;
        }
        else{ //this should never happen
            gridX = 10;
            gridY = 14;
            numMines = 18;
        }


        setUpGame(gridX, gridY, numMines);
    }

    /**
     * If a custom game is being started, then this function will be called
     * @param gridX    - the number of columns
     * @param gridY    - the number of rows
     * @param numMines - the number of mines
     */
    public void createGrid(int gridX, int gridY, int numMines){
        setUpGame(gridX, gridY, numMines);
    }

    /**
     * Programmatically set up the screen
     * Including sound, timings, grid, header, action listeners... EVERYTHING!
     * @param gridX    - the number of columns
     * @param gridY    - the number of rows
     * @param numMines - the number of mines
     * @return the activity
     */
    private Activity setUpGame(int gridX, int gridY, int numMines){

        /* Set the settings */
        settings.setGridX(gridX);
        settings.setGridY(gridY);
        settings.setNumMines(numMines);


        /* Set up the sound effects */
        this.mediaButtonClick = MediaPlayer.create(a, R.raw.buttonpress);
        this.mediaMineExplode = MediaPlayer.create(a, R.raw.diesound);

        /* Set up the grid according to the above values */
        LinearLayout mainGrid = (LinearLayout) a.findViewById(R.id.lytMainGrid);


        /* The Minesweeper class*/
        ms = new Minesweeper(a,a.getApplicationContext(), gridX, gridY, mainGrid );
        settings.setMinesweeperObj(ms);


        /* Set up the header and components */
        TextView txtMineCount = (TextView)a.findViewById(R.id.txtMineCount);
        txtMineCount.setText(Integer.toString(numMines));
        TextView txtTimer = settings.getTxtTimer();
        if(!settings.isTimed()){
            txtTimer.setVisibility(View.GONE);
        }


        /* Add buttons to each square */
        for(int yCount = 0; yCount < gridY; yCount++){
            LinearLayout row = new LinearLayout(a);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1));

            for(int xCount = 0; xCount < gridX; xCount++){
                Button btnGameSquare = new Button(a);
                btnGameSquare.setSingleLine(false);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
                p.weight = 1;
                p.setMargins(2,2,2,2);
                btnGameSquare.setPadding(1, 1, 1, 1);
                btnGameSquare.setLayoutParams(p);
                btnGameSquare.setBackground(c.getResources().getDrawable(R.drawable.square));
                row.addView(btnGameSquare);
            }
            mainGrid.addView(row);
        }
    return a;
    }

    /**
     * Adds mines to their squares
     */
    public void addMines(){
        /* Get positions of mines */
        int numSquares = settings.getNumSqures();
        List<Integer> minePositions = ms.findMinePositions(numSquares, settings.getNumMines());
        settings.setMinePositions(minePositions);


        /* Add mines to each square */
        int squareNum = 0;
        LinearLayout mainGrid = settings.getMainGrid();
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            View row = mainGrid.getChildAt(i);
            for(int k = 0; k<((ViewGroup)row).getChildCount(); k++){
                squareNum++;
                Button b = (Button) ((ViewGroup)row).getChildAt(k);
                if(minePositions.contains(squareNum)){
                    b.setId(2);
                    int[] gridDimens = new int[2];
                    gridDimens[0] = settings.getGridX(); gridDimens[1] = settings.getGridY();
                    b.setTag(gridDimens);
                    b.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            Button b = (Button) view;
                            if(settings.isSound()){
                                mediaMineExplode.start();
                            }
                            b.setBackground(c.getResources().getDrawable(R.drawable.killer_mine));
                            ms.gameOver();
                        }
                    });

                    b.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            Button b = (Button) view;
                            b.setBackground(c.getResources().getDrawable(R.drawable.flag));
                            return true;
                        }
                    });
                }
            }
        }
    }


    /**
     * Calculates how many mines touching each square and add mine indicator accordingly
     */
    public void addMineIndicators(){
        int yCoordinate = 0;
        for (int i = 0; i < settings.getMainGrid().getChildCount(); i++) {
            View row = settings.getMainGrid().getChildAt(i);
            int xCoordinate = 0;
            for(int k = 0; k<((ViewGroup)row).getChildCount(); k++){
                Button b = (Button) ((ViewGroup)row).getChildAt(k);
                if(b.getId() !=2){
                    int[] tagCordinates = new int[2];
                    tagCordinates[0] = xCoordinate;
                    tagCordinates[1] = yCoordinate;
                    b.setTag(tagCordinates);
                    b.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            if(settings.isSound()){
                                mediaButtonClick.start(); // play cool sound
                            }
                            if(settings.getGameState().equals("notStarted")){
                                settings.getTimer().startTiming();
                                settings.setGameState("playing"); }
                            Button b = (Button) view;
                            int[] tagCoordinates = (int[])b.getTag();
                            String touchingMines = Integer.toString(ms.howManyMinesTouching(tagCoordinates[0], tagCoordinates[1]));

                            if(!touchingMines.equals("0")){
                                b.setText(touchingMines);
                                b.setTextColor(Color.parseColor(ms.findMarkerColour(touchingMines)));
                                b.setId(0);
                            }
                            else{ // if blank square
                                b.setId(1);
                                ms.clearSurroundingBlanks(tagCoordinates[0], tagCoordinates[1]);
                            }
                            b.setBackground(c.getResources().getDrawable(R.drawable.square_clicked));
                            b.setClickable(false);

                            settings.setMainGrid((LinearLayout) a.findViewById(R.id.lytMainGrid));
                            if(ms.checkIfWon()){
                               ms.won();
                            }

                        }
                    });

                    b.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            Button b = (Button) view;
                            b.setBackground(c.getResources().getDrawable(R.drawable.flag));
                            return true;
                        }
                    });
                }
                xCoordinate++;
            }
            yCoordinate++;
        }
    }

    /**
     * Adds the listener to the smiley (or sad) face at the top of the screen
     */
    public void addSmileyFaceListeners(){
        ImageButton smiley = settings.getBtnSmiley();
        smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton smiley = (ImageButton)a.findViewById(R.id.btnSmiley);
                if(settings.getGameState().equals("playing")){
                    smiley.setImageResource(R.drawable.face_giveup);
                    settings.setGameState("gaveUp");
                    ms.revealGrid();
                }
                else if(settings.getGameState().equals("died")||
                        settings.getGameState().equals("won")||
                        settings.getGameState().equals("gaveUp")){
                    Intent intent = a.getIntent();
                    a.finish();
                    a.startActivity(intent);
                }
            }
        });
    }






}
