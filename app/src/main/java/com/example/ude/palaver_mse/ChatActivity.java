package com.example.ude.palaver_mse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private ChatArrayAdapter adp;
    private ListView list;
    private EditText chatText;
    private Button send;
    private FloatingActionButton refreshChat;
    private ProgressDialog pDialog;
    private boolean side = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        send = (Button) findViewById(R.id.btn);
        refreshChat = (FloatingActionButton) findViewById(R.id.floatingActionButtonRefreshChat);
        list = (ListView) findViewById(R.id.listview);
        adp = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_window);
        list.setAdapter(adp);
        chatText = (EditText) findViewById(R.id.chat_text);

        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode ==
                        KeyEvent.KEYCODE_ENTER)) {
                    try {
                        sendMessage();
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        refreshChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    refreshChat();
                    Toast.makeText(ChatActivity.this, "Chat aktualisiert", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    sendMessage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(adp);
        adp.registerDataSetObserver(new DataSetObserver() {
            public void OnChanged(){
                super.onChanged();
                list.setSelection(adp.getCount() -1);
            }
        });

        try {
            refreshChat();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private boolean sendChatMessage() {
        adp.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = side;
        return true;
    }

    private void refreshChat() throws JSONException {
        final String url = "http://palaver.se.paluno.uni-due.de/api/message/get";
        pDialog = ProgressDialog.show(this, "Lade Chat", "Einen Moment...", false);
        adp = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_window);
        list.setAdapter(adp);
        final Context c = this;

        final JSONObject params = new JSONObject();
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
            SharedPreferences.Editor editor = sp.edit();
            String username = sp.getString("Username", null);
            String pwd = sp.getString("Password",null);
            String friend;
            friend = getIntent().getExtras().getString("friend");

            params.put("Username", username);
            params.put("Password", pwd);
            params.put("Recipient", friend);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequestGetKonversation = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(String.valueOf(params)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
                        try {
                            String msgType = response.getString("MsgType");
                            String information = response.getString("Info");
                            String time = response.getString("Data");

                            if (msgType.equals("1")) {
                                Log.d("Response", String.valueOf(response) + "\nGesendet wurde: " + String.valueOf(params));
                                Log.d("Active user #####", sp.getString("Username", null));
                                //JSONObject o = response.getJSONArray("Data").getJSONObject(0);
                                //Log.d("Recipient", o.getString("Recipient"));
                                for (int i = 0; i < response.getJSONArray("Data").length(); i++) {
                                    JSONObject o = response.getJSONArray("Data").getJSONObject(i);
                                    if(o.getString("Recipient").equals(getIntent().getExtras().getString("friend")))
                                        adp.add(new ChatMessage(side, o.getString("Data")));
                                    else
                                        adp.add(new ChatMessage(!side, o.getString("Data")));

                                }
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
        pDialog.hide();
        AppController.getInstance().addToRequestQueue(postRequestGetKonversation);
    }


    //new method
    private void sendMessage() throws JSONException {
        final String url = "http://palaver.se.paluno.uni-due.de/api/message/send";

        final Context c = this;

        final JSONObject params = new JSONObject();
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
            SharedPreferences.Editor editor = sp.edit();
            String username = sp.getString("Username", null);
            String pwd = sp.getString("Password",null);
            String friend;
            friend = getIntent().getExtras().getString("friend");


            params.put("Username", username);
            params.put("Password", pwd);
            params.put("Recipient", friend);
            params.put("Mimetype", "text/plain");
            params.put("Data", chatText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequestSchickeNachricht = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(String.valueOf(params)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
                        try {
                            String msgType = response.getString("MsgType");
                            String information = response.getString("Info");
                            String time = response.getString("Data");

                            if (msgType.equals("1")) {
                                Log.d("Response", String.valueOf(response) + "\nGesendet wurde: " + String.valueOf(params));
                                Log.d("Active user #####", sp.getString("Username", null));
                                adp.add(new ChatMessage(side, chatText.getText().toString()));
                                chatText.setText("");
                                side = side;
                            }else{
                                Log.d("########", sp.getString("Username", null));
                                Log.d("Response", String.valueOf(response) + String.valueOf(params));}
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
        AppController.getInstance().addToRequestQueue(postRequestSchickeNachricht);
    }
}