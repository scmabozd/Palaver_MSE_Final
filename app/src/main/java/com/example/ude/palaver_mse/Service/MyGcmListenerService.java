package com.example.ude.palaver_mse.Service;

import com.example.ude.palaver_mse.App;
import com.example.ude.palaver_mse.AppController;
import com.example.ude.palaver_mse.ChatActivity;
import com.example.ude.palaver_mse.LoginActivity;
import com.example.ude.palaver_mse.MainActivity;
import com.example.ude.palaver_mse.R;
import com.example.ude.palaver_mse.contacts;
import com.google.android.gms.gcm.GcmListenerService;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Moritz on 02.07.2017.
 */

public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message, data);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, Bundle data) {

        AppController ac = (AppController) getApplication();
        ac.setContext(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        if( sp.getString("Eingeloggt", null).equals("ja")) {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("friend", data.getString("sender"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);


            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_dialog_email)
                    .setContentTitle("Neue Palaver-Nachricht von " + data.getString("sender"))
                    .setContentText(data.getString("preview"))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            int id = ac.notificationId++;
            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());

            Intent i = new Intent("neueNachricht" + data.getString("sender"));
            sendBroadcast(i);
        }else{


            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_dialog_email)
                    .setContentTitle("Neue Palaver-Nachricht von " + data.getString("sender"))
                    .setContentText("Melden Sie sich an, um die Nachricht lesen zu können")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);
                  //  .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            int id = ac.notificationId++;
            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());

            Intent i = new Intent("gotoNachricht" + data.getString("sender"));
            sendBroadcast(i);
        }

    }
}
