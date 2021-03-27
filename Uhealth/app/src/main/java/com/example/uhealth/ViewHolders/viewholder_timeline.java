package com.example.uhealth.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhealth.R;

public class viewholder_timeline extends RecyclerView.ViewHolder {
    public TextView mDate, mAppointment, mPhysician, mLocation, mNote, mMeds;
    public ImageView mDots;
    public RelativeLayout mAdditional;

    public viewholder_timeline(@NonNull View itemView) {
        super(itemView);
        initView();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur_vis = mDots.getVisibility();
                if (cur_vis == View.VISIBLE){
                    mDots.setVisibility(View.GONE);
                    mAdditional.setVisibility(View.VISIBLE);
                }else{
                    mDots.setVisibility(View.VISIBLE);
                    mAdditional.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initView() {
        mDate = itemView.findViewById(R.id.date);
        mAppointment = itemView.findViewById(R.id.appointment_type);
        mPhysician = itemView.findViewById(R.id.physician_name);
        mLocation = itemView.findViewById(R.id.location);
        mDots = itemView.findViewById(R.id.dots);

        mAdditional = itemView.findViewById(R.id.additional_info);
        mNote = itemView.findViewById(R.id.note);
        mMeds = itemView.findViewById(R.id.medications);
    }
}
