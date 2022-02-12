package com.example.testedutracker.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testedutracker.ListProfiles.ItemActivity;
import com.example.testedutracker.Model.User;
import com.example.testedutracker.R;
import com.example.testedutracker.Registration.ChooseActivity;
import com.example.testedutracker.Registration.ResetPasswordActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.tEmail);
        password = findViewById(R.id.tPassword);

        TextView register = findViewById(R.id.register);
        Button signInBtn = findViewById(R.id.logIn);
        TextView forgotTextLink = findViewById(R.id.forgetPassword);

        register.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        forgotTextLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(MainActivity.this, ChooseActivity.class));
                finish();
                break;
            case R.id.logIn:
                login();
                break;
            case R.id.forgetPassword:
                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
                finish();
                break;
        }
    }

    private void login() {
        String txt_email = email.getText().toString();
        String txt_password = password.getText().toString();
        if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
            Toast.makeText(MainActivity.this, "Բոլոր դաշտերը պարտադիր են", Toast.LENGTH_SHORT).show();
        }
        else {

            auth.signInWithEmailAndPassword(txt_email, txt_password)
                    .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {

                    checkState();

                } else {
                    Toast.makeText(MainActivity.this, "Sign In fail !", Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    private void checkState() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            Intent intent = null;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);


                intent = new Intent(MainActivity.this, ItemActivity.class);
                intent.putExtra("name", user.getName());
                intent.putExtra("state",user.getState());
                intent.putExtra("imgURL", user.getImageURL());
                Toast.makeText(MainActivity.this, "Sign in successfully!", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}