package com.example.uhealth.ViewModelFactory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.uhealth.Interfaces.DataloadedListener;
import com.example.uhealth.ViewModel.TimelineViewModel;

public class TimelineViewModelFactory implements ViewModelProvider.Factory {
    private String[] arraylist;
    private DataloadedListener listener;

    public TimelineViewModelFactory(String[] inputlist, DataloadedListener implemented){
        arraylist = inputlist;
        listener = implemented;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TimelineViewModel.class)){
            return (T) new TimelineViewModel(arraylist, listener);
        }else{
            throw new IllegalArgumentException("VM factory exception");
        }
    }
}
