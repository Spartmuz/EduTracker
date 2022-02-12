package com.example.testedutracker.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testedutracker.Login.MainActivity;
import com.example.testedutracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TeacherRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerTeacher;
    private TextView banner;

    private EditText eTextName;
    private EditText eTextSubject;
    private EditText eTextPhoneNumber;
    private EditText eTextEmail;
    private EditText eTextPassword;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);

        auth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerTeacher = (Button) findViewById(R.id.regTeacher);
        registerTeacher.setOnClickListener(this);


        eTextName = (EditText) findViewById(R.id.tName);
        eTextSubject = findViewById(R.id.tSubject);
        eTextPhoneNumber = (EditText) findViewById(R.id.tPhone);
        eTextEmail = (EditText) findViewById(R.id.tEmail);
        eTextPassword = (EditText) findViewById(R.id.tPassword);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.regTeacher:
                registerTeacher();
                break;
        }
    }

    private void registerTeacher() {

        String name = eTextName.getText().toString().trim();
        String subject = eTextSubject.getText().toString().trim();
        String phone = eTextPhoneNumber.getText().toString().trim();
        String email = eTextEmail.getText().toString().trim();
        String password = eTextPassword.getText().toString().trim();

        if (name.isEmpty()) {
            eTextName.setError("Անուն Ազգ. պարտադիր է");
            eTextName.requestFocus();
            return;
        }
        if (subject.isEmpty()) {
            eTextSubject.setError("Առարկան պարտադիր է");
            eTextSubject.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            eTextEmail.setError("Էլ. հասցեն պարտադիր է");
            eTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            eTextEmail.setError("Ոչ վավեր էլ. հասցե");
            eTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            eTextPassword.setError("Գաղտնաբառը պարտադիր է");
            eTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            eTextPassword.setError("Առնվազն 6 նիշ");
            eTextPassword.requestFocus();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String userid = firebaseUser.getUid();

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("username", email);
                        hashMap.put("imageURL", "default");
                        hashMap.put("name", name);
                        hashMap.put("phone", phone);
                        hashMap.put("search", email.toLowerCase());
                        hashMap.put("state","teacher");
                        hashMap.put("subject", subject);
                        reference = FirebaseDatabase.getInstance().getReference("Teachers").child(subject).child(userid);
                        reference.setValue(hashMap);


                        HashMap<String, String> hashMap1 = new HashMap<>();
                        hashMap1.put("id", userid);
                        hashMap1.put("username", email);
                        hashMap1.put("imageURL", "default");
                        hashMap1.put("name", name);
                        hashMap1.put("phone", phone);
                        hashMap1.put("search", email.toLowerCase());
                        hashMap1.put("state","teacher");
                        hashMap1.put("grade", null);
                        hashMap1.put("subject", subject);
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Intent intent = new Intent(TeacherRegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                    } else {
                        Toast.makeText(TeacherRegisterActivity.this, "Դուք չեք կարող գրանցվել այս էլ. փոստով կամ գաղտնաբառով", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}