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
            public void onClick(View v2) {
                startNewActivity(chatOverviewActivity.class);
            }
        }   );
        Button b3 = (Button) findViewById(R.id.buttonRegister);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                startNewActivity(RegisterActivity.class);
            }
        }   );





    }
    private void startNewActivity(Class x) {

        Intent i = new Intent(this, x);
        startActivity(i);
    }
}
