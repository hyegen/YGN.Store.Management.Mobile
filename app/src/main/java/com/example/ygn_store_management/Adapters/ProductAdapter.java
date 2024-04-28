package com.example.ygn_store_management.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.R;

import java.util.ArrayList;
import java.util.List;
public class ProductAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int resource;

    public ProductAdapter(@NonNull Context context, int pResource, @NonNull ArrayList<Product> pObjects) {
        super(context, pResource, pObjects);
        this.context = context;
        this.resource=pResource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String itemName = getItem(position).ItemName;
        String itemCode= getItem(position).ItemCode;
        String stockAmount= String.valueOf(getItem(position).StockAmount);

        if (convertView==null){
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.products_adapter,parent,false);
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        TextView txtItemCode = convertView.findViewById(R.id.txtItemCode);
        TextView txtItemName = convertView.findViewById(R.id.txtItemName);
        TextView txtStockAmount = convertView.findViewById(R.id.txtStockAmount);

        txtItemCode.setText(itemCode);
        txtItemName.setText(itemName);
        txtStockAmount.setText(stockAmount);

        return convertView;
    }
}