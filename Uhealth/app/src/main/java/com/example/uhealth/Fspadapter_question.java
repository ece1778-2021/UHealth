package com.example.uhealth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class Fspadapter_question extends FragmentStatePagerAdapter {
    private ArrayList<String> list;
    private String qs_id;

    public Fspadapter_question(@NonNull FragmentManager fm, int behavior,
                               ArrayList<String> input,
                               String qs_id) {
        super(fm, behavior);
        this.list = input;
        this.qs_id = qs_id;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Frag_question frag_q = new Frag_question();
        Bundle bundle = new Bundle();
        String qid = list.get(position);
        bundle.putString("qid", qid);
        bundle.putString("qs_id", qs_id);
        bundle.putInt("position", position);
        frag_q.setArguments(bundle);
        position = position+1;
        return frag_q;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
