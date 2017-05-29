package com.example.vova.applicant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.vova.applicant.R;
import com.example.vova.applicant.fragments.DetailApplicantFragment;
import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.engines.ApplicationInfoEngine;

import java.util.List;

public class DetailApplicantPagerActivity extends BaseActivity {

    public static final String INTENT_KEY_APPLICANT_INFO = "INTENT_KEY_APPLICANT_INFO";

    private ViewPager mViewPager;
    private List<ApplicationsInfo> mInfoList;
    private ApplicationsInfo mInfo;

    @Override
    protected void iniActivity() {

        Intent intent = getIntent();
        if (intent != null){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                mInfo = (ApplicationsInfo) bundle
                        .get(DetailApplicantPagerActivity.INTENT_KEY_APPLICANT_INFO);
            }
        }

        mViewPager = (ViewPager) findViewById(R.id.activity_detail_applicant_view_pager);
        final ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
        mInfoList = applicationInfoEngine.getAllApplicantionsById(mInfo.getLongSpecialityId());
        Log.d("My", "mInfo.getLongSpecialityId() ->" + mInfo.getLongSpecialityId());

        FragmentManager manager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                return DetailApplicantFragment.newInstance(mInfoList.get(position).getId());
            }

            @Override
            public int getCount() {
                Log.d("My", "mInfoList.size() ->" + mInfoList.size());
                return mInfoList.size();
            }
        });

        for (int i = 0; i < mInfoList.size(); i++) {
            if (mInfoList.get(i).getId() == mInfo.getId()) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_applicant_pager;
    }

    @Override
    public void setDrawer() {
        super.setDrawer();
    }
}
