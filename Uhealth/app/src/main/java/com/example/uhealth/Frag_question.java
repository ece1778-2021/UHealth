package com.example.uhealth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Frag_question extends Fragment {
    private TextView mQuestion;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private rvadapter_question_answers mAdapter;

    private String qid, qs_id;
    private int pos;
    private FireBaseInfo mFireBaseInfo;

    public Frag_question() {
        // Required empty public constructor
    }

    public static Frag_question newInstance() {
        Frag_question fragment = new Frag_question();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_question, container, false);
        mQuestion = view.findViewById(R.id.frag_question);
        recyclerView = view.findViewById(R.id.rv_question_answer);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);


        qid = getArguments().getString("qid");
        qs_id = getArguments().getString("qs_id");
        pos = getArguments().getInt("position");

        mFireBaseInfo = new FireBaseInfo();
        initView();

        return view;
    }

    private void initView() {
        DocumentReference ref_text = mFireBaseInfo.mFirestore
                .collection("questionnaire")
                .document(qs_id)
                .collection("questions")
                .document(qid);
        Query ref_answers = mFireBaseInfo.mFirestore
                .collection("questionnaire")
                .document(qs_id)
                .collection("questions")
                .document(qid)
                .collection("answers")
                .orderBy("weight", Query.Direction.ASCENDING);

        ref_text.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       String text = documentSnapshot.get("text").toString();
                       mQuestion.setText(text);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Do_questions.TAG, "Load fragment question failed");
                    }
                });

        ref_answers.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int total = queryDocumentSnapshots.size();
                        LinkedHashMap<String, Map<String, String>> mp = new LinkedHashMap<>(total);
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            String answer = documentSnapshot.getData().get("text").toString();
                            String key = documentSnapshot.getId();
                            String weight = documentSnapshot.getData().get("weight").toString();

                            Map<String,String> in_mp = new HashMap<>();
                            in_mp.put("weight", weight);
                            in_mp.put("text", answer);
                            mp.put(key, in_mp);
                        }


                        mAdapter = new rvadapter_question_answers(mp, pos,
                                new questionFragmentCallback() {
                                    @Override
                                    public int getValue(int position) {
                                        return ((Do_questions)getActivity()).getQuestionnaireValue(position);
                                    }

                                    @Override
                                    public void setValue(int weight, int position) {
                                        ((Do_questions)getActivity()).setQuestionnaireValue(weight, position);
                                    }
                                }
                                );
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.setHasFixedSize(true);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Do_questions.TAG, "Load fragment answers failed");
                    }
                });
    }

    public interface questionFragmentCallback{
        int getValue(int position);
        void setValue(int weight, int position);
    }

}