package com.example.uhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Do_questions extends AppCompatActivity {
    public static final String TAG = Do_questions.class.getSimpleName();
    private FireBaseInfo mFireBaseInfo;
    private CachedThreadPool threadPool;
    private String mqs_id;
    private String mqs_text;

    private ViewPager mVPquestion;
    private Fspadapter_question mQadapter;

    private ArrayList<String> qidlist;

    private boolean initanswers;
    private int[] answerweights;

    private ProgressDialog progressDialog;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_questions);

        Intent intent = getIntent();
        mqs_id = intent.getStringExtra("qs_id");
        mqs_text = intent.getStringExtra("qs_text");
        mFireBaseInfo = new FireBaseInfo();
        threadPool = CachedThreadPool.getInstance();
        progressDialog = new ProgressDialog(this);
        dialog = new Dialog(this);

        qidlist = new ArrayList<>();

        mVPquestion = findViewById(R.id.doquestions_VP_question);

        initanswers = true;
        if (savedInstanceState!=null){
            int[] ans = savedInstanceState.getIntArray("answers");
            if (ans!=null){
                initanswers = false;
                answerweights = ans;
            }
        }

        getqid();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putIntArray("answers", answerweights);
        super.onSaveInstanceState(outState);
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

                        if (initanswers){
                            answerweights = new int[qidlist.size()];
                            Arrays.fill(answerweights, -1);
                        }

                        mQadapter = new Fspadapter_question(getSupportFragmentManager(),
                                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                                qidlist,
                                mqs_id
                                );
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

    public void setQuestionnaireValue(int weight, int position){
        answerweights[position] = weight;
    }
    public int getQuestionnaireValue(int position){
        return answerweights[position];
    }

    public void submitAnswers(){
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progressdialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        int[] weightarray = answerweights;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                boolean check = true;
                int sum = 0;
                int index = 0;
                for (int element : weightarray){
                    if (element<0){
                        check = false;
                        break;
                    }
                    sum = sum + element;
                    index = index+1;
                }
                final boolean f_check = check;
                final int f_sum = sum;
                final int f_index = index;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (f_check){
                            Map<String, Object> user_score =  new HashMap<>();
                            user_score.put("user", mFireBaseInfo.mUser.getUid());
                            user_score.put("qs_id", mqs_id);
                            user_score.put("score", f_sum);
                            user_score.put("timestamp", System.currentTimeMillis()/1000);

                            mFireBaseInfo.mFirestore.collection("user_score").add(user_score)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(Do_questions.this, mqs_text+":"+f_sum+" Result Submited!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }else{
                                                progressDialog.dismiss();
                                                Toast.makeText(Do_questions.this, mqs_text+" Submit Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            progressDialog.dismiss();
                            openCheckFailDialog(f_index);
                        }
                    }
                });
            }
        };

        threadPool.add_run(r);
    }

    private void openCheckFailDialog(int index) {
        Button mPos, mNeg;
        TextView mComment;
        dialog.show();
        dialog.setContentView(R.layout.dialog_unanswerquestion);
        mPos = dialog.findViewById(R.id.dialog_unansweredquestion_BT_goto);
        mNeg = dialog.findViewById(R.id.dialog_unansweredquestion_BT_cancel);
        mComment = dialog.findViewById(R.id.dialog_unansweredquestion_TV_comment);
        mComment.append(Integer.toString(index+1));
        mNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mVPquestion.setCurrentItem(index);
            }
        });
    }
}