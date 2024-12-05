package com.example.ygn_store_management.Activities.SalesActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ygn_store_management.Activities.DialogActivities.SalesAmountDialogActivity;
import com.example.ygn_store_management.Adapters.ItemSelectionAdapter;
import com.example.ygn_store_management.Models.Dtos.ItemSelectionDto;
import com.example.ygn_store_management.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class SalesActivity extends AppCompatActivity {
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
    private void startQrScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("QR Kodu Tarayın");
        integrator.initiateScan();
    }
    private void fetchItemDetails(String qrCode) {

        //TO DO: Web Servisten qrCode'a göre ürün detayı çekilecek..
        ItemSelectionDto item = new ItemSelectionDto(qrCode, qrCode, 50.0);

        if(isItemCodeExists(qrCode)){
            Toast.makeText(this, qrCode+" "+"Kodlu Ürün Zaten Eklenmiş", Toast.LENGTH_SHORT).show();
        }else{
            openSalesAmountDialog(item);
        }
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
            if (requestCode == REQUEST_CODE_SALES_AMOUNT && resultCode == RESULT_OK) {
                ItemSelectionDto item = (ItemSelectionDto) data.getSerializableExtra(SalesAmountDialogActivity.EXTRA_ITEM);
                    int amount = data.getIntExtra(SalesAmountDialogActivity.EXTRA_AMOUNT, 0);
                    if (item != null) {
                        item.setAmount(amount);
                        itemList.add(item);
                        itemSelectionAdapter.notifyDataSetChanged();
                    }
            }
        }
    }
    private boolean isItemCodeExists(String itemCode) {
        for (ItemSelectionDto item : itemList) {
            if (item.getCode().equals(itemCode)) {
                return true;
            }
        }
        return false;
    }

    private void completeOrder() {
        Toast.makeText(this, "Sipariş Tamamlandı!", Toast.LENGTH_SHORT).show();
    }
}