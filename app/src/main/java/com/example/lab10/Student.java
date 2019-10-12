package com.example.lab10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.lab10.Database.ClassApp;
import com.example.lab10.Database.DBHelper;
import com.example.lab10.Model.MessageModel;

import java.util.Objects;

public class Student extends AppCompatActivity {

    String username;
    public static final String MESSAGE_ID = "m_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent();
        username = intent.getStringExtra(MainActivity.USERNAME);
        ((TextView)findViewById(R.id.welcome)).setText(String.format("Welcome %s", username));

        DBHelper dbHelper = new DBHelper(this);
        String[] fromColumns = {ClassApp.Message._ID, ClassApp.Message.COLUMN_NAME_USER, ClassApp.Message.COLUMN_NAME_SUBJECT, ClassApp.Message.COLUMN_NAME_MESSAGE};
        int[] toViews = {R.id.id, R.id.user, R.id.subject, R.id.message};
        final ListView listView = findViewById(R.id.listView);
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.item_message, dbHelper.getMessagesCursor(),
                fromColumns, toViews, 0);
        listView.setAdapter(simpleCursorAdapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                Intent intent1 = new Intent(getApplicationContext(), Message.class);
                intent1.putExtra(MESSAGE_ID, cursor.getInt(cursor.getColumnIndexOrThrow(ClassApp.Message._ID)));
                startActivity(intent1);
            }
        });

        // ***************** Notification *****************

        MessageModel messageModel = dbHelper.getLastMessage();
        String title;
        String body;
        if (messageModel == null) {
            title = "No messages";
            body = "Check In later";
        } else {
            title = messageModel.getSubject();
            body = messageModel.getMessage() + " ~" + messageModel.getUser();
        }

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CourseApp";
            String description = "Notification for a student with the latest message";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

        // Create an explicit intent for an Activity in your app
        Intent intentNotif = new Intent(this, Message.class);
        intentNotif.putExtra(MESSAGE_ID, messageModel != null ? messageModel.getId() : 1);
        intentNotif.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentNotif, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.search_bot)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(123, builder.build());
    }
}
