package com.example.ygn_store_management.Managers;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestManager {
    public static String makePostRequest(String stringUrl, String payload,
                                         Context context) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        String line;
        StringBuffer jsonString = new StringBuffer();

        uc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        uc.setRequestMethod("POST");
        uc.setDoInput(true);
        uc.setInstanceFollowRedirects(false);
        uc.connect();
        OutputStreamWriter writer = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
        writer.write(payload);
        writer.close();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            while((line = br.readLine()) != null){
                jsonString.append(line);
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        uc.disconnect();
        return uc.getResponseMessage();
    }

}
