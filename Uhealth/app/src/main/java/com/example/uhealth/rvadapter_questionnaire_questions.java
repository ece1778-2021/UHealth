package com.example.uhealth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;
import java.util.Set;

public class rvadapter_questionnaire_questions extends RecyclerView.Adapter<rvadapter_questionnaire_questions.questionset> {
    private LinkedHashMap<String, String> listmap;
    private onQSClickListener listener;
    private String[] keyarray;

    public rvadapter_questionnaire_questions(LinkedHashMap<String, String> input, onQSClickListener clickListener){
        this.listener = clickListener;
        this.listmap = input;
        Set<String> keySet = input.keySet();
        this.keyarray = keySet.toArray(new String[keySet.size()]);
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
    }

    @Override
    public int getItemCount() {
        return listmap.size();
    }

    public class questionset extends RecyclerView.ViewHolder implements View.OnClickListener{
        Button mBT;
        onQSClickListener mlistener;

        public questionset(@NonNull View itemView, onQSClickListener listener) {
            super(itemView);
            mBT = itemView.findViewById(R.id.rv_questionnaires_questionnaires);
            mlistener = listener;
            mBT.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == mBT.getId()){
                String key = rvadapter_questionnaire_questions.this.keyarray[getAdapterPosition()];
                mlistener.onQSClick(key);
            }
        }
    }

    public interface onQSClickListener{
        void onQSClick(String qs_id);
    }

}
