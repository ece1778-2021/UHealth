package com.example.uhealth.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uhealth.R;
import com.example.uhealth.ViewModel.Share_ViewModel;

public class ShareView_frag extends Fragment {
    private Context mContext;
    private Share_ViewModel viewModel;


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
        View view = inflater.inflate(R.layout.fragment_share_view_frag, container, false);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        viewModel = new ViewModelProvider(getActivity()).get(Share_ViewModel.class);
    }
}