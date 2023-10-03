package com.example.womensecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    AppCompatButton loginBtn;

    TextView for_link,log_link;

    String name,mail,phon,id;

    TextInputEditText login_email,login_pass;


    ConstraintLayout loginstartedRelative;
    String emailPat = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    AlertDialog alertDialog;
    FirebaseAuth auth;
    CheckInternet checkInternet;
    ProgressBar login_pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        Hooks();

        checkInternet = new CheckInternet();
        auth = FirebaseAuth.getInstance();
        login_pro.setVisibility(View.INVISIBLE);

        log_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_link.setTextColor(getResources().getColor(R.color.first));
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        for_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for_link.setTextColor(getResources().getColor(R.color.first));
                Intent intent = new Intent(Login.this,ForgotPass.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (login_email.getText().toString().isEmpty() && login_pass.getText().toString().isEmpty()) {
                    Snackbar.make(loginstartedRelative, "All Fields are Required", Snackbar.LENGTH_SHORT).show();
                } else if (login_email.getText().toString().isEmpty()) {
                    login_email.setError("Required");
                } else if (!login_email.getText().toString().trim().matches(emailPat)) {
                    login_email.setError("Please enter valid email");
                } else if (login_pass.getText().toString().isEmpty()) {
                    login_pass.setError("Required");
                } else if (login_pass.getText().toString().length() < 7)
                    login_pass.setError("Password must greater then 7 digit");
                else {
                    if(checkInternet.isConnect(Login.this)){
                        login_pro.setVisibility(View.VISIBLE);
                        SigninData(login_email.getText().toString(),login_pass.getText().toString());
                    }
                    else {
                        dialog();
                    }
                }
            }
        });
    }

    private void SigninData(String mail, String pass) {
        auth.signInWithEmailAndPassword(mail,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                login_pro.setVisibility(View.INVISIBLE);

              getDAta(mail,pass);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                login_pro.setVisibility(View.INVISIBLE);
                Snackbar.make(loginstartedRelative, Objects.requireNonNull(e.getMessage()),Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getDAta(String mailCust,String pas) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterUser");
        Query query  = reference.orderByChild("userEmail").equalTo(mailCust);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        RegisterModel model = ds.getValue(RegisterModel.class);
                        name = model.getUserName();
                        id = model.getId();
                        phon  = model.getUserPhone();
                    }

                    new PrefManager
                            (Login.this).saveRegisterDetails(name,mailCust,pas,phon,id);
                    new PrefManager
                            (Login.this).Logout("n");

                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Hooks() {
        for_link = findViewById(R.id.for_link);
        login_pass = findViewById(R.id.login_pass);
        login_email = findViewById(R.id.login_email);
        loginBtn = findViewById(R.id.loginBtn);
        log_link = findViewById(R.id.log_link);
        loginstartedRelative = findViewById(R.id.loginstartedRelative);

        login_pro = findViewById(R.id.login_pro);
    }

    private void dialog() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
            alertDialogBuilder.setTitle("No Internet");
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

}