package com.example.uhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class Do_questions extends AppCompatActivity {
    public static final String TAG = Do_questions.class.getSimpleName();
    private FireBaseInfo mFireBaseInfo;
    private String mqs_id;


    private ViewPager mVPquestion;
    private Fspadapter_question mQadapter;

    private ArrayList<String> qidlist;
    private int[] answerweights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_questions);

        Intent intent = getIntent();
        mqs_id = intent.getStringExtra("qs_id");
        mFireBaseInfo = new FireBaseInfo();
        qidlist = new ArrayList<>();

        mVPquestion = findViewById(R.id.doquestions_VP_question);

        getqid();
    }

    private void getqid() {
        mFireBaseInfo.mFirestore
                .collection("questionnaire")
                .document(mqs_id)
                .collection("questions")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            String qid = documentSnapshot.getId();
                            Do_questions.this.qidlist.add(qid);
                        }

//                        todo fill out answer array only if loaded
                        answerweights = new int[qidlist.size()];
                        Arrays.fill(answerweights, -1);

                        mQadapter = new Fspadapter_question(getSupportFragmentManager(),
                                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                                qidlist, mqs_id);
                        mVPquestion.setAdapter(mQadapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Load questions failed");
                finish();
            }
        });

    }

    public void setActivityData(int weight, int position){
        this.answerweights[position] = weight;
    }

    public int getActivityData(int position){
        return this.answerweights[position];
    }
}