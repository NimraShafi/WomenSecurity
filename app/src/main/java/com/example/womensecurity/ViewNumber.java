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

public class ViewNumber extends AppCompatActivity {

    AppCompatImageView back_to_home;

    String phoneNumber;
    TextInputEditText my_pno,guardien1_pno,guardien2_pno,police_pno;
    AppCompatButton updateBtn;
    String num1,num2,num3;
    LinearLayout numLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_number);
        getSupportActionBar().hide();

        numLay = findViewById(R.id.numLay);
        back_to_home = findViewById(R.id.back_to_home);
        my_pno = findViewById(R.id.my_pno);
        guardien1_pno = findViewById(R.id.guardien1_pno);
        guardien2_pno = findViewById(R.id.guardien2_pno);
        police_pno = findViewById(R.id.police_pno);
        updateBtn = findViewById(R.id.updateBtn);

        SharedPreferences sharedpreferences = getSharedPreferences("RegisterDetails", Context.MODE_PRIVATE);

        phoneNumber = sharedpreferences.getString("Phone","");

        SharedPreferences numPref = getSharedPreferences("Number", Context.MODE_PRIVATE);

        num1 = numPref.getString("num1","");
        num2 = numPref.getString("num2","");
        num3 = numPref.getString("num3","");
        guardien1_pno.setText(num1);
        guardien2_pno.setText(num2);

        police_pno.setText(num3);
        my_pno.setText(phoneNumber);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guardien1_pno.getText().toString().isEmpty())
                {
                    Snackbar.make(numLay, "Please Enter Guardian1 Number ", Snackbar.LENGTH_SHORT).show();
                }
               else if (guardien2_pno.getText().toString().isEmpty())
                {
                    Snackbar.make(numLay, "Please Enter Guardian2 Number ", Snackbar.LENGTH_SHORT).show();
                }
                else if (police_pno.getText().toString().isEmpty())
                {
                    Snackbar.make(numLay, "Please Enter Police Number ", Snackbar.LENGTH_SHORT).show();
                }
               else
                {
                    if (guardien1_pno.getText().toString().matches(num1) && guardien2_pno.getText().toString().matches(num2)&& police_pno.getText().toString().matches(num3))
                    {
                        Snackbar.make(numLay, "Already Updated", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        new PrefManager
                                (ViewNumber.this).GuardientNum(guardien1_pno.getText().toString(),guardien2_pno.getText().toString(),police_pno.getText().toString());
                        Snackbar.make(numLay, "Updated Successfully", Snackbar.LENGTH_SHORT).show();
                    }


                }
            }
        });

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewNumber.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            Snackbar.make(numLay,"Use above back button",Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}