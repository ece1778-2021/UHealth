package com.example.uhealth.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.uhealth.Adapters.FPAdapter_Share;
import com.example.uhealth.Fragments.ShareRequest_Frag;
import com.example.uhealth.Fragments.ShareView_frag;
import com.example.uhealth.R;
import com.google.android.material.tabs.TabLayout;

public class ShareFeature extends AppCompatActivity {
    public static final String TAG = ShareFeature.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FPAdapter_Share fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_feature);

        getSupportActionBar().setTitle("Sharing Feature");
        initView();
        initViewPager();
    }

    private void initViewPager() {
        fragmentPagerAdapter = new FPAdapter_Share(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //        todo create two fragments and add to adapter, access viewmodel in each
//        todo set up back running thread

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