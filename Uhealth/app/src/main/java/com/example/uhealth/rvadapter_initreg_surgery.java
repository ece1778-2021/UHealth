package com.example.uhealth;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class rvadapter_initreg_surgery extends RecyclerView.Adapter<rvadapter_initreg_surgery.surgery_box> {
    private Context mContext;

//    Constructor
    public rvadapter_initreg_surgery(Context context){

    }

    @NonNull
    @Override
    public rvadapter_initreg_surgery.surgery_box onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull rvadapter_initreg_surgery.surgery_box holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class surgery_box extends RecyclerView.ViewHolder{
        EditText mName;
        Button mMinus;
        TextView mDate;

        public surgery_box(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.rv_initreg_surgery);
            mMinus = itemView.findViewById(R.id.rv_initreg_minus);
            mDate = itemView.findViewById(R.id.rv_initreg_date);
        }
    }


}
