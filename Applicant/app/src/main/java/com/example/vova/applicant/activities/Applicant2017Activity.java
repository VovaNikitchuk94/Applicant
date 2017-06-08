package com.example.vova.applicant.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vova.applicant.R;

public class Applicant2017Activity extends BaseActivity {

    private static final int ITEM_ID_APPLICANT_INFO = 111;
    public static final String DRIVE_GOOGLE_COM_OPEN_IMPORTANT_INFO_2017 =
            "https://drive.google.com/open?id=0B4__5KtwLylAazV3TEtmWmNYMjQ";

    @Override
    protected void iniActivity() {

        TextView textView = (TextView) findViewById(R.id.textViewApplication2017Activity);
        textView.setText(R.string.textImportantInfoForApplicant2017);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //set applicantItem icon
        MenuItem itemApplicantInfo = menu.add(0, ITEM_ID_APPLICANT_INFO, 0, R.string.textInfoForApplicant);
        Drawable infoDrawable = ContextCompat.getDrawable(this, R.drawable.ic_info_black_24dp);
        infoDrawable.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        itemApplicantInfo.setIcon(infoDrawable);
        itemApplicantInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case ITEM_ID_APPLICANT_INFO:
                //intent for open pdf in new task
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(DRIVE_GOOGLE_COM_OPEN_IMPORTANT_INFO_2017));
//                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_application_2017;
    }
}
