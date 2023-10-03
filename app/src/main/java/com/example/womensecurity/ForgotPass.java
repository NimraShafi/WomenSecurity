package com.example.womensecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ForgotPass extends AppCompatActivity {

    TextInputEditText for_email;
    FirebaseAuth auth;
    AppCompatButton send;
    AppCompatImageView back;
    String emailPat = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ConstraintLayout startedRelative;
    AlertDialog alertDialog;
    CheckInternet checkInternet;
    ProgressBar forProBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        checkInternet =  new CheckInternet();

        for_email = findViewById(R.id.for_email);
        forProBar = findViewById(R.id.forProBar);
        back = findViewById(R.id.back);
        send = findViewById(R.id.send);
        startedRelative = findViewById(R.id.startedRelative);

        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();

        send.setText("SEND");

        forProBar.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPass.this, Login.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (for_email.getText().toString().isEmpty()) {
                    for_email.setError("Required");
                }
                else if (!for_email.getText().toString().trim().matches(emailPat)) {
                    for_email.setError("Please enter valid email");
                }
                else {
                    if (checkInternet.isConnect(ForgotPass.this))
                    {
                        forProBar.setVisibility(View.VISIBLE);
                        checkData(for_email.getText().toString());
                    }
                    else
                    {
                        forProBar.setVisibility(View.INVISIBLE);
                        dialog();
                    }
                }

            }
        });
    }

    private void checkData(String mail) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterUser");
        Query query = reference;
        query.orderByChild("userEmail").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                {
                    forProBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(startedRelative,"Provide account email",Snackbar.LENGTH_LONG).show();
                }
                else {
                    auth.sendPasswordResetEmail(Objects.requireNonNull(for_email.getText()).toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            forProBar.setVisibility(View.INVISIBLE);
                            Snackbar.make(startedRelative,"Email sent to:" + for_email.getText().toString(),Snackbar.LENGTH_LONG).show();
                            for_email.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            forProBar.setVisibility(View.INVISIBLE);
                            Snackbar.make(startedRelative,"Failed",Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void dialog() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotPass.this);

            alertDialogBuilder.setMessage("Please Check Your Internet");
            alertDialogBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            try {
                                alertDialog.dismiss();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            Snackbar.make(startedRelative,"Use above back button",Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}