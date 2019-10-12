package com.example.lab10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lab10.Database.DBHelper;
import com.example.lab10.Model.MessageModel;

public class Message extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        int messageId = intent.getIntExtra(Student.MESSAGE_ID, -1);
        DBHelper dbHelper = new DBHelper(this);
        MessageModel messageModel = dbHelper.readMessage(messageId);
        ((TextView)findViewById(R.id.subject)).setText(messageModel.getSubject());
        ((TextView)findViewById(R.id.user)).setText(messageModel.getUser());
        ((TextView)findViewById(R.id.message)).setText(messageModel.getMessage());
    }

}
