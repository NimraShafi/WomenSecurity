package com.example.womensecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class Splash extends AppCompatActivity {


    private static int splashTimeOut = 5000;
    ImageView logo;
    String logText,email;
    String guardientNum1,guardienNum2,vibrate,alert,call,defaultNumForCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();


        logo = (ImageView) findViewById(R.id.logo);


        SharedPreferences sharedpreferences = getSharedPreferences("RegisterDetails", Context.MODE_PRIVATE);

        email = sharedpreferences.getString("Email","");

        SharedPreferences logout = getSharedPreferences("LogoutData", Context.MODE_PRIVATE);

        logText = logout.getString("logout","");

        SharedPreferences guarPref = getSharedPreferences("Number", Context.MODE_PRIVATE);
        guardientNum1 = guarPref.getString("num1","");
        guardienNum2 = guarPref.getString("num2","");

        SharedPreferences vibrateEnable = getSharedPreferences("Vibrate", Context.MODE_PRIVATE);
        vibrate = vibrateEnable.getString("vibrate","");

        SharedPreferences alertEnable = getSharedPreferences("Alert", Context.MODE_PRIVATE);
        alert = alertEnable.getString("alert","");

        SharedPreferences CallEnable = getSharedPreferences("Call", Context.MODE_PRIVATE);
        call = CallEnable.getString("call","");

        SharedPreferences Default = getSharedPreferences("Default", Context.MODE_PRIVATE);
        defaultNumForCall = Default.getString("Num","");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    if (email.equals(""))
                    {
                        Intent intent = new Intent(Splash.this, Register.class);
                        startActivity(intent);
                    }

                    else if (logText.matches("l" )|| logText.equals("l"))
                    {
                        Intent intent = new Intent(Splash.this, Login.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(Splash.this, MainActivity.class);

                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, splashTimeOut);
        try {
            Animation myanim = AnimationUtils.loadAnimation(this, R.anim.splash);
            logo.startAnimation(myanim);
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);

            return false;
        }
        return true;
    }


}