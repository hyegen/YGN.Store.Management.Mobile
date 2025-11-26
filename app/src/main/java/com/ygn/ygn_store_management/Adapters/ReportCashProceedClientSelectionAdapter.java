package com.ygn.ygn_store_management.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Models.Dtos.ClientSelectionDto;
import com.ygn.ygn_store_management.Models.ReportViews.PurchasingDetailByClientDetail;
import com.ygn.ygn_store_management.R;

import java.util.ArrayList;
import java.util.List;

public class ReportCashProceedClientSelectionAdapter extends RecyclerView.Adapter<ReportCashProceedClientSelectionAdapter.ViewHolder>{
    private List<ClientSelectionDto> _clientSelectionDtoList;
    private List<ClientSelectionDto> filteredList;
    public ReportCashProceedClientSelectionAdapter(List<ClientSelectionDto> clientSelectionDtoList) {
        this._clientSelectionDtoList = clientSelectionDtoList;
        this.filteredList = new ArrayList<>(clientSelectionDtoList);
    }
    @NonNull
    @Override
    public ReportCashProceedClientSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_client_selection, parent, false);
        return new ReportCashProceedClientSelectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportCashProceedClientSelectionAdapter.ViewHolder holder, int position) {
        ClientSelectionDto clientSelectionDto = _clientSelectionDtoList.get(position);
        holder.clientIdTextView.setText(String.valueOf(clientSelectionDto.getClientId()));
        holder.clientDescriptionTextView.setText(clientSelectionDto.getClientDescription());
        /*holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("description", item.getDescription());
            context.startActivity(intent);
        });*/
    }

    @Override
    public int getItemCount() {
        return  _clientSelectionDtoList.size();
    }
    public void updateData(List<ClientSelectionDto> newData) {
        _clientSelectionDtoList.clear();
        _clientSelectionDtoList.addAll(newData);
        filter("");
    }
    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(_clientSelectionDtoList);
        } else {
            text = text.toLowerCase();
            for (ClientSelectionDto client : _clientSelectionDtoList) {
                if (client.getClientDescription().toLowerCase().contains(text)) {
                    filteredList.add(client);
                }
            }
        }
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView clientIdTextView;
        TextView clientDescriptionTextView;
        CardView cardView;

        public ViewHolder(@NonNull View clientView) {
            super(clientView);
            clientIdTextView = clientView.findViewById(R.id.txtClientId);
            clientDescriptionTextView = clientView.findViewById(R.id.txtClientDescription);
            cardView = (CardView) itemView;
        }
    }
}
