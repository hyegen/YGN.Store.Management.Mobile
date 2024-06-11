package com.example.ygn_store_management.Activities.DialogActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ygn_store_management.Adapters.ClientAdapter;
import com.example.ygn_store_management.Models.Client;
import com.example.ygn_store_management.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ClientSelectionDialogActivity extends AppCompatActivity {
    private Integer IOCode;
    private ArrayList<String> clients = new ArrayList<>();
    private static String apiUrl;
    private static final String TAG = "SalesDetailActivity";
    private ListView clientsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_selection_dialog);
        getExtras();
        findViews();
        getSharedPreferences();
        initialize();
        events();
    }
    private void getExtras() {
        IOCode = getIntent().getIntExtra("IOCode",-1);
    }
    private void events() {
        clientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client client = (Client) parent.getItemAtPosition(position);

                int itemId = client.getClientId();
                String itemDescription = client.getClientCodeAndNameAndSurname();

                Intent intent = new Intent(ClientSelectionDialogActivity.this, ProductSelectionDialogActivity.class);
                intent.putExtra("selectedClientId", itemId);
                intent.putExtra("ClientCodeAndNameAndSurname", itemDescription);
                intent.putExtra("IOCode", IOCode);

                startActivity(intent);
            }
        });
    }
    private void startProductSelectionActivity() {
        Intent intent = new Intent(this, ProductSelectionDialogActivity.class);
        intent.putExtra("IOCode", IOCode);
        startActivityForResult(intent, 1); // Request code 1 for product selection
    }
 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CLIENT_SELECTION_REQUEST_CODE) {
                int ioCode = data.getIntExtra("IOCode", -1);
                String resultValue = data.getStringExtra("result_key");
            }
        }
    }*/
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void initialize() {
        new fetchClients().execute();
    }
    private void findViews() {
        clientsListView = findViewById(R.id.clientSelectionListViewTest);
    }
    private class fetchClients extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String apiRoute = "/api/getAllClientsByCodeAndNameAndSurname";
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
                ArrayList<Client> clients = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Client client = new Client();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    client.ClientId = Integer.valueOf(jsonObject.getString("ClientId"));
                    client.ClientCodeAndNameAndSurname = jsonObject.getString("ClientCodeAndNameAndSurname");
                    clients.add(client);
                }
                ClientAdapter adapter = new ClientAdapter(ClientSelectionDialogActivity.this, R.layout.adapter_client_selection, clients);
                clientsListView.setAdapter(adapter);
                clientsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                clientsListView.setItemsCanFocus(false);

            } catch (JSONException e) {
                Log.e(TAG, "Hata: " + e.getMessage());
            }
        }
    }
}