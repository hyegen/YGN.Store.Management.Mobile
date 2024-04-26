package com.example.ygn_store_management.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ygn_store_management.Activities.MainActivity;
import com.example.ygn_store_management.Activities.SettingActivity;
import com.example.ygn_store_management.R;

public class LoginActivity extends AppCompatActivity {

    private Spinner usernameSpinner;
    private Button loginButton;
    private Button settingButton;
    private EditText edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final String[] users = {"BAHAR", "User1", "User2"};

        usernameSpinner = findViewById(R.id.usernameSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, users);
        usernameSpinner.setAdapter(adapter);

        loginButton = findViewById(R.id.loginButton);
        settingButton = findViewById(R.id.settingButton);
        edtPassword = findViewById(R.id.passwordEditText);


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
    }
    private void login(){
        String selectedUser = usernameSpinner.getSelectedItem().toString();

        if (selectedUser.equals("BAHAR")) {
            if (isPasswordValid(edtPassword.getText().toString())) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Şifre yanlış", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Geçersiz kullanıcı", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isPasswordValid(String password) {
        if (password.equals("1")) {
            return true;
        } else {
            return false;
        }
    }
}
