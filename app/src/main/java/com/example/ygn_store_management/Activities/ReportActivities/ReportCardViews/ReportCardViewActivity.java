package com.example.ygn_store_management.Activities.ReportActivities.ReportCardViews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ygn_store_management.Activities.ReportActivities.GeneralReportActivities.ReportGeneralSalesAndPurchasing;
import com.example.ygn_store_management.Activities.ReportActivities.PurchasingReports.ReportPurchasingDetailByClientDetail;
import com.example.ygn_store_management.Activities.ReportActivities.SalesReports.ReportSalesDetailByClientDetail;
import com.example.ygn_store_management.Activities.ReportActivities.GeneralReportActivities.ReportStockAmountActivity;
import com.example.ygn_store_management.R;

public class ReportCardViewActivity extends AppCompatActivity {
    private CardView stockAmountCardView;
    private CardView salesDetailByClientCardView;
    private CardView orderQueryCardView;
    private CardView purchasingDetailByClientCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports_card_view);
        findViews();
        events();
    }
    private void findViews() {
        stockAmountCardView=findViewById(R.id.cardViewStockAmount);
        salesDetailByClientCardView=findViewById(R.id.cardViewSalesDetailByClientDetail);
        orderQueryCardView=findViewById(R.id.cardViewOrderQuery);
        purchasingDetailByClientCardView=findViewById(R.id.cardViewPurchasingDetailByClientDetail);
    }
    private void events() {
        stockAmountCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportCardViewActivity.this, ReportStockAmountActivity.class);
                startActivity(intent);
            }
        });
        salesDetailByClientCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportCardViewActivity.this, ReportSalesDetailByClientDetail.class);
                startActivity(intent);
            }
        });
        orderQueryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportCardViewActivity.this, ReportGeneralSalesAndPurchasing.class);
                startActivity(intent);
            }
        });
        purchasingDetailByClientCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportCardViewActivity.this, ReportPurchasingDetailByClientDetail.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(this, MainCardViewActivity.class);
//        startActivity(intent);
        this.finish();
    }
}