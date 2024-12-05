package com.example.ygn_store_management.MenuFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ygn_store_management.Activities.MainActivities.MainCardViewActivity;
import com.example.ygn_store_management.Activities.ReportActivities.GeneralReportActivities.ReportStockAmountActivity;
import com.example.ygn_store_management.Activities.ReportActivities.SalesReports.ReportSalesDetailByClientDetail;
import com.example.ygn_store_management.R;

public class FragmentOrders extends Fragment {
    private String _currentToken;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainCardViewActivity mainCardViewActivity = (MainCardViewActivity) getActivity();
        _currentToken = mainCardViewActivity.token;

        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        CardView cardViewSales = view.findViewById(R.id.cardViewSales);
        CardView cardViewPurchasing = view.findViewById(R.id.cardViewPurchasing);


        cardViewSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ReportStockAmountActivity.class);
                intent.putExtra("TOKEN", _currentToken);
                startActivity(intent);
            }
        });
        cardViewPurchasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ReportSalesDetailByClientDetail.class);
                intent.putExtra("TOKEN", _currentToken);
                startActivity(intent);
            }
        });

        return view;
    }
}