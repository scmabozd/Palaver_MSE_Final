package com.example.ude.palaver_mse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class AddContactActivity extends AppCompatActivity {


    private EditText userEingabe;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Button b = (Button) findViewById(R.id.button);
        userEingabe = (EditText) findViewById(R.id.editText);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    addUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addUser() throws JSONException {
        showpDialog();
        View focusView = null;
        focusView = userEingabe;
        final String url = "http://palaver.se.paluno.uni-due.de/api/friends/add";

        final Context c = this;

        final JSONObject params = new JSONObject();
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
            SharedPreferences.Editor editor = sp.edit();
            String username = sp.getString("Username", null);
            String pwd = sp.getString("Password", null);
            params.put("Username", username);
            params.put("Password", pwd);
            params.put("Friend", userEingabe.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sp.edit();
        Log.d("test", userEingabe.getText().toString() + "; " + sp.getString("Username", null));
        if (userEingabe.getText().toString().equals(sp.getString("Username", null))) {
            userEingabe.setError("Sie können sich selbst als Kontakt nicht hinzufügen");
                hidepDialog();
            }
        else{
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
                                hidepDialog();
                                final AlertDialog.Builder adb = new AlertDialog.Builder(AddContactActivity.this);
                                adb.setTitle("Kontakt hinzugefügt");
                                adb.setMessage("Kontakt " + userEingabe.getText().toString() + " wurde erfolgreich hinzugefügt");
                                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(AddContactActivity.this, contacts.class));
                                    }
                                });
                                adb.show();

                            }else if (information.equals("Freund bereits auf der Liste")){
                                Log.d("########", sp.getString("Username", null));
                                Log.d("Response", String.valueOf(response) + String.valueOf(params));
                                userEingabe.setError("Benutzer ist bereits auf der Kontaktliste");
                                hidepDialog();
                            }else{
                                Log.d("########", sp.getString("Username", null));
                                Log.d("Response", String.valueOf(response) + String.valueOf(params));
                                userEingabe.setError("Benutzer existiert nicht");
                                hidepDialog();
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
        AppController.getInstance().addToRequestQueue(postRequestAddFreund);}
    }
    public void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}