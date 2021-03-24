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
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.EditText;

import static com.example.notificationstutorial.App.CHANNEL_1_ID;
import static com.example.notificationstutorial.App.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity {

    // for compatibility to older devices
    private NotificationManagerCompat notificationManagerC;
    private EditText editTextTitle;
    private EditText editTexMessage;

    private MediaSessionCompat mediaSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManagerC = NotificationManagerCompat.from(this);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTexMessage = findViewById(R.id.edit_text_message);

        mediaSession = new MediaSessionCompat(this, "tag");
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


        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.kemonito);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(picture)
                // style part
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(picture)
                        .bigLargeIcon(null)

                )
                // end of style
                .setPriority(NotificationCompat.PRIORITY_HIGH) // additionally in here for lower api
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.CYAN)
                .setContentIntent(contentIntent)
                .setAutoCancel(true) // dismiss the notification when touched
                .setOnlyAlertOnce(true)
              //  .addAction(R.mipmap.ic_launcher, "Action", actionIntent)
                .build();

        notificationManagerC.notify(id_1, notification);
    }

    public void sendOnChannel2(View view) {
        int id_2 = 2;

        String title = editTextTitle.getText().toString();
        String message = editTexMessage.getText().toString();

        Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.drawable.kemonito);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_monetization_on_24)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(artwork)
                .addAction(R.drawable.hand, "Hi", null)
                .addAction(R.drawable.cycle, "Run", null)
                .addAction(R.drawable.call, "Call", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0,1,2)

                ).setSubText("Sub text")
                .setPriority(NotificationCompat.PRIORITY_LOW) // additionally in here for lower api
                .build();

        notificationManagerC.notify(id_2, notification);
    }
}