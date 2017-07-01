package com.example.ude.palaver_mse;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
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

import java.util.ResourceBundle;

public class splashActivity extends Activity {
    String eingeloggt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setBackgroundColor(Color.parseColor("#FF7EBF30"));

        ImageView v;
        v= (ImageView) findViewById(R.id.imageView);
        final Animation an = AnimationUtils.loadAnimation(this, R.anim.rotate);

        v.startAnimation(an);

        Context c = this;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sp.edit();
        boolean initial = Boolean.parseBoolean(null);

        try {
            eingeloggt = sp.getString("Eingeloggt",null);
            if (eingeloggt.equals("ja") || (eingeloggt.equals("ja")))
                initial = false;
            Log.d("initial in TRY/catch", String.valueOf(initial));


        }catch ( Exception e){
            initial = true;
            Log.d("initial in try/CATCH", String.valueOf(initial));
        }
        if (initial){
            editor.putString("Eingeloggt", "nein");
            editor.apply();
            Log.d("Eingeloggt initial=t", sp.getString("Eingeloggt",null));

        }else if (!initial){
            editor.putString("Eingeloggt",  sp.getString("Eingeloggt",null));
            editor.apply();
            Log.d("Eingeloggt initial=f", sp.getString("Eingeloggt",null));

        }

        Log.d("Eingeloggt out IfElse", sp.getString("Eingeloggt",null));
        eingeloggt = sp.getString("Eingeloggt",null);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Log.d("Eingeloggt", eingeloggt);
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
