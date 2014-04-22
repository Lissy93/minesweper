package net.as93.minesweeper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.as93.minesweeper.util.MinesweeperSettings;

/**
 * @author Alicia
 */
public class MainActivity extends ActionBarActivity {

    public final static String GAME_MODE = "net.as93.minesweeper.MODE";
    public final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }




    public void startEasyMode(View view){
        startGame("easy");
    }
    public void startMediumMode(View view){
        startGame("medium");
    }
    public void startHardMode(View view){
        startGame("hard");
    }
    public void startCustomMode(View view){
        CustomGameDialog cg =new CustomGameDialog(this);
        cg.show();
    }

    private void startGame(String mode) {
        Intent intent = new Intent(this, PlayGame.class);
        intent.putExtra(GAME_MODE, mode);
        startActivity(intent);
    }

    public void goToScores(View view){
        Intent intent = new Intent(this, Scores.class);
        startActivity(intent);
    }



}
