package com.example.ude.palaver_mse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.CancelledKeyException;
import java.util.ArrayList;
import java.util.List;

public class contacts extends AppCompatActivity {
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
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
        final Context c = this;

        findViewById(R.id.floatingActionButtonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.putString("Username",null);
                editor.putString("Password",null);
                editor.commit();
                startActivity(new Intent(contacts.this, LoginActivity.class));
            }
        });
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
}
