package com.example.testedutracker.ListProfiles;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testedutracker.Adapter.UserAdapter;
import com.example.testedutracker.Model.User;
import com.example.testedutracker.InformationActivity.ProfilesActivity;
import com.example.testedutracker.databinding.ActivityParentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParentActivity extends AppCompatActivity  {

    ActivityParentBinding binding;
    ArrayList<User> teachersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        teachersArrayList = new ArrayList<>();
        Intent extraIntent = this.getIntent();

        DatabaseReference currentSubject = FirebaseDatabase.getInstance()
                .getReference("Teachers").child(extraIntent.getStringExtra("item"));


        currentSubject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot keyId : snapshot.getChildren()) {

                    User user = new User(
                            keyId.child("id").getValue().toString(),
                            keyId.child("name").getValue().toString(),
                            keyId.child("phone").getValue().toString(),
                            keyId.child("username").getValue().toString(),
                            keyId.child("state").getValue().toString(),
                            keyId.child("imageURL").getValue().toString()
                    );
                    teachersArrayList.add(user);
                }

                displayTeachers();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayTeachers(){
        UserAdapter userAdapter = new UserAdapter(ParentActivity.this,teachersArrayList);
        binding.listview.setAdapter(userAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(ParentActivity.this, ProfilesActivity.class);
            i.putExtra("id",    teachersArrayList.get(position).getId());
            i.putExtra("name",  teachersArrayList.get(position).getName());
            i.putExtra("phone", teachersArrayList.get(position).getPhone());
            i.putExtra("email", teachersArrayList.get(position).getUsername());
            i.putExtra("state", teachersArrayList.get(position).getState());
            i.putExtra("imageURL", teachersArrayList.get(position).getImageURL());
            startActivity(i);
        });
    }

}
