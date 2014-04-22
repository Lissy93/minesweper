package net.as93.minesweeper;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import net.as93.minesweeper.util.MinesweeperSettings;

/**
 * @author Alicia
 */
public class SettingsDialog extends Dialog {
    public Activity c;

    public SettingsDialog(Activity a) {
        super(a);
        this.c = a;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Game Settings");
        setContentView(R.layout.preferences);

        MinesweeperSettings settings = new MinesweeperSettings();

        CheckBox chkTimed = (CheckBox)findViewById(R.id.chkTimed);
        if(settings.isTimed()){ chkTimed.setChecked(true); }
        else{ chkTimed.setChecked(false);}
        chkTimed.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            MinesweeperSettings settings = new MinesweeperSettings();
            if(b){
                settings.setTimed(true);
                Toast.makeText(c, "Timed Mode: ON", Toast.LENGTH_SHORT).show();
            }
            else{
                settings.setTimed(false);
                Toast.makeText(c, "Timed Mode: OFF", Toast.LENGTH_SHORT).show();
            }
        }
    });

        CheckBox chkSound = (CheckBox)findViewById(R.id.chkSound);
        if(settings.isSound()){chkSound.setChecked(true);}
        else{chkSound.setChecked(false);}
        chkSound.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            MinesweeperSettings settings = new MinesweeperSettings();
            if(b){
                settings.setSound(true);
                Toast.makeText(c, "Sound Effects: ON", Toast.LENGTH_SHORT).show();
            }
            else{
                settings.setSound(false);
                Toast.makeText(c, "Sound Effects: OFF", Toast.LENGTH_SHORT).show();
            }
        }
    });

}



}
