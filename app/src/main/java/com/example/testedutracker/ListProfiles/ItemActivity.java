package com.example.testedutracker.ListProfiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testedutracker.Adapter.ItemAdapter;
import com.example.testedutracker.InformationActivity.CurrentProfileActivity;
import com.example.testedutracker.Login.MainActivity;
import com.example.testedutracker.Model.ListItem;
import com.example.testedutracker.Model.User;
import com.example.testedutracker.R;
import com.example.testedutracker.databinding.ActivityItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    ActivityItemBinding binding;
    ArrayList<ListItem> itemList;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        in = this.getIntent();

        TextView userName = findViewById(R.id.userName);
        ImageView imageView = findViewById(R.id.current_profile_image);
        itemList = new ArrayList<>();

        userName.setText(in.getStringExtra("name"));

        String imgURL = in.getStringExtra("imgURL");
        if (imgURL.equals("default")) {
            imageView.setImageResource(R.drawable.ic_default);
        } else {
            Glide.with(ItemActivity.this).load(imgURL).into(imageView);
        }

        String state = in.getStringExtra("state");
        Log.v("State in First Page ::",state );

        DatabaseReference itemRef = null;
        if (state.equals("parent")) {
            itemRef = FirebaseDatabase.getInstance().getReference("Teachers");
        } else if (state.equals("teacher")) {
            itemRef = FirebaseDatabase.getInstance().getReference("Students");
        }

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyId : snapshot.getChildren()) {
                    ListItem item = new ListItem(keyId.getKey());
                    itemList.add(item);
                }
                displaySubjects();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(ItemActivity.this,CurrentProfileActivity.class);
            startActivity(intent);
        });
    }

    private void displaySubjects() {

        ItemAdapter listAdapter = new ItemAdapter(ItemActivity.this, itemList);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {

            Intent i = new Intent(ItemActivity.this, UserListActivity.class);
            i.putExtra("item", itemList.get(position).getItem());
            i.putExtra("state",in.getStringExtra("state"));
            startActivity(i);
        });
    }
}