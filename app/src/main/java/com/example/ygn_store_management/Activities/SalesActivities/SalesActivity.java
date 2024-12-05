package com.example.ygn_store_management.Activities.SalesActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ygn_store_management.Activities.DialogActivities.SalesAmountDialogActivity;
import com.example.ygn_store_management.Adapters.ItemSelectionAdapter;
import com.example.ygn_store_management.Interfaces.ItemSelectionService;
import com.example.ygn_store_management.Managers.ApiUtils;
import com.example.ygn_store_management.Models.Dtos.ItemSelectionDto;
import com.example.ygn_store_management.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SalesActivity extends AppCompatActivity {
    private static String apiUrl;
    private EditText etSearch;
    private ImageView ivCamera;
    private RecyclerView recyclerViewItems;
    private Button btnCompleteOrder;
    private List<ItemSelectionDto> itemList = new ArrayList<>();
    private ItemSelectionAdapter itemSelectionAdapter;
    private static final int REQUEST_CODE_SALES_AMOUNT = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        getSharedPreferences();
        etSearch = findViewById(R.id.et_search);
        ivCamera = findViewById(R.id.iv_camera);
        recyclerViewItems = findViewById(R.id.rv_products);
        btnCompleteOrder = findViewById(R.id.btn_complete_order);

        itemSelectionAdapter = new ItemSelectionAdapter(itemList);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems
                .setAdapter(itemSelectionAdapter);

        ivCamera.setOnClickListener(v -> {
            startQrScanner();
        });

        btnCompleteOrder.setOnClickListener(v -> {
            completeOrder();
        });
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void startQrScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("QR Kodu Tarayın");
        integrator.initiateScan();
    }
    private void fetchItemDetails(String qrCode) {
        
            Retrofit retrofit = ApiUtils.InitRequestWithoutToken(apiUrl);
            ItemSelectionService apiService = retrofit.create(ItemSelectionService.class);

            Call<ItemSelectionDto> call = apiService.GetItemByItemCode(qrCode);
            call.enqueue(new Callback<ItemSelectionDto>() {
                @Override
                public void onResponse(Call<ItemSelectionDto> call, Response<ItemSelectionDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ItemSelectionDto item = response.body();
                        if (item != null) {
                            openSalesAmountDialog(item);
                        }
                    }
                    else {
                        Toast.makeText(SalesActivity.this, "Sipariş Numarası veya Bilgilerinizi Kontrol Ediniz.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ItemSelectionDto> call, Throwable t) {
                    Toast.makeText(SalesActivity.this, "Sipariş Numarası veya Bilgilerinizi Kontrol Ediniz.", Toast.LENGTH_SHORT).show();
                }
            });
    }
    private void openSalesAmountDialog(ItemSelectionDto item) {
        Intent intent = new Intent(this, SalesAmountDialogActivity.class);
        intent.putExtra(SalesAmountDialogActivity.EXTRA_ITEM, item);
        startActivityForResult(intent, REQUEST_CODE_SALES_AMOUNT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String qrCode = result.getContents();
                fetchItemDetails(qrCode);
            } else {
                Toast.makeText(this, "QR kod okunamadı", Toast.LENGTH_SHORT).show();
            }
        } else {
            ItemSelectionDto product = (ItemSelectionDto) data.getSerializableExtra(SalesAmountDialogActivity.EXTRA_ITEM);
            if (requestCode == REQUEST_CODE_SALES_AMOUNT && resultCode == RESULT_OK) {

                if(!isItemCodeExists(product.getItemCode())){
                    int amount = data.getIntExtra(SalesAmountDialogActivity.EXTRA_AMOUNT, 0);
                    if (product != null) {
                        product.setItemAmount(amount);
                        itemList.add(product);
                        itemSelectionAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    Toast.makeText(this, product.getItemCode()+" "+"Kodlu Ürün Zaten Eklenmiş", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Aynı Ürün Zaten Eklenmiş. Başka ürün ekleyiniz.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private boolean isItemCodeExists(String itemCode) {
        for (ItemSelectionDto item : itemList) {
            if (item.getItemCode().equals(itemCode)) {
                return true;
            }
        }
        return false;
    }

    private void completeOrder() {
        Toast.makeText(this, "Sipariş Tamamlandı!", Toast.LENGTH_SHORT).show();
    }
}