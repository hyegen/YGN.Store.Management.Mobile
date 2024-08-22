package com.example.ygn_store_management.Activities.ReportActivities.GeneralReportActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ygn_store_management.Interfaces.ReportItemInformationService;
import com.example.ygn_store_management.Managers.ApiUtils;
import com.example.ygn_store_management.Managers.ImageUtils;
import com.example.ygn_store_management.Models.ReportViews.ItemInformation;
import com.example.ygn_store_management.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportItemInformationActivity extends AppCompatActivity {
    private ImageView itemImageView;
    private TextView txtItemId;
    private TextView txtItemCode;
    private TextView txtItemName;
    private TextView txtBrand;
    private TextView txtStockAmount;
    private EditText edtSearchItemCode;
    private Button btnSearchItem;
    private String token;
    private static String apiUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_item_information);
        findViews();
        getSharedPreferences();
        getExtras();
        events();
    }
    private void getExtras() {
        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void findViews(){
        itemImageView = findViewById(R.id.itemImageView);
        txtItemId = findViewById(R.id.txtItemId);
        txtItemCode = findViewById(R.id.txtItemCode);
        txtItemName = findViewById(R.id.txtItemName);
        txtBrand = findViewById(R.id.txtBrand);
        txtStockAmount = findViewById(R.id.txtStockAmount);
        edtSearchItemCode = findViewById(R.id.edtSearchItemCode);
        btnSearchItem = findViewById(R.id.btnSearchItem);
    }
    private void events()
    {
        btnSearchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateGetData())
                    GetData();
            }
        });
    }
    private void GetData(){

        try {
            Retrofit retrofit = ApiUtils.InitRequestWithToken(apiUrl,token);
            ReportItemInformationService apiService = retrofit.create(ReportItemInformationService.class);

            Call<ItemInformation> call = apiService.MobGetItemInformationByItemCode(token,edtSearchItemCode.getText().toString());

            call.enqueue(new Callback<ItemInformation>() {
                @Override
                public void onResponse(Call<ItemInformation> call, Response<ItemInformation> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ItemInformation item = response.body();

                        txtItemId.setText(Integer.toString(item.getId()));
                        txtItemCode.setText(item.getItemCode());
                        txtItemName.setText(item.getItemName());
                        txtBrand.setText(item.getBrand());
                        txtStockAmount.setText(Integer.toString(item.getStockAmount()));

                        if (item.getItemImage()!=null){
                            byte[] imageBytes = Base64.decode(item.getItemImage(), Base64.DEFAULT);
                            ImageUtils.setImageFromBytes(imageBytes, itemImageView);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemInformation> call, Throwable t) {
                    Log.e("HATA",t.getMessage());
                }
            });
        }
        catch (Exception ex){
            Log.e("HATA",ex.getMessage());
        }

    }
    private boolean validateGetData(){
        if(edtSearchItemCode.getText().toString().isEmpty()){
            Toast.makeText(ReportItemInformationActivity.this, "Ürün Kodu Giriniz.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }
}