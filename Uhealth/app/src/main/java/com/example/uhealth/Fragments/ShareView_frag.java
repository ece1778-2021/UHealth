package com.example.uhealth.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uhealth.R;

public class ShareView_frag extends Fragment {

    public ShareView_frag() {
        // Required empty public constructor
    }

    public static ShareView_frag newInstance() {
        ShareView_frag fragment = new ShareView_frag();
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
        return inflater.inflate(R.layout.fragment_share_view_frag, container, false);
    }
}