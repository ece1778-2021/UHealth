package com.example.uhealth.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhealth.DataModel.timeline_item;
import com.example.uhealth.R;
import com.example.uhealth.ViewHolders.viewholder_timeline;

import java.util.List;

public class rvadapter_tlitem extends RecyclerView.Adapter<viewholder_timeline> {
    private List<timeline_item> itemList;
    private Context mContext;

    public rvadapter_tlitem(Context in_Context, List<timeline_item> in_itemlist){
        itemList = in_itemlist;
        mContext = in_Context;
    }

    @NonNull
    @Override
    public viewholder_timeline onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_timeline_item, parent, false);
        viewholder_timeline tlvh = new viewholder_timeline(view);
        return tlvh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder_timeline holder, int position) {
        timeline_item item = itemList.get(position);
        holder.mAppointment.setText(item.getAppointmentType());
        holder.mPhysician.setText(item.getPhysicianName());
        holder.mLocation.setText(item.getApptLocation());
        holder.mNote.setText(item.getNote());
        holder.mDate.setText(Integer.toString(item.getDate()));

        StringBuilder sb = new StringBuilder();
        sb.append("meds: ");
        for (String s:item.getmMedls()){
            sb.append(s);
            sb.append("\t");
        }
        holder.mMeds.setText(sb.toString());
//        todo configure filter
//        todo configure clicked open
        holder.mDots.setVisibility(View.VISIBLE);
        holder.mAdditional.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        if (itemList!=null){
            return itemList.size();
        }else{
            return 0;
        }
    }
}
