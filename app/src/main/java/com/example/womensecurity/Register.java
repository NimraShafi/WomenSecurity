package com.example.womensecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

public class Register extends AppCompatActivity {

    AppCompatButton startBtn;
    TextInputEditText user_name,user_email,user_pno,user_pass;
    TextView reg_link;
    ConstraintLayout signUpRelat;
    FirebaseAuth auth;
    String emailPat = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    AlertDialog alertDialog;
    CheckInternet checkInternet;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

       checkInternet = new CheckInternet();

        Hooks();

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Creating Your Account....");
        progressDialog.setCanceledOnTouchOutside(false);


        auth = FirebaseAuth.getInstance();


        reg_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_link.setTextColor(getResources().getColor(R.color.first));
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user_name.getText().toString().isEmpty()&&user_email.getText().toString().isEmpty()&&user_pno.getText().toString().isEmpty()
                        &&user_pass.getText().toString().isEmpty())
                {
                    Snackbar.make(signUpRelat,"All Fields are Required",Snackbar.LENGTH_SHORT).show();
                }
                else if (user_name.getText().toString().isEmpty())
                {
                    user_name.setError("Required");
                }
                else  if (user_email.getText().toString().isEmpty())
                {
                    user_email.setError("Required");
                }
                else  if (!user_email.getText().toString().trim().matches(emailPat))
                {
                    user_email.setError("Please enter valid email");
                }

                else  if (user_pno.getText().toString().isEmpty())
                {
                    user_pno.setError("Required");
                }
                else  if (user_pass.getText().toString().isEmpty())
                {
                    user_pass.setError("Required");
                }

                else  if (user_pass.getText().toString().length()<7)
                {
                    user_pass.setError("Password must be greater then 7 digit");
                }

                else
                {
                    if (checkInternet.isConnect(Register.this)){
                        checkEmail(user_email.getText().toString());
                    }
                    else{
                        dialog();
                    }
                }

            }
        });
    }

    private void dialog() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);

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

    private void checkPhone(String phone) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterUser");
        Query query = reference.orderByChild("userPhone").equalTo(phone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Snackbar.make(signUpRelat,"TPhone Number Already Exist, Try Other Number",Snackbar.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    SignUpData(user_name.getText().toString(),
                            user_email.getText().toString(),
                            user_pno.getText().toString(),
                            user_pass.getText().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void checkEmail(String mail) {
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterUser");
        Query query = reference.orderByChild("userEmail").equalTo(mail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressDialog.dismiss();
                    Snackbar.make(signUpRelat,"This Email Already Exist, Try Other Email",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    checkPhone(Objects.requireNonNull(user_pno.getText()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void  SignUpData(String user_name, String user_email, String user_phone, String user_password) {

        auth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();

                DatabaseReference CustInfoDB = FirebaseDatabase.getInstance().getReference("RegisterUser").push();
                String id = CustInfoDB.getKey();
                RegisterModel orderModel = new RegisterModel();
                orderModel.setUserPhone(user_phone);
                orderModel.setId(id);
                orderModel.setUserName(user_name);
                orderModel.setUserEmail(user_email);
                CustInfoDB.setValue(orderModel);

                Snackbar.make(signUpRelat,"Register Successful",Snackbar.LENGTH_SHORT).show();

                new PrefManager
                        (Register.this).saveRegisterDetails(user_name,user_email,user_password,user_phone,id);

                new PrefManager
                        (Register.this).Logout("");

                new PrefManager
                        (Register.this).GuardientNum("","","");

                new PrefManager
                        (Register.this).AlertEnable("");
                new PrefManager
                        (Register.this).CallEnable("");
                new PrefManager
                        (Register.this).VibrateEnable("");

                new PrefManager
                        (Register.this).Message("");

                new PrefManager
                        (Register.this).DefaultNum("");

                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                fileList();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(signUpRelat,""+e.getMessage(),Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    private void Hooks() {

        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_pno = findViewById(R.id.user_pno);
        user_pass = findViewById(R.id.user_pass);
        startBtn = findViewById(R.id.startBtn);
        reg_link = findViewById(R.id.reg_link);
        signUpRelat = findViewById(R.id.signUpRelat);
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