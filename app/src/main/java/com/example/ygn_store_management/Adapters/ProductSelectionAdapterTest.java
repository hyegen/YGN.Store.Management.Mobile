package com.example.ygn_store_management.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.R;

import java.util.ArrayList;
import java.util.Collections;

public class ProductSelectionAdapterTest extends ArrayAdapter<Product> {
    private Context context;
    private int resource;
    public ArrayList<Integer> quantities;
    private static final String TAG = "ProductSelectionDialogAdapter";
    static class ViewHolder {
        TextView txtItemCode;
        TextView txtItemName;
        TextView txtUnitPrice;
        CheckBox checkBox;
        EditText editTextQuantity;
    }
    public ProductSelectionAdapterTest(@NonNull Context context, int pResource, @NonNull ArrayList<Product> pObjects, ArrayList<Integer> quantities) {
        super(context, pResource, pObjects);
        this.context = context;
        this.resource = pResource;
        this.quantities = new ArrayList<>(Collections.nCopies(pObjects.size(), 0));
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /*ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_products_selection, parent, false);
            holder = new ViewHolder();
            holder.txtItemCode = convertView.findViewById(R.id.txtItemCode);
            holder.txtItemName = convertView.findViewById(R.id.txtItemName);
            holder.txtUnitPrice = convertView.findViewById(R.id.txtUnitPrice);
            holder.checkBox = convertView.findViewById(R.id.itemCheckBox);
            holder.editTextQuantity = convertView.findViewById(R.id.editTextQuantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String itemName = getItem(position).getItemName();
        String itemCode = getItem(position).getItemCode();
        String unitPrice = String.valueOf(getItem(position).getUnitPrice());

        holder.txtItemCode.setText(itemCode);
        holder.txtItemName.setText(itemName);
        holder.txtUnitPrice.setText(unitPrice);
        holder.checkBox.setChecked(getItem(position).isSelected);

        holder.checkBox.setOnClickListener(v -> {
            CheckBox cb = (CheckBox) v;
            Product product = (Product) cb.getTag();
            product.isSelected = cb.isChecked();
        });

        holder.editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String quantityStr = s.toString();
                if (!quantityStr.isEmpty()) {
                    try {
                        int quantity = Integer.parseInt(quantityStr);
                        if (quantities.size() > position) {
                            quantities.set(position, quantity);
                        } else {
                            quantities.add(quantity);
                        }
                        holder.editTextQuantity.requestFocus();
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Invalid quantity format: " + e.getMessage());
                    }
                }
            }
        });

        return convertView;*/
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_products_selection, parent, false);
            holder = new ViewHolder();
            holder.txtItemCode = convertView.findViewById(R.id.txtItemCode);
            holder.txtItemName = convertView.findViewById(R.id.txtItemName);
            holder.txtUnitPrice = convertView.findViewById(R.id.txtUnitPrice);
            holder.checkBox = convertView.findViewById(R.id.itemCheckBox);
            holder.editTextQuantity = convertView.findViewById(R.id.editTextQuantity);
            holder.editTextQuantity.setText("");
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product currentProduct = getItem(position);
        String itemName = currentProduct.getItemName();
        String itemCode = currentProduct.getItemCode();
        String unitPrice = String.valueOf(currentProduct.getUnitPrice());

        holder.txtItemCode.setText(itemCode);
        holder.txtItemName.setText(itemName);
        holder.txtUnitPrice.setText(unitPrice);
        holder.checkBox.setChecked(currentProduct.isSelected);
        holder.checkBox.setTag(currentProduct);


        holder.checkBox.setOnClickListener(v -> {
            CheckBox cb = (CheckBox) v;
            Product product = (Product) cb.getTag();
            product.isSelected = cb.isChecked();
        });

        holder.editTextQuantity.setTag(position);
        holder.editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String quantityStr = s.toString();
                if (!quantityStr.isEmpty()) {
                    try {
                        int quantity = Integer.parseInt(quantityStr);
                        int pos = (int) holder.editTextQuantity.getTag();
                        if (quantities.size() > pos) {
                            quantities.set(pos, quantity);
                        } else {
                            quantities.add(quantity);
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Invalid quantity format: " + e.getMessage());
                    }
                }
            }
        });

        return convertView;
    }
    public void updateData(ArrayList<Product> newData) {
        ArrayList<Product> updatedData = new ArrayList<>(this.getCount());
        updatedData.addAll(this.getProducts());

        for (Product newProduct : newData) {
            boolean exists = false;
            for (Product existingProduct : updatedData) {
                if (existingProduct.ItemId.equals(newProduct.ItemId)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                updatedData.add(newProduct);
            } else {
                Toast.makeText(context, "Ürün Zaten Eklenmiş.", Toast.LENGTH_SHORT).show();
            }
        }
        clear();
        addAll(updatedData);
        notifyDataSetChanged();
    }
    public ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            products.add(getItem(i));
        }
        return products;
    }
}
