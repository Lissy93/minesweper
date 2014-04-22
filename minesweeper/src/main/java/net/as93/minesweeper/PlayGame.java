package net.as93.minesweeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.as93.minesweeper.util.MinesweeperSettings;
import net.as93.minesweeper.util.MinesweeperTimings;
import net.as93.minesweeper.util.PlayMinesweeper;


/**
 * @author Alicia
 */
public class PlayGame extends Activity {

    public MinesweeperSettings settings;
    private Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_game);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        /* Get the game mode */
        Intent intent = getIntent();
        String gameMode = intent.getStringExtra(MainActivity.GAME_MODE); // (easy | medium | hard | custom)

        /* Assign the settings */
        settings = new MinesweeperSettings();
        settings.setMainGrid((LinearLayout) findViewById(R.id.lytMainGrid));
        settings.setTxtTimer((TextView) findViewById(R.id.txtTimer));
        settings.setBtnSmiley((ImageButton)findViewById(R.id.btnSmiley));
        settings.setGameMode(gameMode);
        settings.setGameState("notStarted");
        settings.setTimer(new MinesweeperTimings());

        PlayMinesweeper pm = new PlayMinesweeper(this.getApplicationContext(), this);

        if(gameMode.equals("custom")){
            Bundle b = getIntent().getExtras();
            pm.createGrid(b.getInt("numCols"),b.getInt("numRows"),b.getInt("numMines"));
        }
        else{
            pm.createGrid(gameMode);
        }

        pm.addMines();

        pm.addMineIndicators();

        pm.addSmileyFaceListeners();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_mainMenu){
            Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else{
            SettingsDialog sa =new SettingsDialog(this);
            sa.show();}
        return true;
    }



}
