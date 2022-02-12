package com.example.testedutracker.InformationActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.testedutracker.R;
import com.example.testedutracker.databinding.ActivityProfilesBinding;

public class ProfilesActivity extends AppCompatActivity {
    ActivityProfilesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null) {

            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            String email = intent.getStringExtra("email");
            String imageURL = intent.getStringExtra("imageURL");

            binding.nameProfile.setText(name);
            binding.phoneProfile.setText(phone);
            binding.mailProfile.setText(email);

            if (imageURL.equals("default")) {
                binding.profileImage.setImageResource(R.drawable.ic_default);
            }
            else {
                Glide.with(getApplicationContext()).load(imageURL).into(binding.profileImage);
            }


        }
    }
}
