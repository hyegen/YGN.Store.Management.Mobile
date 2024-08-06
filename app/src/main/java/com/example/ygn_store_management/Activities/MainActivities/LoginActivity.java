package com.example.ygn_store_management.Activities.MainActivities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ygn_store_management.Managers.NetworkUtil;
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
    private ArrayList<String> users = new ArrayList<>();
    private static final String TAG = "LoginActivity";
    protected ProgressDialog pleaseWait;
    private static final int DELAY_MILLIS = 3000;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         if (checkNetwork()){
             getSharedPreferences();
             findViews();
             events();
             initialize();
         }
    }
    private boolean checkNetwork(){
        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "İnternet bağlantısı yok. Uygulama kapanacaktır.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishAffinity();
                }
            }, DELAY_MILLIS);
            return false;
        }else {
            return true;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
    private void initialize() {
        new fetchUsers().execute();
    }
    private void events() {
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
    private void findViews() {
        loginButton = findViewById(R.id.loginButton);
        settingButton = findViewById(R.id.settingButton);
        edtPassword = findViewById(R.id.passwordEditText);
        infoButton = findViewById(R.id.btnInfo);
        usernameSpinner = findViewById(R.id.usernameSpinner);
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void login() {
        String username = usernameSpinner.getSelectedItem().toString();
        String password = edtPassword.getText().toString();

        if(username.isEmpty()|| password.isEmpty()){
            Toast.makeText(this, "Kullanıcılar Yüklenemedi, İnternet Bağlantınızı Kontrol ediniz. ", Toast.LENGTH_SHORT).show();
        }
        else {
            new LoginTask(username, password).execute();
        }
    }
    private class fetchUsers extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pleaseWait = ProgressDialog.show(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.loading), LoginActivity.this.getResources().getString(R.string.please_wait));
        }
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
                Log.e(TAG, "Hata: " + e.getMessage());
            }
            return users.toString();
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(String jsonData) {
            if (pleaseWait != null) {
                pleaseWait.dismiss();
            }
            try {
                ArrayList<String> users = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    users.add(jsonObject.getString("UserName"));
                }
                usernameSpinner.setAdapter(new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, users));
            } catch (JSONException e) {
                Log.e(TAG, "Hata: " + e.getMessage());
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
            try {
                String apiRoute = apiUrl + "/api/login/{userName}/{password}";
                String user = username;
                String pass = password;

                apiRoute = apiRoute.replace("{userName}", URLEncoder.encode(user, "UTF-8"));
                apiRoute = apiRoute.replace("{password}", URLEncoder.encode(pass, "UTF-8"));

                URL url = new URL(apiRoute);

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
                    Toast.makeText(LoginActivity.this, "Hata !", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String success) {
            if (success != null && success.equals("200")) {
                Intent intent = new Intent(LoginActivity.this, MainCardViewActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "Giriş Başarılı.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Giriş Başarısız.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
