package com.example.gamecam.firebaseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    TextView name;
    TextView email;
    TextView UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        name = (TextView) findViewById(R.id.formName);
        email = (TextView) findViewById(R.id.formEmail);
        UID = (TextView) findViewById(R.id.formUID);

        name.setText(firebaseAuth.getCurrentUser().getDisplayName());
        email.setText(firebaseAuth.getCurrentUser().getEmail());
        UID.setText(firebaseAuth.getCurrentUser().getUid());
    }

    public void UpdateData (View view) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
        .setDisplayName(name.getText().toString()).build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
    }


    public void goToProfile (View view) {
        finish();
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }
}
