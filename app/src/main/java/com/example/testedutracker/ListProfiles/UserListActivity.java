package com.example.testedutracker.ListProfiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.testedutracker.Adapter.UserAdapter;
import com.example.testedutracker.InformationActivity.ProfilesActivity;
import com.example.testedutracker.Model.User;
import com.example.testedutracker.databinding.ActivityUserListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    ActivityUserListBinding binding;
    ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userArrayList = new ArrayList<>();
        Intent extraIntent = this.getIntent();

        String item  = extraIntent.getStringExtra("item");
        String state  = extraIntent.getStringExtra("state");

        DatabaseReference currentGrade = null;

        if (state.equals("parent")) {
            currentGrade = FirebaseDatabase.getInstance().getReference("Teachers").child(item);
        }else if (state.equals("teacher")){
            currentGrade = FirebaseDatabase.getInstance().getReference("Students").child(item);
        }

        currentGrade.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (state.equals("parent")) {
                    for (DataSnapshot keyId : snapshot.getChildren()) {
                        User user = new User(
                                keyId.child("id").getValue().toString(),
                                keyId.child("imageURL").getValue().toString(),
                                keyId.child("name").getValue().toString(),
                                keyId.child("phone").getValue().toString(),
                                keyId.child("search").getValue().toString(),
                                keyId.child("state").getValue().toString(),
                                keyId.child("subject").getValue().toString(),
                                keyId.child("username").getValue().toString()
                        );
                        userArrayList.add(user);
                    }

                }
                else if (state.equals("teacher")) {
                    for (DataSnapshot keyId : snapshot.getChildren()) {
                        User user = new User(
                                keyId.child("grade").getValue().toString(),
                                keyId.child("id").getValue().toString(),
                                keyId.child("imageURL").getValue().toString(),
                                keyId.child("parent").getValue().toString(),
                                keyId.child("phone").getValue().toString(),
                                keyId.child("search").getValue().toString(),
                                keyId.child("state").getValue().toString(),
                                keyId.child("student").getValue().toString(),
                                keyId.child("username").getValue().toString()
                        );
                        userArrayList.add(user);
                    }
                }

                displayUserList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void displayUserList(){
        UserAdapter userAdapter = new UserAdapter(UserListActivity.this, userArrayList);
        binding.listview.setAdapter(userAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {

            Intent i = new Intent(UserListActivity.this, ProfilesActivity.class);
            i.putExtra("id", userArrayList.get(position).getId());
            i.putExtra("phone", userArrayList.get(position).getPhone());
            i.putExtra("email", userArrayList.get(position).getUsername());
            i.putExtra("state", userArrayList.get(position).getState());
            i.putExtra("imageURL", userArrayList.get(position).getImageURL());

            if (userArrayList.get(position).getState().equals("parent")){
                i.putExtra("name", userArrayList.get(position).getParent());
            }
            else if (userArrayList.get(position).getState().equals("teacher")){
                i.putExtra("name", userArrayList.get(position).getName());
            }

            startActivity(i);
        });
    }

}