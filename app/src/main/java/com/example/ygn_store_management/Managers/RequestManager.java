package com.example.ygn_store_management.Managers;

import android.content.Context;
import android.widget.Toast;

import com.example.ygn_store_management.Activities.MainActivities.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RequestManager {

    public String makeGetRequest(String apiUrl, String jsonData) throws IOException, JSONException {


     /*   String apiRoute = "http://192.168.43.16:8181/api/login/{userName}/{password}";
        String user = username;
        String pass = password;

        apiRoute = apiRoute.replace("{userName}", URLEncoder.encode(user, "UTF-8"));
        apiRoute = apiRoute.replace("{password}", URLEncoder.encode(pass, "UTF-8"));

        URL url = new URL(url);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-Type", "application/json");

        urlConnection.connect();

        int statusCode = urlConnection.getResponseCode();

        if (statusCode == 200) {
            InputStream it = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader read = new InputStreamReader(it);
            BufferedReader buff = new BufferedReader(read);
            StringBuilder dta = new StringBuilder();
            String chunks;
            while ((chunks = buff.readLine()) != null) {
                dta.append(chunks);
            }
            return String.valueOf(statusCode);
        } else {
            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
*/
        return  null;
    }


}
