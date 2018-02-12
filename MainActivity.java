package com.example.gamecam.firebaseapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


public class MainActivity extends AppCompatActivity {
    static String ACTUAL_PROJECT_VERSION="0.0.0";
    static int ACTUAL_PROJECT_VERSION_CODE=0;

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;


    //progress dialog
    private ProgressDialog progressDialog;


    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            ACTUAL_PROJECT_VERSION = pInfo.versionName;
            ACTUAL_PROJECT_VERSION_CODE= pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        //Inicializácie
        firebaseAuth = FirebaseAuth.getInstance();
        isUserLogIn();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);

        progressDialog = new ProgressDialog(this);

        Activity activity = (Activity) this;
        FirebaseConfigManager fcm = new FirebaseConfigManager();
        fcm.getRemoteConfig(activity);

        //Get actual app version
        String ACTUAL_VERSION = fcm.getmFirebaseRemoteConfig().getString("actual_version");
        int ACTUAL_VERSION_CODE =(int)(fcm.getmFirebaseRemoteConfig().getLong("actual_version_code"));



        //Overenie aktuálnej verzie programu
        if (!ACTUAL_VERSION.equals(ACTUAL_PROJECT_VERSION) || ACTUAL_PROJECT_VERSION_CODE!=ACTUAL_VERSION_CODE) {
            Toast.makeText(this,"Your version is out of date!! Update the app!",Toast.LENGTH_LONG).show();
        }
    }

    private void registerUser(Dialog dg){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);

        EditText editTextEmail = (EditText) dg.findViewById(R.id.editTextEmail);
        EditText editTextPassword = (EditText) dg.findViewById(R.id.editTextPassword);

        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                            Toast.makeText(MainActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        }else{
                            //display some message here
                            Toast.makeText(MainActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    public void userLogin(View view){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Login.... please wait");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        } else {
                            Toast.makeText(getApplication(),"Bad user email or password",Toast.LENGTH_LONG).show();
                        }


                    }
                });

    }

    public void RegisterButton(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.register_dialog);
        Button buttonSignup = (Button) dialog.findViewById(R.id.buttonRegister);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(dialog);
            }
        });

        dialog.show();

    }

    public void isUserLogIn () {
        if(firebaseAuth.getCurrentUser() != null){

            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }
    }

}