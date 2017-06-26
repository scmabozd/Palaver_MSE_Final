package com.example.ude.palaver_mse;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
/**
 * Created by mbozd on 26.06.2017.
 */

public class App extends Application {

    private SharedPreferences prefs;
    public int notificationId = 1;

    public void setContext(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("palaver", MODE_PRIVATE);
    }

    private Context context;

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public void setPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public String getUsername() {
        return prefs.getString("username", null);
    }

    public void setUsername(String username) {
        SharedPreferences.Editor e = prefs.edit();
        e.putString("username", username);
        e.commit();
    }

    public String getPassword() {
        return prefs.getString("password", null);
    }

    public void setPassword(String password) {
        SharedPreferences.Editor e = prefs.edit();
        e.putString("password", password);
        e.commit();
    }

    public void sendTokenToServer(String token) {
    //TODO
   }


}
