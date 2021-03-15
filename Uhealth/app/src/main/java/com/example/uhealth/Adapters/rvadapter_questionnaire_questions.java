package com.example.uhealth.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhealth.Activity.Questionnaire;
import com.example.uhealth.DataStructures.FireBaseInfo;
import com.example.uhealth.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class rvadapter_questionnaire_questions extends RecyclerView.Adapter<rvadapter_questionnaire_questions.questionset> {
    private LinkedHashMap<String, String> listmap;
    private onQSClickListener listener;
    private String[] keyarray;
    private FireBaseInfo mFirebaseInfo;
    private int lastpos = -1;

    public rvadapter_questionnaire_questions(LinkedHashMap<String, String> input, onQSClickListener clickListener){
        this.listener = clickListener;
        this.listmap = input;
        Set<String> keySet = input.keySet();
        this.keyarray = keySet.toArray(new String[keySet.size()]);
        mFirebaseInfo = new FireBaseInfo();
    }


    @NonNull
    @Override
    public questionset onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_questionnaires_questionnaires,
                parent, false);
        questionset qs = new questionset(view, listener);

        return qs;
    }

    @Override
    public void onBindViewHolder(@NonNull questionset holder, int position) {
        String key = this.keyarray[position];
        String text = this.listmap.get(key);
        holder.mBT.setText(text);
        if (lastpos == position){
            holder.chartLayout.setVisibility(View.VISIBLE);
        }else{
            holder.chartLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listmap.size();
    }

    public class questionset extends RecyclerView.ViewHolder implements View.OnClickListener{
        Button mBT;
        onQSClickListener mlistener;
        FrameLayout chartLayout;
        LineChart lineChart;

        public questionset(@NonNull View itemView, onQSClickListener listener) {
            super(itemView);
            mBT = itemView.findViewById(R.id.rv_questionnaires_questionnaires);
            mlistener = listener;
            lineChart = itemView.findViewById(R.id.linechart);
            chartLayout = itemView.findViewById(R.id.graphlayout);
            lineChart.setTouchEnabled(true);
            lineChart.setPinchZoom(true);
            mBT.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == mBT.getId()){
                int posi = getAdapterPosition();
                String key = rvadapter_questionnaire_questions.this.keyarray[posi];
                String text = listmap.get(key);
                mlistener.onQSClick(key, text);
                chartLayout.setVisibility(View.VISIBLE);


                int temp_last = lastpos;
                lastpos = posi;
                if (posi != temp_last){
                    loadgraph(key, lineChart);
                    notifyItemChanged(temp_last);
                }

            }
        }
    }

    private void loadgraph(String key, LineChart lchart) {
        ArrayList<Entry> values = new ArrayList<>();

        Query query = mFirebaseInfo.mFirestore.collection("user_score")
                .whereEqualTo("qs_id", key)
                .whereEqualTo("user", mFirebaseInfo.mUser.getUid())
                .orderBy("timestamp", Query.Direction.ASCENDING);
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                            try{
                                long timestamp = (long)documentSnapshot.getData().get("timestamp");
                                long score = (long)documentSnapshot.getData().get("score");
                                values.add(new Entry(timestamp,score));
                            }catch (Exception e){
                                Log.d(Questionnaire.TAG, "Exception in avaliable data");
                                return;
                            }
                        }

                        LineDataSet lineDataSet = new LineDataSet(values, "data set1");
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(lineDataSet);

                        LineData oData = new LineData(dataSets);
                        lchart.setData(oData);
                        lchart.invalidate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Questionnaire.TAG, "Load grpah data Failed");
                    }
                });
    }




    public interface onQSClickListener{
        void onQSClick(String qs_id, String qs_text);
    }

}
