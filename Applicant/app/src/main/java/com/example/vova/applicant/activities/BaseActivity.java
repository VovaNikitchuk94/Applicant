package com.example.vova.applicant.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.vova.applicant.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public abstract class BaseActivity extends AppCompatActivity {


    public void drawerAndToolbar() {

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.set
        setSupportActionBar(toolbar);

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.favorite).withIcon(R.drawable.favorite_icon_24dp),
                        new PrimaryDrawerItem().withName(R.string.recommendations).withIcon(R.drawable.ic_priority_high_black_24dp),

                        new SectionDrawerItem().withName(R.string.settings),
                        new SecondaryDrawerItem().withName(R.string.settings).withIcon(R.drawable.ic_settings_black_24dp),
                        new SecondaryDrawerItem().withName(R.string.help).withIcon(R.drawable.ic_help_outline_black_24dp),

                        new SectionDrawerItem().withName(R.string.others),
                        new SecondaryDrawerItem().withName(R.string.share).withIcon(R.drawable.ic_share_black_24dp),
                        new SecondaryDrawerItem().withName(R.string.writeToUs).withIcon(R.drawable.ic_email_black_24dp),
                        new SecondaryDrawerItem().withName(R.string.estimate).withIcon(R.drawable.ic_star_black_24dp),
                        new SecondaryDrawerItem().withName(R.string.aboutProject).withIcon(R.drawable.ic_info_black_24dp)

                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        Intent intent;
                        switch (position){
                            case 8:
                                 intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "voviknik1994@gmail.com"));
                                startActivity(intent);
                                break;
//                            default:
//                                 intent = new Intent(view.getContext(), TopLevelActivity.class);
//                        startActivity(intent);
//                                break;
                        }
                        Log.d("My", "int position, IDrawerItem view.getId() ->" + view.getId());
                        Log.d("My", "int position, IDrawerItem position ->" + position);
                        Log.d("My", "int position, IDrawerItem drawerItem ->" + drawerItem);
//                        Intent intent = new Intent(view.getContext(), TopLevelActivity.class);
//                        startActivity(intent);
                        return false;
                    }
                })
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }
}
