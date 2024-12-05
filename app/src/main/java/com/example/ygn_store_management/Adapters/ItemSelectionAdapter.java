package com.example.ygn_store_management.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ygn_store_management.Models.Dtos.ItemSelectionDto;
import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.R;

import java.util.List;

public class ItemSelectionAdapter extends RecyclerView.Adapter<ItemSelectionAdapter.ProductViewHolder>{
    private List<ItemSelectionDto> productList;

    public ItemSelectionAdapter(List<ItemSelectionDto> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection_row, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ItemSelectionDto product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCode, tvAmount;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCode = itemView.findViewById(R.id.tv_code);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }

        public void bind(ItemSelectionDto product) {
            tvCode.setText(product.getCode());
            tvName.setText(product.getName());
            tvAmount.setText("Miktar: " + product.getAmount());
        }
    }
}
