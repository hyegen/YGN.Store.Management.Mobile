package com.example.ygn_store_management.Activities.MainActivities;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ygn_store_management.R;

public class SettingActivity extends AppCompatActivity {
    private EditText ipAddressEditText;
    private Button saveButton;
    private Button returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findView();
        events();
        showSavedIpAddress();
    }
    private void findView(){
        ipAddressEditText = findViewById(R.id.ipAddressEditText);
        saveButton = findViewById(R.id.saveButton);
        returnButton = findViewById(R.id.returnButton);
    }
    private void events() {
        ipAddressEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    saveIpAddress();
                    Toast.makeText(SettingActivity.this, "Kaydetme Başarılı.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIpAddress();
                Toast.makeText(SettingActivity.this, "Kaydetme Başarılı.", Toast.LENGTH_SHORT).show();
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMainActivity();
            }
        });
    }
    private void saveIpAddress() {
        String ipAddress = ipAddressEditText.getText().toString();
        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS", MODE_PRIVATE).edit();
        editor.putString("ipAddress", ipAddress);
        editor.apply();
    }
    private void returnToMainActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void showSavedIpAddress() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        ipAddressEditText.setText(savedIpAddress);
    }
}
