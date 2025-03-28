package com.ygn.ygn_store_management.Activities.MainActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import com.ygn.ygn_store_management.Interfaces.LoginService;
import com.ygn.ygn_store_management.Managers.ApiUtils;
import com.ygn.ygn_store_management.Managers.NetworkUtil;
import com.ygn.ygn_store_management.Models.LoginResponse;
import com.ygn.ygn_store_management.Models.User;
import com.ygn.ygn_store_management.R;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    //region members
    private Spinner usernameSpinner;
    private Button loginButton;
    private Button settingButton;

    private EditText edtPassword;
    private static String apiUrl;
    private ArrayList<String> users = new ArrayList<>();
    private static final String TAG = "LoginActivity";
    private static final int DELAY_MILLIS = 10000;
    //endregion

    //region overriden methods
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        usernameSpinner.setAdapter(null);

        loginButton.setOnClickListener(null);
        settingButton.setOnClickListener(null);
        edtPassword.setOnEditorActionListener(null);

        if (apiUrl!=null)
            apiUrl=null;

        if (users!=null)
        {
            users.clear();
            users=null;
        }

        this.finishAffinity();
    }
    //endregion

    //region private methods
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
    private void initialize() {
        GetAllUsers();
    }
    private void events() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLogin())
                    LoginByRetrofit();
            }
        });
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
    private void findViews() {
        loginButton = findViewById(R.id.loginButton);
        settingButton = findViewById(R.id.settingButton);
        edtPassword = findViewById(R.id.passwordEditText);
        usernameSpinner = findViewById(R.id.usernameSpinner);
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
        if(savedIpAddress.isEmpty() || savedIpAddress==null){
            Toast.makeText(this, "Ayarlar Sayfasına Url Giriniz.", Toast.LENGTH_SHORT).show();
        }
    }
    private void GetAllUsers(){
        try {
            Retrofit retrofit = ApiUtils.InitRequestWithoutToken(apiUrl);
            LoginService apiService = retrofit.create(LoginService.class);

            Call<List<User>> call = apiService.GetAllUsers();
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<String> usernames = new ArrayList<>();
                        for (User user : response.body()) {
                            usernames.add(user.getUserName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, usernames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        usernameSpinner.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Kullanıcılar Yüklenirken Hata.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.e("HATA",t.getMessage());
                }
            });

        } catch (Exception ex) {
           Log.e("HATA",ex.getMessage());
        }
    }
    private void LoginByRetrofit(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginService apiService = retrofit.create(LoginService.class);

        Call<LoginResponse> call = apiService.Login(usernameSpinner.getSelectedItem().toString(),edtPassword.getText().toString());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();

                    if (response.code()==200)
                    {
                        Intent intent=  new Intent(LoginActivity.this,MainCardViewActivity.class);
                        intent.putExtra("TOKEN",token);
                        startActivity(intent);

                        Toast.makeText(LoginActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Giriş Başarısız.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Şifre Yanlış.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("HATA",t.getMessage());
            }
        });
    }
    private boolean validateLogin(){
        if(edtPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Şifre Giriniz.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(usernameSpinner.getCount()==0){
            Toast.makeText(this, "Kullanıcılar Yüklenirken Hata.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //endregion
}