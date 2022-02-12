package com.example.testedutracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testedutracker.Model.ListItem;
import com.example.testedutracker.R;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<ListItem> {


    public ItemAdapter(Context context, ArrayList<ListItem> userArrayList){

        super(context, R.layout.item_list,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ListItem item = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list,parent,false);

        }

        TextView gradeName = convertView.findViewById(R.id.itemName);

        gradeName.setText(item.getItem());

        return convertView;
    }
}
