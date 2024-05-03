package com.example.ygn_store_management.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.R;

import java.util.ArrayList;

public class SalesDetailAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int resource;
    public SalesDetailAdapter(@NonNull Context context, int pResource, @NonNull ArrayList<Product> pObjects) {
        super(context, pResource, pObjects);
        this.context = context;
        this.resource=pResource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String itemCode= getItem(position).ItemCode;
        String itemName = getItem(position).ItemName;
        //String selectedAmount = String.valueOf(getItem(position).Amount);
        Integer selectedAmount = getItem(position).Amount;
        //String unitPrice = String.valueOf(getItem(position).UnitPrice);
        Double unitPrice = getItem(position).UnitPrice;

        double totalPrice = selectedAmount * unitPrice;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_sales_detail, parent, false);
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView txtItemCode = convertView.findViewById(R.id.txtItemCode);
        TextView txtItemName = convertView.findViewById(R.id.txtItemName);
        TextView txtSelectedAmount = convertView.findViewById(R.id.txtSelectedAmount);
        TextView txtUnitPrice = convertView.findViewById(R.id.txtUnitPrice);
        TextView txtTotalPrice = convertView.findViewById(R.id.txtTotalPrice);

        txtItemCode.setText(itemCode);
        txtItemName.setText(itemName);
        txtSelectedAmount.setText(String.valueOf(selectedAmount));
       // txtUnitPrice.setText(unitPrice);

        txtUnitPrice.setText(String.valueOf(unitPrice));
       // txtTotalPrice.setText(String.valueOf(totalPrice));

        return convertView;
    }
}
