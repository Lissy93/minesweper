package net.as93.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.as93.minesweeper.util.FectchScores;
import net.as93.minesweeper.util.JSONParser;
import net.as93.minesweeper.util.MinesweeperSettings;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 *  @author Alicia
 */
public class Scores extends Activity {

    public MinesweeperSettings settings;
    private Activity a = this;
    private CheckBox chkTimed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scores);

        chkTimed = (CheckBox)findViewById(R.id.chkTimedScores);

        Button btnEasy = (Button)findViewById(R.id.btnEasy);
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = a.getIntent();
                if(chkTimed.isChecked()){
                    intent.putExtra("timed", "true");}
                else{
                    intent.putExtra("timed", "false");
                }
                intent.putExtra("mode", "easy");
                a.finish();
                a.startActivity(intent);
            }
        });
        Button btnMedium = (Button)findViewById(R.id.btnMedium);
        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = a.getIntent();
                if(chkTimed.isChecked()){
                    intent.putExtra("timed", "true");}
                else{
                    intent.putExtra("timed", "false");
                }
                intent.putExtra("mode","medium");
                a.finish();
                a.startActivity(intent);
            }
        });
        Button btnHard = (Button)findViewById(R.id.btnHard);
        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = a.getIntent();
                if(chkTimed.isChecked()){
                    intent.putExtra("timed", "true");}
                else{
                    intent.putExtra("timed", "false");
                }
                intent.putExtra("mode","hard");
                a.finish();
                a.startActivity(intent);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Intent myIntent = getIntent(); // gets the previously created intent

        String timed = null;
        String mode  = null;
        boolean isExtras = false;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("timed") && extras.containsKey("mode")) {
                if(myIntent.getStringExtra("timed")!=null &&  myIntent.getStringExtra("mode")!=null){
                    timed = myIntent.getStringExtra("timed");
                    mode  = myIntent.getStringExtra("mode");
                    isExtras = true;
                }
            }
        }
        try {
            FectchScores fs = new FectchScores();
            JSONObject jsonResults;
            if(isExtras){  jsonResults = fs.execute(timed,mode).get(); }
            else{  jsonResults = fs.execute().get();  }
            drawScoreTable(jsonResults);
        } catch (Exception  e) {
            Toast.makeText(this, "There was an error fetching scores - no network access", Toast.LENGTH_LONG).show();
            System.out.println("Damn it Error: "+e);
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SettingsDialog sa =new SettingsDialog(this);
        sa.show();
        return true;
    }


    private void drawScoreTable(JSONObject jsonResults) throws JSONException {
        List<String[]> results = decodeTheJson(jsonResults);
        if(results.size()<1){ Toast.makeText(this, "No Scores in this Category", Toast.LENGTH_LONG).show(); }
        LinearLayout scoreContainer = (LinearLayout) this.findViewById(R.id.lytMainScores);
        scoreContainer.addView(addRow("Name","Time","Mode",true));
        for(int i = 0; i< results.size(); i++){
            String time = "";
            if(Integer.parseInt(results.get(i)[1]) / 60>0){
                time += Integer.parseInt(results.get(i)[1]) / 60 +":"; }
            time += Integer.parseInt(results.get(i)[1]) % 60;
            scoreContainer.addView(addRow(results.get(i)[0],time,((results.get(i)[2].equals("true")) ? "Timed " : "Untimed ") + results.get(i)[3] + " mode",false));
        }
    }

    private TextView addField(String txt){
        TextView txtField = new TextView(this);
        txtField.setTextColor(Color.parseColor("#F8F8F8"));
        txtField.setText(txt);
        txtField.setMinWidth(150);
        return txtField;
    }

    private TextView addField(String txt, boolean bold){
        TextView txtField = addField(txt);
        if(bold){txtField.setTypeface(null, Typeface.BOLD); }
        return txtField;
    }

    private LinearLayout addRow(String txt1, String txt2, String txt3, boolean bold){
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        row.addView(addField(txt1,bold));
        row.addView(addField(txt2,bold));
        row.addView(addField(txt3,bold));
        if(bold){row.setPadding(0,1,0,5);}
        return row;
    }

    private List<String[]>  decodeTheJson(JSONObject theJson) throws JSONException {
        List<String[]> results = new ArrayList<String[]>();
        JSONArray array = theJson.getJSONArray("highScores");
        for(int i = 0 ; i < array.length() ; i++){
            String[] row = new String[4];
            row[0] = array.getJSONObject(i).getString("usersname");
            row[1] = array.getJSONObject(i).getString("time");
            row[2] = array.getJSONObject(i).getString("timed");
            row[3] = array.getJSONObject(i).getString("mode");
            results.add(row);
        }
        return results;
    }


}
