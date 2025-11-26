package com.ygn.ygn_store_management.Activities.MainActivities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.ygn.ygn_store_management.Adapters.ViewPagerAdapter;
import com.ygn.ygn_store_management.R;
import com.google.android.material.tabs.TabLayout;


public class MainCardViewActivity extends AppCompatActivity {

    //region members
    public String token;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //endregion

    //region overriden methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_card_view);
        findViews();
        initialize();
        getExtras();
        events();
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
    //endregion

    //region private methods
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
    //endregion
}