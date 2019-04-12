package com.aplicacion.essalud;

import androidx.appcompat.app.AppCompatActivity;
import pl.droidsonroids.gif.GifImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int SPLASH_TIME_OUT = 4000;
        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.zoomin);
        ((GifImageView) findViewById(R.id.givBot)).startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, ChatBotActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
