package net.as93.minesweeper.util;

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alicia
 * Async task to call the fetch scores methods and return them
 */
public class FectchScores extends AsyncTask<String, Void, JSONObject> {


    @Override
    protected JSONObject doInBackground(String... inParams) {

        JSONParser jsonParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if(inParams.length!=0){
            params.add(new BasicNameValuePair("timed", inParams[0]));
            params.add(new BasicNameValuePair("mode", inParams[1]));
        }

        return jsonParser.makeHttpRequest("http://android.as93.net/minesweeper/getscore.php","GET",params);

    }
}
