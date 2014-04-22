package net.as93.minesweeper.util;

import android.os.AsyncTask;
import android.widget.Toast;

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


/**
 * @author Alicia
 * Class to subit URL - used for updating the secore table
 */
public class Eventupdate extends AsyncTask<String, Void, Void> {

    String pageContents;

    @Override
    protected Void doInBackground(String... url) {
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(url[0]);
            HttpResponse response = httpClient.execute(httpGet,localContext);
            String result = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = null;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }

            pageContents = result;

        } catch (ClientProtocolException e) {
            System.out.println("Error 1 : "+e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error 2 : "+e);
            e.printStackTrace();
        }

        return null;
    }

    public String getPageContents() {
        return pageContents;
    }
}
