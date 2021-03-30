package com.example.uhealth.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhealth.DataModel.Share_outstandings_item;
import com.example.uhealth.R;

import java.util.List;

public class rvadapter_shareoutstandings extends RecyclerView.Adapter<rvadapter_shareoutstandings.outstandings_vh> {
    private List<Share_outstandings_item> outstandingslist;
    private Context mContext;

    public rvadapter_shareoutstandings(Context in_context, List<Share_outstandings_item> in_outstandinglist){
//        constructor
        outstandingslist = in_outstandinglist;
        mContext = in_context;
    }

    @NonNull
    @Override
    public outstandings_vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_share_outstandings_item, parent, false);
        outstandings_vh vh = new outstandings_vh(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull outstandings_vh holder, int position) {
        Share_outstandings_item outstandings_item = outstandingslist.get(position);
        String fromuser = outstandings_item.getFrom_username();
        holder.tv.setText(fromuser);
    }

    @Override
    public int getItemCount() {
        if (outstandingslist!=null){
            return outstandingslist.size();
        }else{
            return 0;
        }
    }

    public class outstandings_vh extends RecyclerView.ViewHolder {
        TextView tv;
        public outstandings_vh(@NonNull View itemView) {
            super(itemView);
            initview();
        }

        private void initview() {
            tv = itemView.findViewById(R.id.outstandings_test);
        }
    }
}
