package com.example.ygn_store_management.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ygn_store_management.Models.ReportViews.OrderInformationLines;
import com.example.ygn_store_management.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReportGeneralSalesAndPurchasingAdapter extends ArrayAdapter<OrderInformationLines> {
    private Context context;
    private int resource;

    public ReportGeneralSalesAndPurchasingAdapter(@NonNull Context context, int pResource, @NonNull ArrayList<OrderInformationLines> pObjects) {
        super(context, pResource, pObjects);
        this.context = context;
        this.resource=pResource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String itemName = getItem(position).ItemName;
        String amount = String.valueOf(getItem(position).Amount);
        String unitPrice = String.valueOf(getItem(position).UnitPrice);
        Double lineTotal = getItem(position).LineTotal;


        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedPrice = decimalFormat.format(lineTotal);


        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.adapter_products,parent,false);
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        TextView txtItemName = convertView.findViewById(R.id.txtItemName);
        TextView txtAmount = convertView.findViewById(R.id.txtAmount);
        TextView txtUnitPrice = convertView.findViewById(R.id.txtUnitPrice);
        TextView txtLineTotal = convertView.findViewById(R.id.txtLineTotal);

        txtItemName.setText(itemName);
        txtAmount.setText(amount);
        txtUnitPrice.setText(unitPrice);
        txtLineTotal.setText(formattedPrice);

        return convertView;
    }
}
