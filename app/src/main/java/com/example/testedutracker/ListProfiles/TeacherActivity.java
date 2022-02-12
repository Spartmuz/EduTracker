package com.example.testedutracker.ListProfiles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testedutracker.Adapter.UserAdapter;
import com.example.testedutracker.Model.User;
import com.example.testedutracker.InformationActivity.ProfilesActivity;
import com.example.testedutracker.databinding.ActivityTeacherBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherActivity extends AppCompatActivity {

    ActivityTeacherBinding binding;
    ArrayList<User> studentsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        studentsArrayList = new ArrayList<>();
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
                for (DataSnapshot keyId : snapshot.getChildren()) {

                    User user = new User(
                            keyId.child("id").getValue().toString(),
                            keyId.child("username").getValue().toString(),
                            keyId.child("imageURL").getValue().toString(),
                            keyId.child("name").getValue().toString(),
                            keyId.child("phone").getValue().toString(),
                            keyId.child("state").getValue().toString()
                    );

                    studentsArrayList.add(user);
                }

                displayStudentList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void displayStudentList(){
        UserAdapter userAdapter = new UserAdapter(TeacherActivity.this,studentsArrayList);
        binding.listview.setAdapter(userAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(TeacherActivity.this, ProfilesActivity.class);
            i.putExtra("id", studentsArrayList.get(position).getId());
            i.putExtra("name", studentsArrayList.get(position).getName());
            i.putExtra("phone", studentsArrayList.get(position).getPhone());
            i.putExtra("email", studentsArrayList.get(position).getUsername());
            i.putExtra("state", studentsArrayList.get(position).getState());
            i.putExtra("imageURL", studentsArrayList.get(position).getImageURL());
            startActivity(i);
        });
    }

}