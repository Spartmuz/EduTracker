package com.example.testedutracker.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testedutracker.R;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView teacher;
    private TextView parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        teacher = findViewById(R.id.chooseTeacher);
        parent = findViewById(R.id.chooseParent);

        teacher.setOnClickListener(this);
        parent .setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseTeacher:
                startActivity(new Intent(ChooseActivity.this, TeacherRegisterActivity.class));
                finish();
                break;
            case R.id.chooseParent:
                startActivity(new Intent(ChooseActivity.this, ParentRegisterActivity.class));
                finish();
                break;
        }
    }

}