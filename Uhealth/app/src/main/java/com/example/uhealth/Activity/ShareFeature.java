package com.example.uhealth.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.uhealth.Adapters.FPAdapter_Share;
import com.example.uhealth.DataModel.Share_outstandings_item;
import com.example.uhealth.Fragments.ShareRequest_Frag;
import com.example.uhealth.Fragments.ShareView_frag;
import com.example.uhealth.Interfaces.DataloadedListener;
import com.example.uhealth.Interfaces.ShareDataLoadedListener;
import com.example.uhealth.R;
import com.example.uhealth.ViewModel.Share_ViewModel;
import com.example.uhealth.ViewModelFactory.ShareViewModelFactory;
import com.example.uhealth.utils.FireBaseInfo;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class ShareFeature extends AppCompatActivity {
    public static final String TAG = ShareFeature.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FPAdapter_Share fragmentPagerAdapter;
    private FireBaseInfo mFireBaseInfo;
    private Share_ViewModel viewModel;

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
                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.sf_viewpager + ":" + viewPager.getCurrentItem());
                ShareRequest_Frag mFragRequest = (ShareRequest_Frag) page;
                viewModel.getOutstandings().observe(ShareFeature.this, new Observer<List<Share_outstandings_item>>() {
                    @Override
                    public void onChanged(List<Share_outstandings_item> share_outstandings_items) {
                        if (mFragRequest!=null){
                            mFragRequest.updateadapter();
                            Toast.makeText(ShareFeature.this, "Outstandings reloaded", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            @Override
            public void onAcceptedLoaded() {
//                todo implement call from fragment
            }
        });
        viewModel = new ViewModelProvider(this, factory).get(Share_ViewModel.class);
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
}