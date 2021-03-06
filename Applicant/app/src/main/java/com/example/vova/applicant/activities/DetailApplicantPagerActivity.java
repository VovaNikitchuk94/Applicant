package com.example.vova.applicant.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.LegendAdapter;
import com.example.vova.applicant.fragments.DetailApplicantFragment;
import com.example.vova.applicant.fragments.ImportantInfoApplicantFragment;
import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.LegendInfo;
import com.example.vova.applicant.model.engines.ApplicationsEngine;
import com.example.vova.applicant.model.engines.LegendEngine;

import java.util.ArrayList;
import java.util.List;

public class DetailApplicantPagerActivity extends BaseActivity {

    public static final String INTENT_KEY_APPLICANT_INFO = "INTENT_KEY_APPLICANT_INFO";

    private static final int MENU_ITEM_LEGEND = 113;

    private BottomSheetBehavior bottomSheetBehavior;
    private ViewPager mViewPager;
    private List<ApplicationsInfo> mInfoList;
    public static ApplicationsInfo mInfo;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        FragmentManager manager = getSupportFragmentManager();

        // add new fragment
        Fragment fragment = new ImportantInfoApplicantFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragmentImportantInfoApplicant, fragment);
        transaction.commit();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mInfo = (ApplicationsInfo) bundle
                        .get(DetailApplicantPagerActivity.INTENT_KEY_APPLICANT_INFO);
            }
        }

        mViewPager = (ViewPager) findViewById(R.id.activity_detail_applicant_view_pager);
        final ApplicationsEngine applicationsEngine = new ApplicationsEngine(getApplication());
        mInfoList = applicationsEngine.getAllApplicationsById(mInfo.getLongSpecialityId());

        mViewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                return DetailApplicantFragment.newInstance(mInfoList.get(position).getId());
            }

            @Override
            public int getCount() {
                return mInfoList.size();
            }
        });

        for (int i = 0; i < mInfoList.size(); i++) {
            if (mInfoList.get(i).getId() == mInfo.getId()) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        //modified toolbar
        Drawable menuRightIconDrawable = ContextCompat.getDrawable(this, R.drawable.ic_more_vert_black_24dp);
        menuRightIconDrawable.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getToolbar().setOverflowIcon(menuRightIconDrawable);

        //set recyclerView with legend data
        setLegendList();
    }

    private void setLegendList() {
        Log.d("My", "setLegendList start - >" + true);

        RecyclerView legendRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewLegendInfoListActivity);
        LinearLayoutManager legendLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        legendRecyclerView.setLayoutManager(legendLayoutManager);
        DividerItemDecoration legendDividerItemDecoration = new DividerItemDecoration(legendRecyclerView.getContext(),
                legendLayoutManager.getOrientation());
        legendRecyclerView.addItemDecoration(legendDividerItemDecoration);

        // получение вью нижнего экрана
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        // настройка поведения нижнего экрана
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setHideable(true);

        LegendEngine legendEngine = new LegendEngine(getApplication());
        ArrayList<LegendInfo> legendInfos = legendEngine.getLegendsById(mInfo.getLongSpecialityId());
        LegendAdapter legendAdapter = new LegendAdapter(legendInfos);
        legendRecyclerView.setAdapter(legendAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_applicant_pager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem itemHelp = menu.add(3, MENU_ITEM_LEGEND,3, R.string.textLegend);
        itemHelp.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case MENU_ITEM_LEGEND:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Log.d("My", " if bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Log.d("My", " else bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
                }

                Log.d("My", " bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
