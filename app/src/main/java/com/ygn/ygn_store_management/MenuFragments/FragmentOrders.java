package com.ygn.ygn_store_management.MenuFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ygn.ygn_store_management.Activities.DialogActivities.ClientSelectionDialogActivity;
import com.ygn.ygn_store_management.Activities.MainActivities.MainCardViewActivity;
import com.ygn.ygn_store_management.Activities.ReportActivities.SalesReports.ReportSalesDetailByClientDetail;
import com.ygn.ygn_store_management.Activities.SalesActivities.SalesActivity;
import com.ygn.ygn_store_management.R;

public class FragmentOrders extends Fragment {

    CardView cardViewSales;
    CardView cardViewPurchasing;
    private String _currentToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setToken();
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        cardViewSales = view.findViewById(R.id.cardViewSales);
        cardViewPurchasing = view.findViewById(R.id.cardViewPurchasing);
        events();

        return view;
    }
    private void setToken() {
        MainCardViewActivity mainCardViewActivity = (MainCardViewActivity) getActivity();
        _currentToken = mainCardViewActivity.token;
    }
    private void events() {
        cardViewSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent clientSelectionIntent = new Intent(requireActivity(), ClientSelectionDialogActivity.class);
                clientSelectionIntent.putExtra("TOKEN",_currentToken);
                clientSelectionIntent.putExtra("IOCode",1);
                startActivity(clientSelectionIntent);*/

                Intent intent = new Intent(requireActivity(), SalesActivity.class);
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

    }
}