package com.example.ude.palaver_mse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
//import info.androidhive.volleyjson.R;
//import info.androidhive.volleyjson.app.AppController;
import static com.example.ude.palaver_mse.R.id.info;


public class AddContactActivity extends AppCompatActivity {


    private EditText userEingabe;
    private EditText pwdEingabe;
    private String username;
    private String pwd;
    private ProgressDialog pDialog;
    AppController palaver;

    // temporary string to show the parsed response

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
        final String url = "http://palaver.se.paluno.uni-due.de/api/friends/add";


        final JSONObject params = new JSONObject();
        try {
            params.put("Username", palaver.getUsername());
            params.put("Password", palaver.getPassword());
            params.put("Friend", userEingabe.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequestAddFreund = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(String.valueOf(params)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", String.valueOf(response) +  String.valueOf(params));
                        hidepDialog();
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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}