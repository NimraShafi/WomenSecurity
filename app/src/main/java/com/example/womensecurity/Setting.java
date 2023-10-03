package com.example.womensecurity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

public class Setting extends AppCompatActivity {

    LinearLayout settLay,numLay;
    SwitchCompat enAlert,vibAlert,callAlert;
    CheckBox checkBoxA, checkBoxB, checkBoxC;
    AppCompatButton set;
    String falge;
    String num,vibrate,alert,res;
    AppCompatImageView back_to_home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().hide();

        hooks();
        numLay.setVisibility(View.INVISIBLE);

        // Get Default Num
        SharedPreferences defNum = getSharedPreferences("Default", Context.MODE_PRIVATE);

        res = defNum.getString("Num","");
        if (res.matches("a"))
        {
            checkBoxA.setChecked(true);
        }

       else if (res.matches("b"))
        {
            checkBoxB.setChecked(true);
        }
       else if (res.matches("c"))
        {
            checkBoxC.setChecked(true);
        }
       else
        {
            checkBoxC.setChecked(false);
            checkBoxB.setChecked(false);
            checkBoxA.setChecked(false);
        }

        // Get Call
        SharedPreferences sharedpreferences = getSharedPreferences("Call", Context.MODE_PRIVATE);

        num = sharedpreferences.getString("call","");


        if (num.matches("call"))
        {
            callAlert.setChecked(true);
            numLay.setVisibility(View.VISIBLE);

        }
        else {
            callAlert.setChecked(false);
            numLay.setVisibility(View.INVISIBLE);

        }
            // Get Vibration Enable
        SharedPreferences vib = getSharedPreferences("Vibrate", Context.MODE_PRIVATE);

        vibrate = vib.getString("vibrate","");

        if (vibrate.matches("vib"))
        {
            vibAlert.setChecked(true);
        }
        else
        {
            vibAlert.setChecked(false);
        }
        // Get Enable Alert
            SharedPreferences alertEnable = getSharedPreferences("Alert", Context.MODE_PRIVATE);

            alert = alertEnable.getString("alert","");

            if (alert.matches("alert"))
            {
                enAlert.setChecked(true);
            }
            else
            {
                enAlert.setChecked(false);
            }

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this,MainActivity.class);

                startActivity(intent);
            }
        });
        vibAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {

                    new PrefManager
                            (Setting.this).VibrateEnable("vib");
                }
                else
                {
                    new PrefManager
                            (Setting.this).VibrateEnable("");
                }
            }
        });
        enAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {

                    new PrefManager
                            (Setting.this).AlertEnable("alert");
                }
                else
                {
                    new PrefManager
                            (Setting.this).AlertEnable("");
                }
            }
        });
        callAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    numLay.setVisibility(View.VISIBLE);

                    new PrefManager
                            (Setting.this).CallEnable("call");
                }
                else
                {
                    numLay.setVisibility(View.INVISIBLE);
                    new PrefManager
                            (Setting.this).CallEnable("");
                }
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (falge == "")
                {
                    Snackbar.make(settLay, "Please Select one Option", Snackbar.LENGTH_LONG)
                            .setAction("ACTION",null).show();
                }
                else
                {

                    new PrefManager
                            (Setting.this).DefaultNum(falge);

                    falge = "";
                    Snackbar.make(settLay, "Successfully done", Snackbar.LENGTH_LONG)
                            .setAction("ACTION",null).show();
                }
            }
        });
    }
    public void onCheckboxClicked(View view) {

        switch(view.getId()) {

            case R.id.checkBoxA:
                falge = "a";
                checkBoxB.setChecked(false);
                checkBoxC.setChecked(false);

                break;

            case R.id.checkBoxB:
                falge = "b";
                checkBoxC.setChecked(false);
                checkBoxA.setChecked(false);

                break;

            case R.id.checkBoxC:
                falge = "c";
                checkBoxA.setChecked(false);
                checkBoxB.setChecked(false);

                break;
        }
    }
    private void hooks() {
        back_to_home = findViewById(R.id.back_to_home);
        numLay = findViewById(R.id.numLay);
        set = findViewById(R.id.set);
        settLay = findViewById(R.id.settLay);
        enAlert = findViewById(R.id.enAlert);
        vibAlert = findViewById(R.id.vibAlert);
        callAlert = findViewById(R.id.callAlert);
        checkBoxA = (CheckBox) findViewById(R.id.checkBoxA);
        checkBoxB = (CheckBox) findViewById(R.id.checkBoxB);
        checkBoxC = (CheckBox) findViewById(R.id.checkBoxC);
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            Snackbar.make(settLay,"Use above back button",Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}