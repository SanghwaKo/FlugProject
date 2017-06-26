package recoding.hackathon.com.flugproject;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by KSH on 2017-06-18.
 */

public class HttpHandler {
    public HttpHandler() {

    }

    public String makeServiceCall(String reqUrl){
        String response = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(reqUrl);
            if(Debug.DEBUG){
                Log.d(Constant.APP_TAG, "url " + reqUrl);
            }

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = conn.getInputStream();
            response = convertStreamToString(new InputStreamReader(in, "UTF-8"));
        } catch (MalformedURLException e) {
            Log.e(Constant.APP_TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(Constant.APP_TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(Constant.APP_TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(Constant.APP_TAG, "Exception: " + e.getMessage());
        }finally {
            if(conn != null){
                conn.disconnect();
            }
        }
        return response;
    }

    private String convertStreamToString(InputStreamReader inputStreamReader) {
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e(Constant.APP_TAG, "IOException: " + e.getMessage());
        } finally {
            try {
                inputStreamReader.close();
            } catch (IOException e){
                Log.e(Constant.APP_TAG, "IOException: " + e.getMessage());
            }
        }
        return sb.toString();
    }
}
