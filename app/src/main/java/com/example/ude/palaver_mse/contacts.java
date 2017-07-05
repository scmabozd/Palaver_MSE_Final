package com.example.ude.palaver_mse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ude.palaver_mse.Service.QuickstartPreferences;
import com.example.ude.palaver_mse.Service.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.CancelledKeyException;
import java.util.ArrayList;
import java.util.List;

public class contacts extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    ProgressDialog pDialog;
    AlertDialog deleteDialog;
    ProgressDialog progressDialog;

    //GCM
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //GCM
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

            }
        };
        // Registering BroadcastReceiver
        registerReceiver();
        if (checkPlayServices()) {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            // Start IntentService to register this application with GCM.
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        try {
            getRequestFriends();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((ListView)findViewById(R.id.contactsList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(contacts.this, ChatActivity.class).putExtra("friend", adapter.getItem(position)));
            }
        });

        ((ListView)findViewById(R.id.contactsList)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(contacts.this);
                adb.setTitle("Löschen");
                adb.setMessage("Wollen Sie " + adapter.getItem(position) + " von der Kontaktliste entfernen?");
                adb.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog = ProgressDialog.show(contacts.this, "Entferne Kontakt", "Einen Moment...", false);
                        try {
                            kontaktEntfernen(adapter.getItem(position).toString());
                            getRequestFriends();
                            progressDialog.hide();
                            Toast.makeText(contacts.this, "Kontakt gelöscht", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                adb.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDialog.hide();
                    }
                });
                deleteDialog = adb.show();
                return true;
            }
        });

        findViewById(R.id.floatingActionButtonLogout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(contacts.this, MainActivity.class));
                return false;
            }
        });

        final Context c = this;

        findViewById(R.id.floatingActionButtonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("alterUser", sp.getString("Username", null));
                //editor.clear();
                editor.putString("Username",null);
                editor.putString("Password",null);
                editor.putString("Eingeloggt", "nein");
                editor.putString("Switch", "x");
                editor.commit();
                finish();
                startActivity(new Intent(contacts.this, LoginActivity.class));
            }
        });
        findViewById(R.id.floatingActionButtonAddContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(contacts.this, AddContactActivity.class));
            }
        });
        findViewById(R.id.floatingActionButtonRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showpDialog();
                    getRequestFriends();
                    Toast.makeText(contacts.this, "Kontaktliste aktualisiert", Toast.LENGTH_SHORT).show();
                    hidepDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void kontaktEntfernen(String kontakt) throws JSONException {
        final String url = "http://palaver.se.paluno.uni-due.de/api/friends/remove";

        final Context c = this;

        final JSONObject params = new JSONObject();
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
            SharedPreferences.Editor editor = sp.edit();
            String username = sp.getString("Username", null);
            String pwd = sp.getString("Password", null);
            params.put("Username", username);
            params.put("Password", pwd);
            params.put("Friend", kontakt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject[] k = {new JSONObject()};

        JsonObjectRequest postRequestAddFreund = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(String.valueOf(params)),
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
                                refreshFriends(response);
                                getRequestFriends();
                            }else{
                                Log.d("########", sp.getString("Username", null));
                                Log.d("Response", String.valueOf(response) + String.valueOf(params));
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
        AppController.getInstance().addToRequestQueue(postRequestAddFreund);
    }

    private void getRequestFriends() throws JSONException {
        final String url = "http://palaver.se.paluno.uni-due.de/api/friends/get";

        final Context c = this;

        final JSONObject params = new JSONObject();
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
            SharedPreferences.Editor editor = sp.edit();
            String username = sp.getString("Username", null);
            String pwd = sp.getString("Password", null);
            params.put("Username", username);
            params.put("Password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject[] k = {new JSONObject()};

        JsonObjectRequest postRequestAddFreund = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(String.valueOf(params)),
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
                                refreshFriends(response);
                            }else{
                                Log.d("########", sp.getString("Username", null));
                                Log.d("Response", String.valueOf(response) + String.valueOf(params));
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
        AppController.getInstance().addToRequestQueue(postRequestAddFreund);
    }

    public void showFriends(List<String> items) {
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
        ((ListView)findViewById(R.id.contactsList)).setAdapter(adapter);
    }

    public void refreshFriends(JSONObject json) {
        try {
            ArrayList<String> items = new ArrayList<>();
            for(int i = 0; i < json.getJSONArray("Data").length(); i++) {
                items.add(json.getJSONArray("Data").getString(i));
            }
            showFriends(items);
        } catch(Exception e) {e.printStackTrace();}
    }

    public void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //GCM
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}
