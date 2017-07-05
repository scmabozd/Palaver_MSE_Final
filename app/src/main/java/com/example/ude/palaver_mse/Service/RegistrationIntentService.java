package com.example.ude.palaver_mse.Service;

import android.app.AlertDialog;
import android.app.IntentService;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ude.palaver_mse.AddContactActivity;
import com.example.ude.palaver_mse.AppController;
import com.example.ude.palaver_mse.R;
import com.example.ude.palaver_mse.contacts;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Moritz on 02.07.2017.
 */

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    protected boolean neueID(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String s = sharedPreferences.getString("Switch", null);
        boolean result = Boolean.parseBoolean(null);
        try {
            if (sharedPreferences.getString("Switch", null).equals("x"))
                Log.d("AAAAAAAA",sharedPreferences.getString("Switch", null));
                result =  true;
        }catch(Exception e){
            result = false;
        }
        return result;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]


            InstanceID instanceID = InstanceID.getInstance(this);

            if (neueID()){
                try{if(!(sharedPreferences.getString("alterUser", null).equals(sharedPreferences.getString("Username", null))))
                try{
                    instanceID.deleteInstanceID();
                }catch(IOException e){
                    e.printStackTrace();
                }}catch (Exception e){
                }
            }
            instanceID = InstanceID.getInstance(this);
            //String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
            //        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            String token = instanceID.getToken("594324547505", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) throws JSONException {
        // Add custom implementation, as needed.
        //Uebungs-App:
       // new NetworkHelper(null).execute(NetworkHelper.ApiCommand.USER_PUSHTOKEN.toString(), "{\"Username\":\"" + getUsername() + "\",\"Password\":\"" + getPassword() + "\",\"PushToken\":\"" + token + "\"}");

            //showpDialog();
            View focusView = null;

            final String url = "http://palaver.se.paluno.uni-due.de/api/user/pushtoken";

            final Context c = this;

            final JSONObject params = new JSONObject();
            try {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
                SharedPreferences.Editor editor = sp.edit();
                String username = sp.getString("Username", null);
                String pwd = sp.getString("Password", null);
                params.put("Username", username);
                params.put("Password", pwd);
                params.put("PushToken", token);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
            SharedPreferences.Editor editor = sp.edit();
            Log.d("test",   "; " + sp.getString("Username", null));

                JsonObjectRequest postRequestsendRegistration = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(String.valueOf(params)),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
                                try {
                                    String msgType = response.getString("MsgType");
                                    String information = response.getString("Info");
                                    String data = response.getString("Data");

                                    if (msgType.equals("1")) {
                                        Log.d("Response", String.valueOf(response) + String.valueOf(params));
                                        Log.d("########", sp.getString("Username", null));
                                      //  hidepDialog();
                                        //final AlertDialog.Builder adb = new AlertDialog.Builder(AddContactActivity.this);
                                        //adb.setTitle("Kontakt hinzugefügt");
                                        //adb.setMessage("Kontakt " + userEingabe.getText().toString() + " wurde erfolgreich hinzugefügt");

                                        //adb.show();

                                    }else if (information.equals("Freund bereits auf der Liste")){
                                        Log.d("########", sp.getString("Username", null));
                                        Log.d("Response", String.valueOf(response) + String.valueOf(params));
                                        //userEingabe.setError("Benutzer ist bereits auf der Kontaktliste");
                                       // hidepDialog();
                                    }else{
                                        Log.d("########", sp.getString("Username", null));
                                        Log.d("Response", String.valueOf(response) + String.valueOf(params));
                                        //userEingabe.setError("Benutzer existiert nicht");
                                        //hidepDialog();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", url);
                            }
                        }
                ) {
                };
                AppController.getInstance().addToRequestQueue(postRequestsendRegistration);

    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}
