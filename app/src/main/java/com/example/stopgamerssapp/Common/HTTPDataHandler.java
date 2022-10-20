package com.example.stopgamerssapp.Common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPDataHandler {
    static String stream = null;

    public HTTPDataHandler() {
    }

    public String GetHTTPData(String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null){
                    stringBuilder.append(line);
                }
                stream = stringBuilder.toString();
                urlConnection.disconnect();
            }
        }
        catch (Exception ex){

        }
    }
}
