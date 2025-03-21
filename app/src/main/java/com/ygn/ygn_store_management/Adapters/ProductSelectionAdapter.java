package com.ygn.ygn_store_management.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ygn.ygn_store_management.Models.Product;
import com.ygn.ygn_store_management.R;

import java.util.ArrayList;
import java.util.Collections;

public class ProductSelectionAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int resource;
    public ArrayList<Integer> quantities;
    private static final String TAG = "ProductSelectionDialogAdapter";
    public ProductSelectionAdapter(@NonNull Context context, int pResource, @NonNull ArrayList<Product> pObjects, ArrayList<Integer> quantities) {
        super(context, pResource, pObjects);
        this.context = context;
        this.resource = pResource;
        //this.quantities = quantities;
        this.quantities = new ArrayList<>(Collections.nCopies(pObjects.size(), 0));
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        String itemName = getItem(position).getItemName();
        String itemCode = getItem(position).getItemCode();
        String unitPrice = String.valueOf(getItem(position).getUnitPrice());
        String  amount = String.valueOf(getItem(position).getAmount());

        ArrayList<Integer> addedItemIds = new ArrayList<>();
        ArrayList<Product> products = getProducts();

        for (Product product : products) {
            addedItemIds.add(product.ItemId);
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_products_selection, parent, false);
        }

        TextView txtItemCode = convertView.findViewById(R.id.txtItemCode);
        TextView txtItemName = convertView.findViewById(R.id.txtItemName);
        TextView txtUnitPrice = convertView.findViewById(R.id.txtUnitPrice);
        CheckBox checkBox = convertView.findViewById(R.id.itemCheckBox);
        EditText editTextQuantity = convertView.findViewById(R.id.editTextQuantity);

        txtItemCode.setText(itemCode);
        txtItemName.setText(itemName);
        txtUnitPrice.setText(unitPrice);
        checkBox.setTag(getItem(position));
        checkBox.setChecked(getItem(position).isSelected);
        editTextQuantity.setText(amount);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Product product = (Product) cb.getTag();
                product.isSelected = cb.isChecked();
            }
        });
        editTextQuantity.addTextChangedListener(new TextWatcher() {
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
                        editTextQuantity.requestFocus();
                    } catch (NumberFormatException e) {
                    }
                } else {
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