package com.example.uhealth.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uhealth.R;

public class ShareRequest_Frag extends Fragment {

    public ShareRequest_Frag() {
        // Required empty public constructor
    }

    public static ShareRequest_Frag newInstance() {
        ShareRequest_Frag fragment = new ShareRequest_Frag();
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
        return inflater.inflate(R.layout.fragment_share_request_, container, false);
    }
}