package com.ygn.ygn_store_management.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Interfaces.ClientSelectionService;
import com.ygn.ygn_store_management.Models.Dtos.ClientSelectionDto;
import com.ygn.ygn_store_management.Models.ReportViews.PurchasingDetailByClientDetail;
import com.ygn.ygn_store_management.R;

import java.util.ArrayList;
import java.util.List;

public class ClientSelectionAdapter extends RecyclerView.Adapter<ClientSelectionAdapter.ViewHolder>{
    private List<ClientSelectionDto> _clientSelectionDtoList;
    public ClientSelectionAdapter(List<ClientSelectionDto> clientSelectionDtoList) {
        this._clientSelectionDtoList = clientSelectionDtoList;
    }
    @NonNull
    @Override
    public ClientSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_client_selection, parent, false);
        return new ClientSelectionAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ClientSelectionAdapter.ViewHolder holder, int position) {
        ClientSelectionDto clientSelectionDto = _clientSelectionDtoList.get(position);
        holder.clientIdTextView.setText(String.valueOf(clientSelectionDto.getClientId()));
        holder.clientDescriptionTextView.setText(clientSelectionDto.getClientDescription());
    }
    @Override
    public int getItemCount() {
      return  _clientSelectionDtoList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView clientIdTextView;
        TextView clientDescriptionTextView;


        public ViewHolder(@NonNull View clientView) {
            super(clientView);
            clientIdTextView = clientView.findViewById(R.id.txtClientId);
            clientDescriptionTextView = clientView.findViewById(R.id.txtClientDescription);

        }
    }
}
