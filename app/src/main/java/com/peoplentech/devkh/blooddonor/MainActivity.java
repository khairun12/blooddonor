package com.peoplentech.devkh.blooddonor;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    //Button find_donar, find_bank, login, register;

    private Animation slideAnimationLeft;
    //private static int SPLASH_TIME_OUT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*login = (Button)findViewById(R.id.log_btn);
        register = (Button)findViewById(R.id.reg_btn);
        find_donar = (Button)findViewById(R.id.blooddonar_btn);*/

        final ViewGroup transition = (ViewGroup) findViewById(R.id.layout1);

        final Button find_donar = (Button) transition.findViewById(R.id.blooddonar_btn);
        final Button register = (Button) transition.findViewById(R.id.reg_btn);
        final Button login = (Button) transition.findViewById(R.id.log_btn);
        final Button find_bank = (Button) transition.findViewById(R.id.bank_btn);

        slideAnimationLeft = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_left);

        find_donar.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        find_bank.setVisibility(View.VISIBLE);

        find_donar.startAnimation(slideAnimationLeft);
        register.startAnimation(slideAnimationLeft);

        login.startAnimation(slideAnimationLeft);

        find_bank.startAnimation(slideAnimationLeft);


        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //animation

                TransitionManager.beginDelayedTransition(transition);
                find_donar.setVisibility(View.VISIBLE);
                register.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                find_bank.setVisibility(View.VISIBLE);
            }
        }, SPLASH_TIME_OUT);*/

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        find_donar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchDonorActivity.class);
                startActivity(intent);
            }
        });

        find_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }
}
