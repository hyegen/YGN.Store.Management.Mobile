package com.example.ygn_store_management.Activities.MainActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ygn_store_management.Activities.ReportActivities.ReportCardViews.ReportCardViewActivity;
import com.example.ygn_store_management.Activities.ReportActivities.ReportStockAmountActivity;
import com.example.ygn_store_management.Activities.DialogActivities.ClientSelectionDialogActivity;
import com.example.ygn_store_management.R;

public class MainCardViewActivity extends AppCompatActivity {
    private CardView salesCardView;
    private CardView purchasingCardView;
    private CardView reportsCardView;
    private static final int CLIENT_SELECTION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_card_view);
        findViews();
        events();
    }
    private void findViews(){
        salesCardView=findViewById(R.id.salesCardView);
        purchasingCardView=findViewById(R.id.purchasingCardView);
        reportsCardView=findViewById(R.id.reportsCardView);
    }
    private void events(){
        purchasingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainCardViewActivity.this, ClientSelectionDialogActivity.class);
                intent.putExtra("IOCode",1);
                startActivity(intent);*/
                startClientSelectionActivity(1);
            }
        });
        salesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainCardViewActivity.this, ClientSelectionDialogActivity.class);
                intent.putExtra("IOCode",2);
                startActivity(intent);*/
                startClientSelectionActivity(2);
            }
        });
        reportsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCardViewActivity.this, ReportCardViewActivity.class);
                startActivity(intent);
            }
        });
    }
    private void startClientSelectionActivity(int ioCode) {
        Intent intent = new Intent(this, ClientSelectionDialogActivity.class);
        intent.putExtra("IOCode", ioCode);
        startActivityForResult(intent, CLIENT_SELECTION_REQUEST_CODE);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}