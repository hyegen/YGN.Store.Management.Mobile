package com.example.ygn_store_management.Activities.MainActivities;

import static com.example.ygn_store_management.Managers.RequestManager.makePostRequest;
import static java.lang.System.out;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ygn_store_management.Activities.ReportActivities.ReportStockAmountActivity;
import com.example.ygn_store_management.Adapters.UserAdapter;
import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.Models.User;
import com.example.ygn_store_management.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private Spinner usernameSpinner;
    private Button loginButton;
    private Button settingButton;
    private Button infoButton;
    private EditText edtPassword;
    private static String apiUrl;
    private ArrayList<String> users= new ArrayList<>();;
    private static final String TAG = "LoginActivity";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSharedPreferences();
        findViews();
        events();
        initialize();
    }
    private void initialize(){

        new fetchDataTask().execute();
    }
    private void events(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    login();
                    return true;
                }
                return false;
            }
        });
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });
    }
    private void findViews(){
        loginButton = findViewById(R.id.loginButton);
        settingButton = findViewById(R.id.settingButton);
        edtPassword = findViewById(R.id.passwordEditText);
        infoButton=findViewById(R.id.btnInfo);
        usernameSpinner = findViewById(R.id.usernameSpinner);
    }
    private void login(){

        String username = usernameSpinner.getSelectedItem().toString();
        String password = edtPassword.getText().toString();
        LoginTask loginTask = new LoginTask(username, password);
        loginTask.execute();

       /* String selectedUser = usernameSpinner.getSelectedItem().toString();

        if (selectedUser.equals("BAHAR")) {
            if (isPasswordValid(edtPassword.getText().toString())) {
                Intent intent = new Intent(LoginActivity.this, ReportStockAmountActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Şifre yanlış", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Geçersiz kullanıcı", Toast.LENGTH_SHORT).show();
        }*/
    }
    private void getSharedPreferences(){
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private class fetchDataTask extends AsyncTask<Void, Void, String> {
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
                    users.add(data);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching data: " + e.getMessage());
            }
            return users.toString();
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
                usernameSpinner.setAdapter(new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, users));
            }catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON: " + e.getMessage());
            }
        }
    }
    private class LoginTask extends AsyncTask<Void, Void, String> {
        private String username;
        private String password;

        public LoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String apiRoute = "/api/login";

            try {
                String response = makePostRequest(apiUrl+apiRoute,
                        "{\"userName\":\""+username+"\", \"password\":\""+password+"\"}", getApplicationContext());
                return response;
            } catch (IOException ex) {
                ex.printStackTrace();
                return "";
            }
        }
        @Override
        protected void onPostExecute(String success) {
            if (success.equals("OK")) {
                Intent intent = new Intent(LoginActivity.this, ReportStockAmountActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
