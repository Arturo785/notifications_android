package com.example.notificationstutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.notificationstutorial.App.CHANNEL_1_ID;
import static com.example.notificationstutorial.App.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity {

    // for compatibility to older devices
    private NotificationManagerCompat notificationManagerC;
    private EditText editTextTitle;
    private EditText editTexMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManagerC = NotificationManagerCompat.from(this);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTexMessage = findViewById(R.id.edit_text_message);
    }

    public void sendOnChannel1(View view) {
        int id_1 = 1;

        String title = editTextTitle.getText().toString();
        String message = editTexMessage.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // additionally in here for lower api
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerC.notify(id_1, notification);
    }

    public void sendOnChannel2(View view) {
        int id_2 = 2;

        String title = editTextTitle.getText().toString();
        String message = editTexMessage.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_monetization_on_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW) // additionally in here for lower api
                .build();

        notificationManagerC.notify(id_2, notification);
    }
}