package com.example.womensecurity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class LearnUses extends AppCompatActivity {

    AppCompatImageView back_to_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_uses);

        getSupportActionBar().hide();

        back_to_home = findViewById(R.id.back_to_home);

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearnUses.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
//            Snackbar.make(messLay,"Use above back button",Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}