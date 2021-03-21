package com.example.notificationstutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
        int requestCode = 0;

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, requestCode, activityIntent, 0);

        // notifications are actually handled by Android OS so a pending Intent is necessary
        // to define the rules you are granting it the right to perform the operation you
        // have specified as if the other application was yourself

        // this is for the button
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        // the flag means that if we create another pending intent it updates the extras of the intent
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.kemonito);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                // style part
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.long_dummy_text))
                        .setBigContentTitle("Big content title")
                        .setSummaryText("Summary text"))
                // end of style
                .setPriority(NotificationCompat.PRIORITY_HIGH) // additionally in here for lower api
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.CYAN)
                .setContentIntent(contentIntent)
                .setAutoCancel(true) // dismiss the notification when touched
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Action", actionIntent)
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
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("This is line 1")
                        .addLine("This is line 2")
                        .addLine("This is line 3")
                        .addLine("This is line 4")
                        .addLine("This is line 5")
                        .addLine("This is line 6")
                        .setBigContentTitle("Big content title")
                        .setSummaryText("Summary text"))
                .setPriority(NotificationCompat.PRIORITY_LOW) // additionally in here for lower api
                .build();

        notificationManagerC.notify(id_2, notification);
    }
}