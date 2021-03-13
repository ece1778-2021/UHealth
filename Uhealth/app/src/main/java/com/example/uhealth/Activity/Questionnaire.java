package com.example.uhealth.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.uhealth.DataStructures.FireBaseInfo;
import com.example.uhealth.R;
import com.example.uhealth.Adapters.rvadapter_questionnaire_questions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedHashMap;

public class Questionnaire extends AppCompatActivity {
    public static final String TAG = Questionnaire.class.getSimpleName();
    private FireBaseInfo mFireBaseInfo;

    private RecyclerView mRVquestionsets;
    private RecyclerView.LayoutManager mLMquestionsets;
    private rvadapter_questionnaire_questions mADquestionsets;
    private Button mStartButton;
    private RelativeLayout mStartLayout;

    private String qs_idHolder= "", qs_textHolder="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        mFireBaseInfo = new FireBaseInfo();

        mRVquestionsets = findViewById(R.id.questionnaire_RV_questionnaires);
        mLMquestionsets = new GridLayoutManager(this, 1);
        mRVquestionsets.setLayoutManager(mLMquestionsets);
        mStartLayout = findViewById(R.id.questionnaire_startlayout);
        mStartButton = mStartLayout.findViewById(R.id.questionnaire_start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartListener();
            }
        });

        setRV();
        getSupportActionBar().setTitle("Questionnaire");
    }

    private void setStartListener() {
        if (qs_textHolder.equals("") || qs_idHolder.equals("")){
            Toast.makeText(this, "Need to Select a Question Set", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(Questionnaire.this, Do_questions.class);
            intent.putExtra("qs_id", qs_idHolder);
            intent.putExtra("qs_text", qs_textHolder);
            Questionnaire.this.startActivity(intent);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        qs_idHolder = savedInstanceState.getString("QS_ID");
        qs_textHolder = savedInstanceState.getString("QS_TEXT");
        mStartLayout.setVisibility(savedInstanceState.getInt("START"));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("QS_ID", qs_idHolder);
        outState.putString("QS_TEXT", qs_textHolder);
        outState.putInt("START", mStartLayout.getVisibility());
        super.onSaveInstanceState(outState);
    }

    private void setRV() {
        mFireBaseInfo.mFirestore.collection("questionnaire").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int total = queryDocumentSnapshots.size();
                        LinkedHashMap<String, String> mp = new LinkedHashMap<String,String>(total);
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                            String value = document.getData().get("name").toString();
                            String key = document.getId();
                            mp.put(key, value);
                        }
                        mADquestionsets = new rvadapter_questionnaire_questions(mp, new rvadapter_questionnaire_questions.onQSClickListener() {
                            @Override
                            public void onQSClick(String qs_id, String qs_text) {
                                Questionnaire.this.qs_idHolder = qs_id;
                                Questionnaire.this.qs_textHolder = qs_text;
                                mStartLayout.setVisibility(View.VISIBLE);
                            }
                        });
                        mRVquestionsets.setAdapter(mADquestionsets);
                        mRVquestionsets.setHasFixedSize(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Fail to retrive questionnaire from firebase");
                    }
                });
    }


}