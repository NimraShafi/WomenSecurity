package com.example.womensecurity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements LocationListener {


    ImageView add_user, btn_alert, view_num, learn;
    LocationManager locationManager;
    String country, locality, city, state, subArea;
    AlertDialog alertDialog;
    int countUp = 0, getCountDown = 0;
    String email, id;
    ConstraintLayout signUpRelat;
    LinearLayout setting, myLocation;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    Vibrator vibe;
    String prefPassword, prefEmail, loc;
    String alert, vibrate, call, defaultNumForCall, message, guardientNum1, guardienNum2,police_num;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    Double latitude, lontitude;
    Geocoder geocoder;
    List<Address> adss;
    String myAddress;
    CheckInternet checkInternet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);//prevent application from being locked
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //I am not sure it works.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);//turns the screen on
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        add_user = findViewById(R.id.add_user);
        btn_alert = findViewById(R.id.btn_alert);
        view_num = findViewById(R.id.view_num);
        learn = findViewById(R.id.learn);
        signUpRelat = findViewById(R.id.signUpRelat);
        myLocation = findViewById(R.id.myLocation);
        setting = findViewById(R.id.setting);


        getPref();
        checkInternet = new CheckInternet();


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        vibe = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Addmessage.class);

                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
            }
        });
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();

            }
        });
        view_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewNumber.class);
                startActivity(intent);
            }
        });
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LearnUses.class);
                startActivity(intent);
            }
        });
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                if (checkInternet.isConnect(MainActivity.this))
                {
                    if (alert.matches("alert")) {
                        if (!message.isEmpty()) {
                            if (!guardientNum1.isEmpty()) {
                                if (!guardienNum2.isEmpty()) {
                                    if (!police_num.isEmpty()) {
                                        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                Snackbar.make(signUpRelat, "Location Not Accessed", Snackbar.LENGTH_SHORT).show();
                                        }
                                        else {

                                            sendSMSMessage();
                                        }
                                    }
                                    else
                                    {
                                        Snackbar.make(signUpRelat, "Please Add Police Number In View Number Tab", Snackbar.LENGTH_SHORT).show();

                                    }

                                } else {
                                    Snackbar.make(signUpRelat, "Please Add Guardian 2 Number In View Number Tab", Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar.make(signUpRelat, "Please Add Guardian 1 Number In View Number Tab", Snackbar.LENGTH_SHORT).show();
                            }

                        } else {
                            Snackbar.make(signUpRelat, "Please Write Some Message In Menu Button At Top", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                    else {
                        Snackbar.make(signUpRelat, "Please Enable Alert From Setting", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    dialog();
                }

            }

        });

    }

    private void dialog() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

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

    private void getPref() {

        SharedPreferences messPref = getSharedPreferences("AlertMessage", Context.MODE_PRIVATE);
        message = messPref.getString("messg", "");

        SharedPreferences userData = getSharedPreferences("RegisterDetails", Context.MODE_PRIVATE);
        prefEmail = userData.getString("Email", "");
        prefPassword = userData.getString("Password", "");

        SharedPreferences guarPref = getSharedPreferences("Number", Context.MODE_PRIVATE);
        guardientNum1 = guarPref.getString("num1", "");
        guardienNum2 = guarPref.getString("num2", "");
        police_num = guarPref.getString("num3", "");

        SharedPreferences vibrateEnable = getSharedPreferences("Vibrate", Context.MODE_PRIVATE);
        vibrate = vibrateEnable.getString("vibrate", "");

        SharedPreferences alertEnable = getSharedPreferences("Alert", Context.MODE_PRIVATE);
        alert = alertEnable.getString("alert", "");

        SharedPreferences CallEnable = getSharedPreferences("Call", Context.MODE_PRIVATE);
        call = CallEnable.getString("call", "");

        SharedPreferences Default = getSharedPreferences("Default", Context.MODE_PRIVATE);
        defaultNumForCall = Default.getString("Num", "");

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    protected void sendSMSMessage() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
            Snackbar.make(signUpRelat, "SMS Permission Checking...Wait", Snackbar.LENGTH_SHORT).show();
            sendSMSMessage();
        }
        else
        {
            getLocation();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
        LinearLayout logoutBtn = bottomSheetDialog.findViewById(R.id.logoutBtn);
        LinearLayout add = bottomSheetDialog.findViewById(R.id.add);
        LinearLayout share = bottomSheetDialog.findViewById(R.id.share);
        LinearLayout del = bottomSheetDialog.findViewById(R.id.del);
        bottomSheetDialog.show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewNumber.class);
                startActivity(intent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Women Security");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PrefManager
                        (MainActivity.this).Logout("l");
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                alertDialogBuilder.setTitle("Delete Account");
                alertDialogBuilder.setMessage("Are You Sure To Delete Account");

                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                try {
                                    SharedPreferences sharedpreferences = getSharedPreferences("RegisterDetails", Context.MODE_PRIVATE);

                                    email = sharedpreferences.getString("Email", "");
                                    id = sharedpreferences.getString("Id", "");

                                    checkPhone(id, email);
                                    alertDialog.dismiss();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                try {
                                    alertDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

    }

    private void checkPhone(String id, String email) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterUser");
        Query query = reference.orderByChild("userEmail").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    deleteAccount();
                    new PrefManager
                            (MainActivity.this).saveRegisterDetails("", "", "", "", "");

                    new PrefManager
                            (MainActivity.this).Logout("");

                    new PrefManager
                            (MainActivity.this).GuardientNum("", "" , "");

                    new PrefManager
                            (MainActivity.this).AlertEnable("");
                    new PrefManager
                            (MainActivity.this).CallEnable("");
                    new PrefManager
                            (MainActivity.this).VibrateEnable("");

                    new PrefManager
                            (MainActivity.this).Message("");

                    new PrefManager
                            (MainActivity.this).DefaultNum("");

                    DatabaseReference r = FirebaseDatabase.getInstance().getReference("RegisterUser").child(id);
                    r.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Snackbar.make(signUpRelat, "Account Deleted Successfully", Snackbar.LENGTH_SHORT).show();
                            new PrefManager
                                    (MainActivity.this).saveRegisterDetails("", "", "", "", "");

                            Intent intent = new Intent(MainActivity.this, Register.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(signUpRelat, "failed to Delete Account", Snackbar.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Snackbar.make(signUpRelat, "Account Not Exist", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void deleteAccount() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(prefEmail, prefPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        }
                                    }
                                });
                    }
                });
    }


    private void Vibrate() {

        vibe.vibrate(5000);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(signUpRelat, "Location Not Accessed", Snackbar.LENGTH_SHORT).show();
                return;
            }
            else {
                if (locationManager.isLocationEnabled()) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5,
                            MainActivity.this);
                    city = adss.get(0).getLocality();
                    state = adss.get(0).getAdminArea();
                    country = adss.get(0).getCountryName();
                    subArea = adss.get(0).getSubAdminArea();
                    latitude = adss.get(0).getLatitude();
                    lontitude = adss.get(0).getLongitude();
                    locality = adss.get(0).getAddressLine(0);
                    myAddress = "Country:  " +country + "\nCity:  " +city + "\nArea:  " + state + "SubArea:  " + subArea + "\nLocality:  " + locality;
                    loc = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + lontitude;

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(guardientNum1, null, message + "\n\n" + loc, null, null);

                    SmsManager smsManager1 = SmsManager.getDefault();
                    smsManager1.sendTextMessage(guardienNum2, null, message + "\n\n" + loc, null, null);

                    SmsManager smsManager2 = SmsManager.getDefault();
                    smsManager2.sendTextMessage(police_num, null, message + "\n\n" + loc, null, null);
                    Snackbar.make(signUpRelat, "Sms Send Successfully", Snackbar.LENGTH_SHORT).show();
                   if (vibrate.matches("vib")) {
                       Vibrate();
                       Sound();
                   }
                   if (call.matches("call"))
                   {
                       if (defaultNumForCall.matches("a")) {
                           String phone = guardientNum1;
                           Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                           startActivity(intent);


                       } else if (defaultNumForCall.matches("b")) {
                           String phone = guardienNum2;
                           Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                           startActivity(intent);
                       } else if (defaultNumForCall.matches("c")) {
                           String phone = "police";
                           Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                           startActivity(intent);

                       }
                       else
                       {
                           System.out.println("None");
                       }
                   }
                   }
                else {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    boolean gps_enabled = false;
                    boolean network_enabled = false;
                    try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (!gps_enabled && !network_enabled) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Enable GPS Service")
                                .setMessage("We need your GPS location to get your location")
                                .setCancelable(false)
                                .setPositiveButton("Enable", new
                                        DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                                sendSMSMessage();
                                            }
                                        })

                                .show();

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onLocationChanged(Location location) {

        try {
            if (locationManager.isLocationEnabled())
            {
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                adss = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            }
            else
            {
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                boolean network_enabled = false;
                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!gps_enabled && !network_enabled) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Enable GPS Service")
                            .setMessage("We need your GPS location to get your location")
                            .setCancelable(false)
                            .setPositiveButton("Enable", new
                                    DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                            
                                        }
                                    })

                            .show();

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_POWER)
        {

        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            getCountDown +=1;

            if (getCountDown == 1)
            {
                Snackbar.make(signUpRelat, "Press Two More Time", Snackbar.LENGTH_SHORT).show();
            }

            else if (getCountDown == 2)
            {
                Snackbar.make(signUpRelat, "Press One More Time", Snackbar.LENGTH_SHORT).show();
            }

            else if (getCountDown == 3)
            {
                getCountDown %=3;
                sendSMSMessage();

            }
            else {

            }
        }
        return true;
    }

    private void Sound() {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT,  100000);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            countUp +=1;
            if (countUp == 1)
            {
                Snackbar.make(signUpRelat, "Press Two More Time", Snackbar.LENGTH_SHORT).show();
            }

            else if (countUp == 2)
            {
                Snackbar.make(signUpRelat, "Press One More Time", Snackbar.LENGTH_SHORT).show();
            }

            else if (countUp == 3)
            {
                countUp %=3;
                sendSMSMessage();


            }
            else {

            }
        }
        return true;
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 44) {
                sendSMSMessage();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onStart() {
        getPref();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyPressed = event.getKeyCode();
        if(keyPressed==KeyEvent.KEYCODE_POWER){
            sendSMSMessage();
            return true;}
        else
            return super.dispatchKeyEvent(event);
    }
}

