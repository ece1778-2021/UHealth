package com.example.uhealth.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.uhealth.Adapters.FPAdapter_Share;
import com.example.uhealth.DataModel.Share_accepted_item;
import com.example.uhealth.DataModel.Share_outstandings_item;
import com.example.uhealth.Fragments.Outstanding_accept_Dfrag;
import com.example.uhealth.Fragments.ShareRequest_Frag;
import com.example.uhealth.Fragments.ShareView_frag;
import com.example.uhealth.Interfaces.DataloadedListener;
import com.example.uhealth.Interfaces.ShareDataLoadedListener;
import com.example.uhealth.R;
import com.example.uhealth.ViewModel.Share_ViewModel;
import com.example.uhealth.ViewModelFactory.ShareViewModelFactory;
import com.example.uhealth.utils.FireBaseInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;

public class ShareFeature extends AppCompatActivity {
    public static final String TAG = ShareFeature.class.getSimpleName();
    private final String OUTSTANDINGS = "Outstanding_Requests";
    private final String ACCEPTED = "Accepted_Requests";
    private final String INFOHISTORY = "";
    private final String DIAGNOSIS = "";
    private final String APPOINTMENTS = "AApointment";
//    in s
    public static final long OUTSTANDING_EXPIRE_TIME = 60;
    public static final long ACCEPTED_EXPIRE_TIME = 24*3600;
//    in ms
    public static final int LOOP_INTERVAL = 30*1000;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FPAdapter_Share fragmentPagerAdapter;
    private FireBaseInfo mFireBaseInfo;
    private Share_ViewModel viewModel;

    private Handler mHandler;
    private Runnable mLoopcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_feature);

        getSupportActionBar().setTitle("Sharing Feature");
        mFireBaseInfo = new FireBaseInfo();

        initView();
        initViewPager();

        ShareViewModelFactory factory = new ShareViewModelFactory(new ShareDataLoadedListener() {
            @Override
            public void onOutstandingsLoaded() {
                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.sf_viewpager + ":" + 0);
                ShareRequest_Frag mFragRequest = (ShareRequest_Frag) page;
                viewModel.getOutstandings().observe(ShareFeature.this, new Observer<List<Share_outstandings_item>>() {
                    @Override
                    public void onChanged(List<Share_outstandings_item> share_outstandings_items) {
                        if (mFragRequest!=null){
                            mFragRequest.updateadapter();
//                            Toast.makeText(ShareFeature.this, "Outstandings reloaded", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            @Override
            public void onAcceptedLoaded() {
                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.sf_viewpager + ":" + 1);
                ShareView_frag mFragView = (ShareView_frag) page;
                viewModel.getAccepteds().observe(ShareFeature.this, new Observer<List<Share_accepted_item>>() {
                    @Override
                    public void onChanged(List<Share_accepted_item> share_accepted_items) {
                        if (mFragView!=null){
                            mFragView.updateAdapter();
//                            Toast.makeText(ShareFeature.this, "Accepted reloaded", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        viewModel = new ViewModelProvider(this, factory).get(Share_ViewModel.class);

        initHandler();
    }

    private void initHandler() {
//        use of handler is justified since firebase is already an Async task, check work is light
        mHandler = new Handler(Looper.getMainLooper());
        mLoopcheck = new Runnable() {
            @Override
            public void run() {
                long cur_time = System.currentTimeMillis()/1000;
//                both are oldest first
                if (viewModel!=null){
                    for(Share_outstandings_item item :viewModel.getOutstandings().getValue()){
                        if (item.getExpire()<cur_time){
                            String id = item.getDocumentId();
                            ShareFeature.this.deleteOutstandingdoc(id);
                        }else{
                            break;
                        }
                    }
                    for(Share_accepted_item item: viewModel.getAccepteds().getValue()){
                        if (item.getExpire()<cur_time){
                            String id = item.getDocumentId();
                            ShareFeature.this.deleteAccepteddoc(id);
                        }else{
                            break;
                        }
                    }
                }
                Log.d(TAG, "One loop of expire check");
                mHandler.postDelayed(this, LOOP_INTERVAL);
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.post(mLoopcheck);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mLoopcheck);
    }

    private void initViewPager() {
        fragmentPagerAdapter = new FPAdapter_Share(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        fragmentPagerAdapter.addFragments(ShareRequest_Frag.newInstance(), "Request");
        fragmentPagerAdapter.addFragments(ShareView_frag.newInstance(), "View");

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        tabLayout = findViewById(R.id.sf_tablayout);
        viewPager = findViewById(R.id.sf_viewpager);
    }

    public void deleteOutstandingdoc(String id){
        mFireBaseInfo.mFirestore.collection(OUTSTANDINGS).document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Delete outstanding success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShareFeature.this, "Delete Outstanding Failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Delete outstanding failed");
            }
        });
    }

    public void deleteAccepteddoc(String id){
        mFireBaseInfo.mFirestore.collection(ACCEPTED).document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Delete Accepted success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShareFeature.this, "Delete Accepted Failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Delete Accepted failed");
            }
        });
    }

    public void openOutstandingAcceptDialog(Share_outstandings_item in_item){
        viewModel.setOutstandingAcceptDialogItem(in_item);
        Outstanding_accept_Dfrag outstandingAcceptDfrag = Outstanding_accept_Dfrag.newInstance();
        outstandingAcceptDfrag.show(getSupportFragmentManager(), "tag");
    }



    //    todo this thing
    public void acceptOutstandings(HashMap<String, Boolean> b_map){
//        item is saved into viewmodel, thus even if outstanding is deleted, the dialog can still generate desired result
        Share_outstandings_item item = viewModel.getOutstandingAcceptDialogItem();
        HashMap<String, Object> map = new HashMap<>();
        map.put("IdKey", item.getIdKey());
        map.put("expire", System.currentTimeMillis()/1000+ACCEPTED_EXPIRE_TIME);
        map.put("from_Id", item.getFrom_id());
        map.put("from_email", item.getFrom_email());
        map.put("from_username", item.getFrom_username());
        map.put("to_Id", item.getTo_id());
        map.put("to_email", item.getTo_email());
        map.put("to_username", item.getTo_username());

        final boolean mInfoandHistory, mDiagnosis, mTimeline;
        final String outstandingdocid = item.getDocumentId();
        mInfoandHistory = b_map.get("InfoandHistory");
        mDiagnosis = b_map.get("Diagnosis");
        mTimeline = b_map.get("Timeline");

        map.put("InfoandHistory", mInfoandHistory);
        map.put("Diagnosis", mDiagnosis);
        map.put("Timeline", mTimeline);


        mFireBaseInfo.mFirestore.collection(ACCEPTED)
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
//                        todo copy collections into this docid
                        String docid = documentReference.getId();

//                        todo combined task and when all complete (get and delete) then nest combined task to upload into************************
                        if (mInfoandHistory){
//                            todo copy collection over
                        }
                        if (mDiagnosis){
//                            todo copy colletion over (not support yet)
                        }
                        if(mTimeline){
//                            todo copy collection over (not support yet)
                        }
                        mFireBaseInfo.mFirestore.collection(OUTSTANDINGS).document(outstandingdocid)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "delete old outstanding succeeded");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "delete old outstanding failed");
                                        Toast.makeText(ShareFeature.this, "Delete old outstanding failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
//                        todo**************************************************************
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShareFeature.this, "Accept Instance failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Create accept instance failed");
                    }
                });

    }

//    todo this one
    public void viewFromAccepteds(String rbText, Share_accepted_item item){
//        todo switch rbText choose collection within docid.
//        todo launch activities from here
        Toast.makeText(this, "Show: "+rbText+"\t"+item.getTo_username(), Toast.LENGTH_SHORT).show();
    }
}