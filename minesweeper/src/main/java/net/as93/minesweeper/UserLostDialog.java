package net.as93.minesweeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.as93.minesweeper.util.MinesweeperSettings;

public class UserLostDialog extends Dialog {

    public Activity c;
    public final static String GAME_MODE = "net.as93.minesweeper.MODE";

    public UserLostDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Game Over - You Hit a Mine");
        setContentView(R.layout.dialog_lost);

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


    }





}