package com.example.gamecam.firebaseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        ImageView img = (ImageView) findViewById(R.id.imageView3);
        TextView name = (TextView) findViewById(R.id.name);
        TextView email = (TextView) findViewById(R.id.email);
        TextView UID = (TextView) findViewById(R.id.unique);

        FirebaseStorageManager fsm = new FirebaseStorageManager();


        //Glide.get(this).clearDiskCache();
        //Glide.get(this).clearMemory();

        Glide.clear(img);

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(fsm.getStorageReference())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(img);



        name.setText("Your display name: "+firebaseAuth.getCurrentUser().getDisplayName());
        email.setText("Your email: "+firebaseAuth.getCurrentUser().getEmail());
        UID.setText("Your unique ID: "+firebaseAuth.getCurrentUser().getUid());


    }

    public void backToMenu (View view) {
        finish();
        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
    }

    public void editProfile (View view) {
        finish();
        startActivity(new Intent(getApplicationContext(), EditProfile.class));
    }
}
