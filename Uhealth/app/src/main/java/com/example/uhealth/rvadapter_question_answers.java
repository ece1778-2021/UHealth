package com.example.uhealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class rvadapter_question_answers extends RecyclerView.Adapter<rvadapter_question_answers.answers> {
    private LinkedHashMap<String, Map<String, String>> lmap;
    private int pos;
    private String[] keyarray;

//    Constructor
    public rvadapter_question_answers(LinkedHashMap<String, Map<String, String>> input, int position){
        lmap = input;
        pos = position;
        Set<String> keyset = input.keySet();
        keyarray = keyset.toArray(new String[keyset.size()]);
    }

    @NonNull
    @Override
    public answers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_question_answers, parent, false);
        answers ans = new answers(view);

        return ans;
    }

    @Override
    public void onBindViewHolder(@NonNull answers holder, int position) {
        String key = keyarray[position];
        String text = lmap.get(key).get("text");
        int weight = Integer.parseInt(lmap.get(key).get("weight"));

        holder.mBT.setText(text);
    }

    @Override
    public int getItemCount() {
        return lmap.size();
    }

    public class answers extends RecyclerView.ViewHolder {
        Button mBT;
        public answers(@NonNull View itemView) {
            super(itemView);
            mBT = itemView.findViewById(R.id.rv_question_answer);
        }
    }

}
