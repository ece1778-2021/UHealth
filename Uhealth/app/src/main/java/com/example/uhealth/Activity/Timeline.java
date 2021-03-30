package com.example.uhealth.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import com.example.uhealth.ViewModelFactory.TimelineViewModelFactory;
import com.example.uhealth.R;
import com.example.uhealth.utils.CachedThreadPool;

import java.text.SimpleDateFormat;
import java.util.List;

public class Timeline extends AppCompatActivity{
    public static final String TAG = Timeline.class.getSimpleName();

    private TimelineViewModel timelineViewModel;
    private CachedThreadPool threadPool;

    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setTitle("Timeline");

        threadPool = CachedThreadPool.getInstance();

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
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onBeforeLoaded() {
                progressDialog = new ProgressDialog(Timeline.this);
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.progressdialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        });
        timelineViewModel = new ViewModelProvider(this, factory).get(TimelineViewModel.class);
        initRecyclerView();
    }

    private void initRecyclerView() {
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new rvadapter_tlitem(this, new passVMtoRV() {
            @Override
            public List<String> getfiltertypes() {
                return timelineViewModel.getTypeFilter();
            }
        }, timelineViewModel.getTl_items().getValue());
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
            mAdapter.notifyDataSetChanged();
            return true;
        }else if (v_id == R.id.tl_tb_filter){
            openFilterDialog();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void openFilterDialog() {
        DialogFragment timelinefilter = TimelineFilter.newInstance(new dialogListener() {
            @Override
            public void jumppagelistener() {
                int range = timelineViewModel.get_date();
                if (range<0){
//                    no years selected
                }else{
                    Runnable r = () -> runOnUiThread(() -> {
                        List<timeline_item> datalist = timelineViewModel.getTl_items().getValue();
                        String year_s = String.valueOf(timelineViewModel.get_date());

                        SimpleDateFormat dateformat= new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                        try {
                            int timestamp = (int)(dateformat.parse(year_s+"-01-01-01:01").getTime()/1000);
                            int index = bisection(datalist, timestamp);
                            recyclerView.scrollToPosition(index);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    });
                    threadPool.add_run(r);
                }
            }

            @Override
            public void filterlistener() {
//                onbind logic to deal with selected type
                mAdapter.notifyDataSetChanged();
            }
        });
        timelinefilter.show(getSupportFragmentManager(), "tag");
    }

    private int bisection(List<timeline_item> datalist, int dateGoal) {
        int total_length = datalist.size();
        if (total_length==1){
            return timelineViewModel.getTl_items().getValue().indexOf(datalist.get(0));
        }
        int mid_index = (int)(total_length/2);
        int mid_date = datalist.get(mid_index).getDate();
        if (dateGoal>mid_date){
            return bisection(datalist.subList(0, mid_index), dateGoal);
        }else{
            return bisection(datalist.subList(mid_index, total_length), dateGoal);
        }
    }

    public interface dialogListener{
        void jumppagelistener();
        void filterlistener();
    }

    public interface passVMtoRV{
        List<String> getfiltertypes();
    }
}