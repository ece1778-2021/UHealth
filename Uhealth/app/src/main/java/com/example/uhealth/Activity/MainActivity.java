package com.example.uhealth.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.uhealth.DataStructures.FireBaseInfo;
import com.example.uhealth.R;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private FireBaseInfo mFireBaseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        mFireBaseInfo = new FireBaseInfo();
        FirebaseUser mUser = mFireBaseInfo.mUser;

        Intent intent;
        if (mUser != null){
            Log.d(TAG, "User loggin on mainactivity");
            intent = new Intent(this, ProfilePage.class);
        }else{
            Log.d(TAG, "User not loggedin on mainactivity");
            intent = new Intent(this, LoginPage.class);
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
    @Override
    protected void onStart(){
        super.onStart();

    }
}