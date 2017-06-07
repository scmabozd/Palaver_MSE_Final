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
        Button b = (Button) findViewById(R.id.buttonTest);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity();
            }
        });

        Button b2 = (Button) findViewById(R.id.buttonChatOverview);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                startChatOverviewActivity();
            }
        }   );
    }
    private void startNewActivity() {
<<<<<<< Updated upstream
        Intent i = new Intent(this, LoginActivity.class);
=======
        Intent i = new Intent(this, ChatActivity.class);
>>>>>>> Stashed changes
        startActivity(i);
    }

    private void startChatOverviewActivity() {
        Intent i2 = new Intent(this, chatOverviewActivity.class);
        startActivity(i2);
    }


}
/*
@Override
            public void onClick(View v) {
                startNewActivity();
            }
 */