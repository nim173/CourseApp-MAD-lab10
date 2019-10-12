package com.example.lab10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab10.Database.DBHelper;

public class MainActivity extends AppCompatActivity {
    public static final String USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                EditText un = findViewById(R.id.un);
                EditText pw = findViewById(R.id.pw);
                String username = un.getText().toString();
                String password = pw.getText().toString();
                switch (dbHelper.readUser(username, password)) {
                    case 0:
                        Toast.makeText(MainActivity.this, "Invalid credentials, please try again", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                        Intent intentTeacher = new Intent(getApplicationContext(), Teacher.class);
                        intentTeacher.putExtra(USERNAME, username);
                        startActivity(intentTeacher);
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                        Intent intentStudent = new Intent(getApplicationContext(), Student.class);
                        intentStudent.putExtra(USERNAME, username);
                        startActivity(intentStudent);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "!Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
