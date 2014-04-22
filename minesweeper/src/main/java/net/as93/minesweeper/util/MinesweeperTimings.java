package net.as93.minesweeper.util;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.appcompat.R;

/**
 * @author Alicia
 * Class contains everything for the Minesweeper timings
 */
public class MinesweeperTimings {

    /* Class Members */
    public long startTime = 0L;
    public  Handler customHandler = new Handler();
    public long gameTime = 0L;
    public long timeSwapBuff = 0L;
    public long updatedTime = 0L;
    private boolean gamePlaying;
    private long endTime;
    private long totalEndGameTime;
    MinesweeperSettings settings;

    /**
     * Gets the Minesweeper game settings
     */
    public MinesweeperTimings(){
        settings = new MinesweeperSettings();
    }

    /**
     * Starts the timer
     */
    public void startTiming(){
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        gamePlaying = true;
    }

    /**
     * Stops the timer      TODO: test this method
     */
    public void stopTiming(){
        gamePlaying = false;
        timeSwapBuff += startTime;
        customHandler.removeCallbacks(updateTimerThread);
        endTime = SystemClock.uptimeMillis();
        totalEndGameTime = endTime - startTime;
        startTime = 0;
    }

    /**
     * @return the current time spent playing game
     */
    public int getIntTime(){
        gameTime = SystemClock.uptimeMillis() - startTime;
        updatedTime = timeSwapBuff + gameTime;
        return (int) (updatedTime / 1000);
    }

    /**
     * The actual timer
     */
    public Runnable updateTimerThread = new Runnable() {
        public void run() {
            if(gamePlaying){
                gameTime = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + gameTime;
                int secs = (int) (updatedTime / 1000);
                if (secs % 30 == 0 && secs > 5) {
                    if(settings.isSound()){
                        MediaPlayer mediaTimeRunning = MediaPlayer.create(settings.getMinesweeperObj().getActivity(), net.as93.minesweeper.R.raw.timebeep);
                        mediaTimeRunning.start();
                    }
                }
                int mins = secs / 60;
                secs = secs % 60;
                settings.getTxtTimer().setText("" + mins + ":" + String.format("%02d", secs));
                customHandler.postDelayed(this, 0);
            }

        }
    };

}


