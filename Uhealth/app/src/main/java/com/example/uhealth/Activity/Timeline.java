package com.example.uhealth.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.uhealth.Adapters.rvadapter_tlitem;
import com.example.uhealth.DataModel.timeline_item;
import com.example.uhealth.Fragments.TimelineFilter;
import com.example.uhealth.Interfaces.DataloadedListener;
import com.example.uhealth.ViewModel.TimelineViewModel;
import com.example.uhealth.ViewModel.TimelineViewModelFactory;
import com.example.uhealth.R;
import com.example.uhealth.utils.GenerateFakeData;

import java.util.List;

public class Timeline extends AppCompatActivity{
    public static final String TAG = Timeline.class.getSimpleName();

    private TimelineViewModel timelineViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setTitle("Timeline");

        initViews();

        String[] filterlist = getResources().getStringArray(R.array.Appointment_types);
        TimelineViewModelFactory factory = new TimelineViewModelFactory(filterlist, new DataloadedListener() {
            @Override
            public void onDataLoaded() {
                timelineViewModel.getTl_items().observe(Timeline.this, new Observer<List<timeline_item>>() {
                    @Override
                    public void onChanged(List<timeline_item> timeline_items) {
                        Log.d(TAG, "Loaded from repo, notifyDatasetChange");
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        timelineViewModel = new ViewModelProvider(this, factory).get(TimelineViewModel.class);
        initRecyclerView();
    }

    private void initRecyclerView() {
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new rvadapter_tlitem(this, timelineViewModel.getTl_items().getValue());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_tl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        adds to action bar if present
        inflater.inflate(R.menu.timeline_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int v_id = item.getItemId();
        if (v_id==R.id.tl_tb_clear){
            timelineViewModel.clearall();
            return true;
        }else if (v_id == R.id.tl_tb_filter){
            openFilterDialog();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void openFilterDialog() {
        DialogFragment timelinefilter = TimelineFilter.newInstance();
        timelinefilter.show(getSupportFragmentManager(), "tag");
    }
}