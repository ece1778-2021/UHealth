package com.example.uhealth.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uhealth.AppointmnetList;
import com.example.uhealth.DataStructures.CachedThreadPool;
import com.example.uhealth.DataStructures.FireBaseInfo;
import com.example.uhealth.MedicationList;
import com.example.uhealth.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {
    private Toolbar toolbar;
    private Button mPlaceHolderInitReg, mPlaceHolderQuestionnaire, mPlaceHolderapp, mPlaceHoldermed,mPlaceHolderPopulate;

    private FireBaseInfo mFireBaseInfo;
    private CachedThreadPool threadPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        toolbar = findViewById(R.id.profilepg_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("profile page");

        mFireBaseInfo = new FireBaseInfo();
        threadPool = CachedThreadPool.getInstance();

        mPlaceHolderInitReg = findViewById(R.id.placeholder_initreg);
        mPlaceHolderInitReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, InitRegPage.class);
                startActivity(intent);
                finish();
            }
        });
        mPlaceHolderQuestionnaire = findViewById(R.id.placeholder_questionnaire);
        mPlaceHolderQuestionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, Questionnaire.class);
                startActivity(intent);
            }
        });
        mPlaceHolderapp = findViewById(R.id.placeholder_app);
        mPlaceHolderapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, AppointmnetList.class);
                startActivity(intent);
            }
        });
        mPlaceHoldermed = findViewById(R.id.placeholder_med);
        mPlaceHoldermed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, MedicationList.class);
                startActivity(intent);
            }
        });
        mPlaceHolderPopulate = findViewById(R.id.placeholder_populate);
        mPlaceHolderPopulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for populate
                Toast.makeText(ProfilePage.this,"disabled", Toast.LENGTH_SHORT).show();
//                placeholderpopulate();
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

    private void placeholderpopulate() {
        String question_text =
                "Overall, how much does leaking urine interfere with your everyday life?"
                ;
        int[] weightarray = {0, 1, 2, 3,4,5,6,7,8,9,10};
        String[] textarray = {
                "0 (not at all)"
                ,
                "1"
                ,
                "2"
                ,
                "3"
                ,
                "4"
                ,
                "5"
                ,
                "6"
                ,
                "7"
                ,
                "8"
                ,
                "9"
                ,
                "10 (a great deal)"

        };

        if (weightarray.length != textarray.length){
            Toast.makeText(this, "array no match", Toast.LENGTH_SHORT).show();
        }

        Map<String,Object> mp = new HashMap<>();
        mp.put("text", question_text);

        CollectionReference cref = mFireBaseInfo.mFirestore.collection("questionnaire")
                .document(
                        "M7ynd1hCO7Tw2Wi2v3Jt"
                )
                .collection("questions");
        cref
                .add(mp)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        String id = documentReference.getId();

                        for (int i=0; i<weightarray.length;i++){
                            Map<String,Object> in_mp=  new HashMap<>();
                            in_mp.put("text", textarray[i]);
                            in_mp.put("weight", weightarray[i]);
                            cref.document(id).collection("answers").add(in_mp);
                        }

                        Toast.makeText(ProfilePage.this, "Done", Toast.LENGTH_SHORT).show();
                    }
                });


    }

}