package com.example.lab10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab10.Database.DBHelper;

public class Teacher extends AppCompatActivity {

    EditText subject;
    EditText message;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        Intent intent = getIntent();
        username = intent.getStringExtra(MainActivity.USERNAME);

        ((TextView)findViewById(R.id.welcome)).setText(String.format("Welcome %s", username));

        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
    }

    public void sendMessage(View view) {
        if ("".equals(subject.getText().toString()) || "".equals(message.getText().toString())){
            Toast.makeText(this, "Don't keep anything blank :)", Toast.LENGTH_SHORT).show();
        } else {
            DBHelper dbHelper = new DBHelper(this);
            if (dbHelper.insertMessage(username, subject.getText().toString(), message.getText().toString())) {
                Toast.makeText(this, "Message successfully sent", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "!Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
