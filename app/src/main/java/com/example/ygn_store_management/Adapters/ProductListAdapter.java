package com.example.ygn_store_management.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.R;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    private ArrayList<Product> mProductList;

    public ProductListAdapter(Context context, ArrayList<Product> productList) {
        super(context, 0, productList);
        mContext = context;
        mProductList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // ListView'ın her bir satırını oluştur ve doldur
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.adapter_products, parent, false);
        }

        Product currentProduct = mProductList.get(position);

        // Diğer işlemleri yap...

        return listItem;
    }
    public void updateData(ArrayList<Product> newList) {
        mProductList.clear();
        mProductList.addAll(newList);
        notifyDataSetChanged();
    }

}
