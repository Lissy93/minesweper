package net.as93.minesweeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Alicia
 * The dialog for games with custom grid dimensions and numbers of mines
 */
public class CustomGameDialog extends Dialog {

    public Activity c;
    public final static String GAME_MODE = "net.as93.minesweeper.MODE";

    public CustomGameDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Select Custom Dimensions");
        setContentView(R.layout.custom_start);

        Button startGameButton = (Button)findViewById(R.id.cmdStartCstom);

        startGameButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText txtGridWidth = (EditText)findViewById(R.id.txtGridWidth);
                int numCols = 0;
                if(txtGridWidth.getText().toString().length()>0){
                    numCols = Integer.parseInt(txtGridWidth.getText().toString().trim());}
                EditText txtGridHeight = (EditText)findViewById(R.id.txtGridHeight);
                int numRows = 0;
                if(txtGridHeight.getText().toString().length()>0){
                    numRows = Integer.parseInt(txtGridHeight.getText().toString().trim());}
                EditText txtNumMines = (EditText)findViewById(R.id.txtNumMines);
                int numMines = 0;
                if(txtNumMines.getText().toString().length()>0){
                    numMines = Integer.parseInt(txtNumMines.getText().toString().trim());}

                if(validateForm()){
                startGame(numCols,numRows,numMines);}
            }
        });
    }


    /**
     * Checks each value in the textboxes and outputs an error message if the input is not okay
     * @return boolean true if everything is all cool, or false + Toast if there's a problem
     */
    private boolean validateForm(){
        EditText txtGridWidth = (EditText) findViewById(R.id.txtGridWidth);
        String ed_text1;
        ed_text1 = txtGridWidth.getText().toString().trim();
        if (TextUtils.isEmpty(ed_text1)|| ed_text1.length() == 0 || ed_text1.equals("")) {
            Toast.makeText(c.getBaseContext(),"You must enter a grid width",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.parseInt(ed_text1)>15 || Integer.parseInt(ed_text1)<6) {
            Toast.makeText(c.getBaseContext(),"Grid width must be between 6 and 15",Toast.LENGTH_SHORT).show();
            return false;
        }

        EditText txtGridHeight = (EditText) findViewById(R.id.txtGridHeight);
        String ed_text2 = txtGridHeight.getText().toString().trim();
        if (TextUtils.isEmpty(ed_text2)|| ed_text2.length() == 0 || ed_text2.equals("")) {
            Toast.makeText(c.getBaseContext(),"You must enter a grid height",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.parseInt(ed_text2)>20 || Integer.parseInt(ed_text2)<10) {
            Toast.makeText(c.getBaseContext(),"Grid height must be between 10 and 20",Toast.LENGTH_SHORT).show();
            return false;
        }


        EditText txtNumMines = (EditText) findViewById(R.id.txtNumMines);
        String ed_text3 = txtNumMines.getText().toString().trim();
        if (TextUtils.isEmpty(ed_text3)|| ed_text3.length() == 0 || ed_text3.equals("")) {
            Toast.makeText(c.getBaseContext(),"You must enter a number of mines",Toast.LENGTH_SHORT).show();
            return false;
        }
        int maxNumMines =  (int)(Math.round((Double.parseDouble(ed_text1) * Double.parseDouble(ed_text2))/2));
        if (Integer.parseInt(ed_text3)>maxNumMines || Integer.parseInt(ed_text3)<6) {
            Toast.makeText(c.getBaseContext(),"There must be between 6 and "+Integer.toString(maxNumMines)+" mines",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * Starts the PlayMinesweeper activity
     * Precondition: all values must have been checked to be valid
     * @param numCols - the number of colloumns
     * @param numRows - the number of rows
     * @param numMines - the number of mines
     */
    private void startGame(int numCols, int numRows, int numMines) {
        Context mContext = this.getContext();
        Intent intent = new Intent(mContext, PlayGame.class);
        Bundle b = new Bundle();
        b.putString(GAME_MODE,"custom");
        b.putInt("numCols", numCols);
        b.putInt("numRows", numRows);
        b.putInt("numMines", numMines);
        intent.putExtras(b);
        mContext.startActivity(intent);
    }


}