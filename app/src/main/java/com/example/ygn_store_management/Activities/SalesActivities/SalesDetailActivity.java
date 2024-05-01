package com.example.ygn_store_management.Activities.SalesActivities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ygn_store_management.Activities.MainActivities.LoginActivity;
import com.example.ygn_store_management.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SalesDetailActivity extends AppCompatActivity {
    private Spinner clientsSpinner;
    private ArrayList<String> clients = new ArrayList<>();
    private static String apiUrl;
    private static final String TAG = "SalesDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sales_detail);
        findViews();
        getSharedPreferences();
        initialize();
    }
    private void getSharedPreferences(){
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void initialize(){
        new fetchClients().execute();
    }
    private void findViews(){
        clientsSpinner=findViewById(R.id.clientsSpinner);
    }
    private class fetchClients extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String apiRoute = "/api/getAllUsers";
            try {
                URL url = new URL(apiUrl + apiRoute);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    String data = jsonArray.getString(i);
                    clients.add(data);
                }
            } catch (Exception e) {
                Log.e(TAG, "Hata: " + e.getMessage());
            }
            return clients.toString();
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(String jsonData) {
            try {
                ArrayList<String> users = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    users.add(jsonObject.getString("UserName"));
                }
                clientsSpinner.setAdapter(new ArrayAdapter<String>(SalesDetailActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, users));
            } catch (JSONException e) {
                Log.e(TAG, "Hata: " + e.getMessage());
            }
        }
    }
}