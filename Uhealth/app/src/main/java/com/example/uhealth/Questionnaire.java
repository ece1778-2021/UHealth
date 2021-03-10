package com.example.uhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedHashMap;

public class Questionnaire extends AppCompatActivity {
    private static final String TAG = Questionnaire.class.getSimpleName();
    private FireBaseInfo mFireBaseInfo;

    private RecyclerView mRVquestionsets;
    private RecyclerView.LayoutManager mLMquestionsets;
    private rvadapter_questionnaire_questions mADquestionsets;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        mFireBaseInfo = new FireBaseInfo();

        mRVquestionsets = findViewById(R.id.questionnaire_RV_questionnaires);
        mLMquestionsets = new GridLayoutManager(this, 1);
        mRVquestionsets.setLayoutManager(mLMquestionsets);
        setRV();



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
                                Intent intent = new Intent(Questionnaire.this, Do_questions.class);
                                intent.putExtra("qs_id", qs_id);
                                intent.putExtra("qs_text", qs_text);
                                Questionnaire.this.startActivity(intent);
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