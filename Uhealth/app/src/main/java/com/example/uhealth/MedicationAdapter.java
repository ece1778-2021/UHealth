package com.example.uhealth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {
    private List<Medication> mMedicationList;
    @NonNull
    @Override
    public MedicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.medication_unit,null);
        MedicationAdapter.ViewHolder holder=new MedicationAdapter.ViewHolder(view);
        return holder;
    }
    public MedicationAdapter(List<Medication> medicationList){
        mMedicationList = medicationList;
    }
    @Override
    public void onBindViewHolder(@NonNull MedicationAdapter.ViewHolder holder, int position) {

        Medication mMedication=mMedicationList.get(position);
        //note from res id to actual bitmap
        //holder.fruitImage.setImageResource(mphoto.getImageID());

        holder.medicationTag.setText(mMedication.getMedicine());
    }
    @Override
    public int getItemCount() {
        return mMedicationList.size();

    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView medicationTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicationTag  =itemView.findViewById(R.id.medication_text_unit);
        }
    }
}
