package com.example.vova.applicant.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.FavoritePagerAdapter;
import com.example.vova.applicant.fragments.FavoriteCitiesFragment;
import com.example.vova.applicant.fragments.FavoriteSpecialitiesFragment;
import com.example.vova.applicant.fragments.FavoriteUniversityFragment;

public class FavoriteItemsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_items);
        Log.d("My", "FavoriteItemsActivity onCreate ->");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.di

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        FavoritePagerAdapter adapter = new FavoritePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FavoriteCitiesFragment(), getString(R.string.textCities));
        adapter.addFragment(new FavoriteUniversityFragment(), getString(R.string.textUniversities));
        adapter.addFragment(new FavoriteSpecialitiesFragment(), getString(R.string.textSpecialities));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
