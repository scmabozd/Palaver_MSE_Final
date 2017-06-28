package com.example.ude.palaver_mse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button b = (Button) findViewById(R.id.buttonLogIn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(LoginActivity.class);
            }
        });

        Button b2 = (Button) findViewById(R.id.buttonChatOverview);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(chatOverviewActivity.class);
            }
        }   );
        Button b3 = (Button) findViewById(R.id.buttonRegister);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(RegisterActivity.class);
            }
        }   );


        Button b4 = (Button) findViewById(R.id.buttonChat);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(ChatActivity.class);
            }
        }   );

        Button b5 = (Button) findViewById(R.id.buttonAddContact);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(AddContactActivity.class);
            }
        }   );

        Button b6 = (Button) findViewById(R.id.buttonContacts);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(contacts.class);
            }
        }   );

    }
    private void startNewActivity(Class x) {

        Intent i = new Intent(this, x);
        startActivity(i);
    }
}
