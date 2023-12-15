package com.example.navistick;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH = 2000;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        animation = AnimationUtils.loadAnimation(this, R.anim.animation);

        ImageView logo = findViewById(R.id.splashLogo);
        TextView txtHue = findViewById(R.id.txtHue);
        TextView txtBarcode = findViewById(R.id.txtBarcode);
        logo.setAnimation(animation);
        txtHue.setAnimation(animation);
        txtBarcode.setAnimation(animation);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH);
    }
}