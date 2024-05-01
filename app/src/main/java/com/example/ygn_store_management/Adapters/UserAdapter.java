package com.example.ygn_store_management.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ygn_store_management.Models.User;
import com.example.ygn_store_management.R;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    private int resource;

    public UserAdapter(@NonNull Context context, int pResource, @NonNull ArrayList<User> pObjects) {
        super(context, pResource, pObjects);
        this.context = context;
        this.resource=pResource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String userName = getItem(position).UserName;

        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.adapter_user,parent,false);
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);
        TextView txtUserName = convertView.findViewById(R.id.txtUserName);

        txtUserName.setText(userName);
        return convertView;
    }

    /*@NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String userName = getItem(position).UserName;

        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.user_adapter,parent,false);
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        TextView txtUserName = convertView.findViewById(R.id.txtUserName);

        txtUserName.setText(userName);

        return convertView;
    }*/
}
