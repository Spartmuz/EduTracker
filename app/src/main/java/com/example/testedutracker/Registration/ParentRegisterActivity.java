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

public class ParentRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registerUser;
    private TextView banner;

    private EditText editTextStudentName;
    private EditText editTextParentName;
    private EditText editTextPhoneNumber;
    private EditText editTextGradeNumber;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    /**  Main Class*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        auth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.regParent);
        registerUser.setOnClickListener(this);

        editTextStudentName = (EditText) findViewById(R.id.studentName);
        editTextParentName = (EditText) findViewById(R.id.parentName);
        editTextGradeNumber = (EditText) findViewById(R.id.gradeNumber);
        editTextPhoneNumber = (EditText) findViewById(R.id.pPhone);
        editTextEmail = (EditText) findViewById(R.id.pEmail);
        editTextPassword = (EditText) findViewById(R.id.pPassword);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.regParent:
                registerStudent();
                break;
        }
    }

    private void registerStudent() {

        String student = editTextStudentName.getText().toString().trim();
        String parentN = editTextParentName.getText().toString().trim();
        String phone = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (student.isEmpty()){
            editTextStudentName.setError("Աշակերտի Անուն Ազգ. պարտադիր է");
            editTextStudentName.requestFocus();
            return;
        }
        if (parentN.isEmpty()){
            editTextParentName.setError("Ծնողի Անուն Ազգ. պարտադիր է");
            editTextParentName.requestFocus();
            return;
        }
        if (email.isEmpty()){
            editTextEmail.setError("Էլ. հասցեն պարտադիր է");
            editTextEmail.requestFocus();
            return;
        }
//        if (gradeN.isEmpty()){
//            editTextGradeNumber.setError("Դասարան դաշտը պարտադիր է");
//            editTextGradeNumber.requestFocus();
//            return;
//        }
//        if (gradeN.length()>2){
//            editTextGradeNumber.setError("Ոչ վավեր");
//            editTextGradeNumber.requestFocus();
//            return;
//        }
        if (editTextGradeNumber.getText().toString().trim().isEmpty()){
            editTextGradeNumber.setError("Դասարան դաշտը պարտադիր է");
            editTextGradeNumber.requestFocus();
            return;
        }
        if (editTextGradeNumber.getText().toString().trim().length()>2){
            editTextGradeNumber.setError("Ոչ վավեր");
            editTextGradeNumber.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Ոչ վավեր էլ. հասցե");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            editTextPassword.setError("Գաղտնաբառը պարտադիր է");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            editTextPassword.setError("Առնվազն 6 նիշ");
            editTextPassword.requestFocus();
            return;
        }

        String gradeN = "Դասարան "+editTextGradeNumber.getText().toString().trim();


        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String userid = firebaseUser.getUid();

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("username", email);
                        hashMap.put("imageURL", "default");
                        hashMap.put("student", student);
                        hashMap.put("parent", parentN);
                        hashMap.put("phone", phone);
                        hashMap.put("search", email.toLowerCase());
                        hashMap.put("state","parent");
                        hashMap.put("grade", gradeN);
                        reference = FirebaseDatabase.getInstance().getReference("Students").child(gradeN).child(userid);
                        reference.setValue(hashMap);


                        HashMap<String, String> hashMap1 = new HashMap<>();
                        hashMap1.put("id", userid);
                        hashMap1.put("username", email);
                        hashMap1.put("imageURL", "default");
                        hashMap1.put("name", parentN);
                        hashMap1.put("phone", phone);
                        hashMap1.put("search", email.toLowerCase());
                        hashMap1.put("state","parent");
                        hashMap1.put("grade", gradeN);
                        hashMap1.put("subject", null);
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                        reference.setValue(hashMap1).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                Intent intent = new Intent(ParentRegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(ParentRegisterActivity.this, "Դուք չեք կարող գրանցվել այս էլ. փոստով կամ գաղտնաբառով", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}