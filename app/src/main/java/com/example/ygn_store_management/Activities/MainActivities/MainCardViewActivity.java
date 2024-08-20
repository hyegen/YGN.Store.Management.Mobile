package com.example.ygn_store_management.Activities.MainActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ReportFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ygn_store_management.Activities.ReportActivities.GeneralReportActivities.ReportStockAmountActivity;
import com.example.ygn_store_management.Adapters.ViewPagerAdapter;
import com.example.ygn_store_management.MenuFragments.FragmentReports;
import com.example.ygn_store_management.R;
import com.google.android.material.tabs.TabLayout;


public class MainCardViewActivity extends AppCompatActivity {

    private CardView cardViewStockAmount;
    public String token;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Bundle testToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_card_view);
        findViews();
        initialize();
        getExtras();
        events();
    }
    private void getExtras() {
        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
    }
    private void findViews(){
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }
    private void events(){

    }
    private void initialize(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}