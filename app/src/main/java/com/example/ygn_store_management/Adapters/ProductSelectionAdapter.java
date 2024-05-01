package com.example.ygn_store_management.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ygn_store_management.Models.Client;
import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.R;

import java.util.ArrayList;


public class ProductSelectionAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int resource;

    public ProductSelectionAdapter(@NonNull Context context, int pResource, @NonNull ArrayList<Product> pObjects) {
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
        String unitPrice= String.valueOf(getItem(position).UnitPrice);

        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.adapter_products_selection,parent,false);
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        TextView txtItemCode = convertView.findViewById(R.id.txtItemCode);
        TextView txtItemName = convertView.findViewById(R.id.txtItemName);
        TextView txtStockAmount = convertView.findViewById(R.id.txtStockAmount);
        TextView txtUnitPrice = convertView.findViewById(R.id.txtUnitPrice);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox); // Checkbox tanımlaması

        // Ürünün seçilip seçilmediğini kontrol etmek için checkbox durumunu güncelle
        checkBox.setChecked(getItem(position).isSelected);

        // Checkbox tıklama işlemi
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Product product = (Product) cb.getTag();
                product.isSelected = cb.isChecked();
            }
        });

        txtItemCode.setText(itemCode);
        txtItemName.setText(itemName);
        txtStockAmount.setText(stockAmount);
        txtUnitPrice.setText(unitPrice);
        checkBox.setTag(getItem(position));

        return convertView;
    }
}
