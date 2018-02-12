package com.example.gamecam.firebaseapp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.io.File;
import java.io.IOException;

public class MenuActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private final int PICK_IMAGE_REQUEST = 71;
    ImageView imageView;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        firebaseAuth = FirebaseAuth.getInstance();
        String notificationToken = FirebaseInstanceId.getInstance().getToken();

        setTitle("User: "+firebaseAuth.getCurrentUser().getEmail());
        firebaseAuth.getCurrentUser().getUid();

        FirebaseMessaging.getInstance().subscribeToTopic("login_user");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Activity activity = (Activity) this;
        FirebaseConfigManager fcm = new FirebaseConfigManager();
        fcm.getRemoteConfig(activity);


        String welcomeMessage = fcm.getmFirebaseRemoteConfig().getString("welcome_text");
        TextView txt=(TextView) findViewById(R.id.message);
        txt.setText(welcomeMessage);

        imageView = (ImageView) findViewById(R.id.imgView);

    }

    public void logOut (View view) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        FirebaseMessaging.getInstance().unsubscribeFromTopic("login_user");
    }



    public void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void uploadBut (View view) {
        if (filePath!=null) {
        FirebaseStorageManager fsm = new FirebaseStorageManager();
        fsm.uploadFile(filePath, this);
        imageView.setImageResource(android.R.color.transparent);
        }
    }

    public void goToProfile (View view) {
        finish();
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }


}

