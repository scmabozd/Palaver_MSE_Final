package com.example.ude.palaver_mse;
/**
 * Created by mbozd on 26.06.2017.
 */
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }





    //new

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


}