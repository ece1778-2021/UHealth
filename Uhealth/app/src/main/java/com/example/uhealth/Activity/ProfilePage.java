package com.example.uhealth.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhealth.AppointmnetList;
import com.example.uhealth.utils.FireBaseInfo;
import com.example.uhealth.MedicationList;
import com.example.uhealth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfilePage extends AppCompatActivity {
    private FireBaseInfo mFireBaseInfo;
    private Toolbar toolbar;
    private TextView mUsername;

    private RelativeLayout mPlaceHolderInitReg, mPlaceHolderapp, mPlaceHoldermed, mPlaceHolderQuestionnaire,
            mPlaceHolderTimeline, mPlaceHolderShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mFireBaseInfo = new FireBaseInfo();
        toolbar = findViewById(R.id.profilepg_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("profile page");

        mUsername = findViewById(R.id.profileusername);

        mPlaceHolderInitReg = findViewById(R.id.infopageSection);

        mPlaceHolderQuestionnaire = findViewById(R.id.questionnaireSection);

        mPlaceHolderapp = findViewById(R.id.appointmentSection);

        mPlaceHoldermed = findViewById(R.id.medicationSection);

        mPlaceHolderTimeline = findViewById(R.id.TimeLineSection);

        mPlaceHolderShare = findViewById(R.id.ShareSection);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlaceHolderInitReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, InitRegPage.class);
                startActivity(intent);
                finish();
            }
        });
        mPlaceHolderQuestionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, Questionnaire.class);
                startActivity(intent);
            }
        });
        mPlaceHolderapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, AppointmnetList.class);
                startActivity(intent);
            }
        });
        mPlaceHoldermed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, MedicationList.class);
                startActivity(intent);
            }
        });
        mPlaceHolderTimeline.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, Timeline.class);
                startActivity(intent);
            }
        });
        mPlaceHolderShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, ShareFeature.class);
                startActivity(intent);
            }
        });

        mFireBaseInfo.mFirestore.collection("users").document(mFireBaseInfo.mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            try {
                                mUsername.setText(documentSnapshot.get("username").toString());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(ProfilePage.this, "load username failed", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        adds to action bar if present
        inflater.inflate(R.menu.profilepg_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int v_id = item.getItemId();
        if (v_id==R.id.pp_tb_setting){
            Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
            return true;
        }else if (v_id == R.id.pp_tb_logout){
            Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
            logOut();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        mFireBaseInfo.mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}