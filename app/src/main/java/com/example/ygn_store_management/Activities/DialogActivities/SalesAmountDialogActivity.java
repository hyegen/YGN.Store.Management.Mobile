package com.example.ygn_store_management.Activities.DialogActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ygn_store_management.Models.Dtos.ItemSelectionDto;
import com.example.ygn_store_management.R;

public class SalesAmountDialogActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "extra_product";
    public static final String EXTRA_AMOUNT = "extra_amount";
    private ItemSelectionDto itemSelectionDto;
    private EditText etAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_amount_dialog);

        itemSelectionDto = (ItemSelectionDto) getIntent().getSerializableExtra(EXTRA_ITEM);

        TextView tvProductName = findViewById(R.id.tv_item_name);
        etAmount = findViewById(R.id.et_amount);
        Button btnOk = findViewById(R.id.btn_ok);
        Button btnCancel = findViewById(R.id.btn_cancel);

        if (itemSelectionDto != null) {
            tvProductName.setText(itemSelectionDto.getName());
        }

        btnOk.setOnClickListener(v -> {
            String amountText = etAmount.getText().toString();
            if (!amountText.isEmpty()) {
                int amount = Integer.parseInt(amountText);

                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_ITEM, itemSelectionDto);
                resultIntent.putExtra(EXTRA_AMOUNT, amount);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                etAmount.setError("Lütfen bir miktar girin");
            }
        });

        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}
