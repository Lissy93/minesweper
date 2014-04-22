package net.as93.minesweeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.as93.minesweeper.util.Minesweeper;
import net.as93.minesweeper.util.MinesweeperSettings;
import net.as93.minesweeper.util.ScoreObj;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserWonDialog extends Dialog {

    public Activity c;
    public final static String GAME_MODE = "net.as93.minesweeper.MODE";

    public UserWonDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Congratulations - You Won!");
        setContentView(R.layout.dialog_win);

        Button cmdPlayAgain = (Button)findViewById(R.id.cmdPlayAgain);
        cmdPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinesweeperSettings settings = new MinesweeperSettings();
                Intent intent = new Intent(c, PlayGame.class);
                Bundle b = new Bundle();
                b.putString(GAME_MODE,settings.getGameMode());
                b.putInt("numCols", settings.getGridX());
                b.putInt("numRows", settings.getGridY());
                b.putInt("numMines", settings.getNumMines());
                b.putBoolean("isTimed",settings.isTimed());
                intent.putExtras(b);
                c.startActivity(intent);
            }
        });

        Button cmdMainMenu = (Button)findViewById(R.id.cmdMainMenu);
        cmdMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, MainActivity.class);
                c.startActivity(intent);
            }
        });


        Button cmdSaveScore = (Button)findViewById(R.id.cmdScores);
        cmdSaveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinesweeperSettings settings = new MinesweeperSettings();
                Intent intent = new Intent(c, Scores.class);
                Bundle b = new Bundle();

                if(settings.isTimed()){
                    intent.putExtra("timed", "true");}
                else{
                    intent.putExtra("timed", "false");
                }
                intent.putExtra("mode",settings.getGameMode());


                EditText txtName = (EditText) findViewById(R.id.txtName);
                String usersName = txtName.getText().toString().trim();
                b.putString("mode",settings.getGameMode());
                b.putInt("time",settings.getTimer().getIntTime());
                b.putString("usersName",usersName);
                b.putBoolean("timed",settings.isTimed());

                if(!usersName.equals("")&&!usersName.equals("Enter Your Name")&&usersName.length()<20){
                    ScoreObj scoreObj = new ScoreObj(usersName, settings.getGameMode(), settings.getTimer().getIntTime(),settings.isTimed());

                    try {
                        if(settings.getMinesweeperObj().saveScore(scoreObj)){
                            Toast.makeText(c.getBaseContext(), "High Score Saved", Toast.LENGTH_SHORT).show();

                            c.finish();
                            c.startActivity(intent);}

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    c.startActivity(intent);
                }
                else{
                    Toast.makeText(c.getBaseContext(), "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }






}