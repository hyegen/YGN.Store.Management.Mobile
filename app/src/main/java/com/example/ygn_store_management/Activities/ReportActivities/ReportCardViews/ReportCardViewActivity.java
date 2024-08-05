package com.example.ygn_store_management.Activities.ReportActivities.ReportCardViews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ygn_store_management.Activities.MainActivities.LoginActivity;
import com.example.ygn_store_management.Activities.MainActivities.MainCardViewActivity;
import com.example.ygn_store_management.Activities.ReportActivities.ReportStockAmountActivity;
import com.example.ygn_store_management.R;

public class ReportCardViewActivity extends AppCompatActivity {
    private CardView stockAmountCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports_card_view);
        findViews();
        events();
    }
    private void events() {
        stockAmountCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportCardViewActivity.this, ReportStockAmountActivity.class);
                startActivity(intent);
            }
        });
    }
    private void findViews() {
        stockAmountCardView=findViewById(R.id.cardViewStockAmount);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainCardViewActivity.class);
        startActivity(intent);
        finish();
    }
}