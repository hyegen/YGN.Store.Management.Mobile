
package com.example.ygn_store_management.MenuFragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ygn_store_management.Activities.MainActivities.MainCardViewActivity;
import com.example.ygn_store_management.Activities.ReportActivities.GeneralReportActivities.ReportGeneralSalesAndPurchasing;
import com.example.ygn_store_management.Activities.ReportActivities.GeneralReportActivities.ReportItemInformationActivity;
import com.example.ygn_store_management.Activities.ReportActivities.GeneralReportActivities.ReportStockAmountActivity;
import com.example.ygn_store_management.Activities.ReportActivities.PurchasingReports.ReportPurchasingDetailByClientDetail;
import com.example.ygn_store_management.Activities.ReportActivities.SalesReports.ReportSalesDetailByClientDetail;
import com.example.ygn_store_management.R;

public class FragmentReports extends Fragment {
    private String _currentToken;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MainCardViewActivity mainCardViewActivity= (MainCardViewActivity) getActivity();
        _currentToken = mainCardViewActivity.token;

        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        CardView cardViewStockAmount = view.findViewById(R.id.cardViewStockAmount);
        CardView cardViewSalesDetailByClientDetail = view.findViewById(R.id.cardViewSalesDetailByClientDetail);
        CardView cardViewOrderQuery = view.findViewById(R.id.cardViewOrderQuery);
        CardView cardViewPurchasingDetailByClientDetail = view.findViewById(R.id.cardViewPurchasingDetailByClientDetail);
        CardView cardViewItemInformation = view.findViewById(R.id.cardViewItemInformation);

        cardViewStockAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ReportStockAmountActivity.class);
                intent.putExtra("TOKEN", _currentToken);
                startActivity(intent);
            }
        });
        cardViewSalesDetailByClientDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ReportSalesDetailByClientDetail.class);
                intent.putExtra("TOKEN", _currentToken);
                startActivity(intent);
            }
        });
        cardViewOrderQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ReportGeneralSalesAndPurchasing.class);
                intent.putExtra("TOKEN", _currentToken);
                startActivity(intent);
            }
        });
        cardViewPurchasingDetailByClientDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ReportPurchasingDetailByClientDetail.class);
                intent.putExtra("TOKEN", _currentToken);
                startActivity(intent);
            }
        });
        cardViewItemInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ReportItemInformationActivity.class);
                intent.putExtra("TOKEN", _currentToken);
                startActivity(intent);
            }
        });
        return view;
    }
}
