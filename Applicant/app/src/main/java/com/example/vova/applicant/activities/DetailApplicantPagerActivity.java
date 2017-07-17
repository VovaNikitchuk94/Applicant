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

    private static final int ITEM_ID_APPLICANT_INFO = 112;
    private static final int MENU_ITEM_LEGEND = 113;

    private BottomSheetBehavior bottomSheetBehavior;
    private ViewPager mViewPager;
    private List<ApplicationsInfo> mInfoList;
    public static ApplicationsInfo mInfo;

    @Override
    protected void initActivity() {
        FragmentManager manager = getSupportFragmentManager();

        // add new fragment
        Fragment fragment = new ImportantInfoApplicantFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragmentImportantInfoApplicant, fragment);
        Log.d("My", "transaction.add ->");
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
        Log.d("My", "mInfo.getLongSpecialityId() ->" + mInfo.getStrApplicantName());
        Log.d("My", "mInfo.getLongSpecialityId() ->" + mInfo.getLongSpecialityId());
        Log.d("My", "mInfo.applicationsEngine.getAllApplicationsById(mInfo.getId())() ->" + applicationsEngine.getAllApplicationsById(mInfo.getId()).size());
        Log.d("My", "mInfo.applicationsEngine.getAllApplicationsById(mInfo.getLongSpecialityId())() ->" + applicationsEngine.getAllApplicationsById(mInfo.getLongSpecialityId()).size());


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
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // настройка возможности скрыть элемент при свайпе вниз
        bottomSheetBehavior.setHideable(true);

        LegendEngine legendEngine = new LegendEngine(getApplication());
//        mLongSpecialityId = mSpecialtiesInfo.getId();
        ArrayList<LegendInfo> legendInfos = legendEngine.getLegendsById(mInfo.getLongSpecialityId());
        Log.d("My", "setLegendList start legendInfos size- >" + legendInfos.size());
        LegendAdapter legendAdapter = new LegendAdapter(legendInfos);
        legendRecyclerView.setAdapter(legendAdapter);
//        legendAdapter.notifyDataSetChanged();

        Log.d("My", "setLegendList  bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_applicant_pager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //set applicantItem icon
        MenuItem itemApplicantInfo = menu.add(0, ITEM_ID_APPLICANT_INFO, 0, R.string.textInfoForApplicant);
        Drawable infoDrawable = ContextCompat.getDrawable(this, R.drawable.ic_info_black_24dp);
        infoDrawable.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        itemApplicantInfo.setIcon(infoDrawable);
        itemApplicantInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem itemHelp = menu.add(3, MENU_ITEM_LEGEND,3, R.string.textLegend);
        itemHelp.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case ITEM_ID_APPLICANT_INFO:
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("https://drive.google.com/open?id=0B4__5KtwLylAazV3TEtmWmNYMjQ"));
//                startActivity(browserIntent);

                Toast.makeText(this, "applicant info selected", Toast.LENGTH_SHORT).show();
                break;
            case MENU_ITEM_LEGEND:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Log.d("My", " if bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Log.d("My", " else bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
                }

                Log.d("My", " bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
                Toast.makeText(this, "MENU_ITEM_LEGEND selected", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
