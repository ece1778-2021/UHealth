package com.example.uhealth.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uhealth.DataModel.Share_outstandings_item;
import com.example.uhealth.Interfaces.ShareDataLoadedListener;
import com.example.uhealth.repository.Share_repo;

import java.util.List;

public class Share_ViewModel extends ViewModel {
    private ShareDataLoadedListener listener;
    private Share_repo repo;

    private MutableLiveData<List<Share_outstandings_item>> outstandings_items;

    public Share_ViewModel(ShareDataLoadedListener in_listener){
        listener = in_listener;
        repo = Share_repo.getInstance(listener);
        outstandings_items = repo.getOutstandings();
    }

    public LiveData<List<Share_outstandings_item>> getOutstandings(){
        return outstandings_items;
    }

}
