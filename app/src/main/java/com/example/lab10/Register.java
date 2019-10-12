package com.example.lab10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lab10.Database.DBHelper;

public class Register extends AppCompatActivity {

    EditText un;
    EditText pw;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        un = findViewById(R.id.un);
        pw = findViewById(R.id.pw);
        radioGroup = findViewById(R.id.radioGroup);
    }

    public void register(View view) {
        DBHelper dbHelper = new DBHelper(this);
        String username = un.getText().toString();
        String password = pw.getText().toString();
        String type = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else if (!type.equals("Teacher") && !type.equals("Student")) {
            Toast.makeText(this, "Please select a type", Toast.LENGTH_SHORT).show();
        } else if (!dbHelper.validateUsername(username)) {
            Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
        } else {
            if (dbHelper.insertUser(username, password, type)) {
                Toast.makeText(this, "User successfully registered", Toast.LENGTH_SHORT).show();
                if ("Teacher".equals(type)) {
                    Intent intent = new Intent(this, Teacher.class);
                    intent.putExtra(MainActivity.USERNAME, username);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, Student.class);
                    intent.putExtra(MainActivity.USERNAME, username);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
