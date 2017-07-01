package com.example.ude.palaver_mse;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splashActivity extends Activity {
    String eingeloggt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setBackgroundColor(Color.parseColor("#FF7EBF30"));

        ImageView v;
        v= (ImageView) findViewById(R.id.imageView);
        final Animation an = AnimationUtils.loadAnimation(this, R.anim.rotate);

        v.startAnimation(an);
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(splashActivity.this);
        SharedPreferences.Editor editor = sp.edit();
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                eingeloggt = sp.getString("Eingeloggt",null);
                Log.d("Eingeloggt",eingeloggt);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                if (eingeloggt.equals("ja"))
                {
                    Intent i = new Intent(getBaseContext(),contacts.class);
                    startActivity(i);

                }else{
                    Intent i = new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
