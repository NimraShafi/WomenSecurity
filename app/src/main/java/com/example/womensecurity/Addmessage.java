package com.example.womensecurity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Addmessage extends AppCompatActivity {

    AppCompatImageView back_to_home;
    TextInputEditText alert_messg;
    AppCompatButton save;
    LinearLayout messLay;
    String mssage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmessage);

        Objects.requireNonNull(getSupportActionBar()).hide();

        back_to_home = findViewById(R.id.back_to_home);
        alert_messg = findViewById(R.id.alert_messg);
        messLay = findViewById(R.id.messLay);
        save = findViewById(R.id.save);

        SharedPreferences sharedpreferences = getSharedPreferences("AlertMessage", Context.MODE_PRIVATE);

        mssage = sharedpreferences.getString("messg","");

        alert_messg.setText(mssage);

        if (!alert_messg.getText().toString().isEmpty())
        {
            save.setText("Update");
        }

        back_to_home.setOnClickListener(v -> {
            Intent intent = new Intent(Addmessage.this,MainActivity.class);
            startActivity(intent);
        });
        save.setOnClickListener(v -> {
            if (alert_messg.getText().toString().isEmpty())
            {
                Snackbar.make(messLay,"Message box is empaty",Snackbar.LENGTH_SHORT).show();
            }
            else
            {
                new PrefManager
                        (Addmessage.this).Message(alert_messg.getText().toString());

                Snackbar.make(messLay,"Message Added Successfully",Snackbar.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            Snackbar.make(messLay,"Use above back button",Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}