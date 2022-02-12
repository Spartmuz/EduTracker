package com.example.testedutracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.testedutracker.Model.User;
import com.example.testedutracker.R;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {


    public UserAdapter(Context context, ArrayList<User> userArrayList) {

        super(context, R.layout.user_list, userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User user = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list, parent, false);

        }

        ImageView imageView = convertView.findViewById(R.id.profile_pic);
        TextView userName = convertView.findViewById(R.id.personName);


        if (user.getImageURL().equals("default")) {
            imageView.setImageResource(R.drawable.ic_default);
        } else {
            Glide.with(getContext()).load(user.getImageURL()).into(imageView);
        }

        if (user.getState().equals("parent")){
            userName.setText(user.getStudent());
        }else if (user.getState().equals("teacher")){
            userName.setText(user.getName());
        }

        return convertView;
    }
}
