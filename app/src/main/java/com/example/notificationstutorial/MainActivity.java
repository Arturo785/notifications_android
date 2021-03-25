package com.example.notificationstutorial;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import static com.example.notificationstutorial.App.CHANNEL_1_ID;
import static com.example.notificationstutorial.App.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity {

    // for compatibility to older devices
    private NotificationManagerCompat notificationManagerC;
    private EditText editTextTitle;
    private EditText editTexMessage;


    static List<Message> MESSAGES = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManagerC = NotificationManagerCompat.from(this);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTexMessage = findViewById(R.id.edit_text_message);


        MESSAGES.add(new Message("Good morning", "Tu"));
        MESSAGES.add(new Message("Hi", null)); // means is mine
        MESSAGES.add(new Message("K onda", "Tu"));
    }

    public void sendOnChannel1(View view){
        if(!notificationManagerC.areNotificationsEnabled()){
            openNotificationSettings();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                isChannelBlocked(CHANNEL_1_ID)) {
            openChannelSettings(CHANNEL_1_ID);
            return;
        }
        
        sendChannel1Notification(this);
    }

    public static void sendChannel1Notification(Context context) {
        int id_1 = 1;
        int requestCode = 0;

        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestCode, activityIntent, 0);

        // notifications are actually handled by Android OS so a pending Intent is necessary
        // to define the rules you are granting it the right to perform the operation you
        // have specified as if the other application was yourself

        // this is for the button
        Intent broadcastIntent = new Intent(context, DirectReplyReceiver.class);
        broadcastIntent.putExtra("toastMessage", "");
        // the flag means that if we create another pending intent it updates the extras of the intent
        PendingIntent actionIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);


      //  Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.kemonito);

        RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                .setLabel("Your answer...")
                .build();

        Intent replyIntent;
        PendingIntent replyPendingIntent = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            replyIntent = new Intent(context, DirectReplyReceiver.class);
            replyPendingIntent = PendingIntent.getBroadcast(context, 0, replyIntent, 0);
        }else{
            // start chat activity with pending intent
            // cancel notification with notificationManagerCompat.cancel(id)
        }

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_baseline_monetization_on_24,
                "Reply",
                replyPendingIntent
        ).addRemoteInput(remoteInput).build();

        Person.Builder builder = new Person.Builder().setName("Me");

        NotificationCompat.MessagingStyle messagingStyle =
                new NotificationCompat.MessagingStyle(builder.build());

        messagingStyle.setConversationTitle("Group chat");

        for(Message chatMessage : MESSAGES){
            NotificationCompat.MessagingStyle.Message notificationMessage =
                    new NotificationCompat.MessagingStyle.Message(
                            chatMessage.getText(),
                            chatMessage.getTimestamp(),
                            new Person.Builder().setName(chatMessage.getSender()).build()
                    );
            messagingStyle.addMessage(notificationMessage);
        }


        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
              //  .setContentTitle(title)
              //  .setContentText(message)
            //    .setLargeIcon(picture)
                // style part
                .setStyle(messagingStyle)
                .addAction(replyAction)
                // end of style
                .setPriority(NotificationCompat.PRIORITY_HIGH) // additionally in here for lower api
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.CYAN)
                .setContentIntent(contentIntent)
                .setAutoCancel(true) // dismiss the notification when touched
                .setOnlyAlertOnce(true)
              //  .addAction(R.mipmap.ic_launcher, "Action", actionIntent)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id_1, notification);
    }

    public void sendOnChannel2(View view) {
        final int id_2 = 2;
        List<String> titles = new ArrayList<>();
        List<String> messages = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            titles.add("title: " + i);
            messages.add("message : " +i);
        }

        final Notification notification1 = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_monetization_on_24)
                .setContentTitle("Tittle 1")
                .setContentText("Message 1")
                .setPriority(NotificationCompat.PRIORITY_LOW) // additionally in here for lower api
                .setGroup("example group")
                .build();

        final Notification notification2 = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_monetization_on_24)
                .setContentTitle("Tittle 2")
                .setContentText("Message 2")
                .setPriority(NotificationCompat.PRIORITY_LOW) // additionally in here for lower api
                .setGroup("example group")
                .build();

        final Notification summaryNotification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_monetization_on_24)
                .setStyle(new NotificationCompat.InboxStyle()
                    .addLine("title2 message2 ")
                    .addLine("title1 message1 ")
                    .setBigContentTitle("2 new messages")
                    .setSummaryText("user@example.com")
                )
                .setPriority(NotificationCompat.PRIORITY_LOW) // additionally in here for lower api
                .setGroup("example group")
                .setGroupSummary(true)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .build();


        SystemClock.sleep(2000);
        notificationManagerC.notify(id_2, notification1);
        SystemClock.sleep(2000);
        notificationManagerC.notify(4, notification2);
        SystemClock.sleep(2000);
        notificationManagerC.notify(5, summaryNotification);


    }


    private void openNotificationSettings() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    @RequiresApi(26)
    private boolean isChannelBlocked(String channelId){
        NotificationManager manager = getSystemService(NotificationManager.class);
        NotificationChannel channel = manager.getNotificationChannel(channelId);

        return channel != null &&
                channel.getImportance() == NotificationManager.IMPORTANCE_NONE; // is disabled
    }

    @RequiresApi(26)
    private void openChannelSettings(String channelId){
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);

        startActivity(intent);
    }
}