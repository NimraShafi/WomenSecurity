package com.example.womensecurity;


import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    Context context;

    public PrefManager(Context context)
    {
        this.context = context;
    }


    public void saveRegisterDetails(String name,String email, String password,String phone,String id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("RegisterDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.putString("Phone", phone);
        editor.putString("Id", id);
        editor.apply();
    }

    public void Logout(String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LogoutData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logout", name);
        editor.apply();
    }

    public void Message(String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AlertMessage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("messg", name);
        editor.apply();
    }

    public void GuardientNum(String num1,String  num2 , String num3){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Number", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("num1", num1);
        editor.putString("num2", num2);
        editor.putString("num3", num3);
        editor.apply();
    }

    public void AlertEnable(String alrt){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Alert", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("alert", alrt);
        editor.apply();
    }

    public void VibrateEnable(String vib){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Vibrate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("vibrate", vib);
        editor.apply();
    }

    public void CallEnable(String call){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Call", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("call", call);
        editor.apply();
    }

    public void DefaultNum(String num){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Default", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Num", num);
        editor.apply();
    }


}