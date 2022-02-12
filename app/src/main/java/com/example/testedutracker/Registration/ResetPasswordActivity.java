package com.example.testedutracker.Registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toolbar;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testedutracker.Login.MainActivity;
import com.example.testedutracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText send_email;
    Button btn_reset;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        send_email = findViewById(R.id.send_email);
        btn_reset = findViewById(R.id.btn_reset);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(view -> {
            String email = send_email.getText().toString();

            if (email.equals("")){
                Toast.makeText(ResetPasswordActivity.this, "Բոլոր դաշտերը պարտադիր են լրացման համար!", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this, "Խնդրում ենք ստուգել ձեր էլ. հասցեն", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ResetPasswordActivity.this, MainActivity.class));
                            finish();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
