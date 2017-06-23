package com.example.vova.applicant.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vova.applicant.R;

public class TopLevelActivity extends BaseActivity implements View.OnClickListener {

    public static final String HTTP_WWW_VSTUP_INFO_2016 = "http://www.vstup.info/2016/";
    public static final String HTTP_WWW_VSTUP_INFO_2015 = "http://www.vstup.info/2015/";

    private Button mButton2017Year;
    private Button mButton2016Year;
    private Button mButton2015Year;

    public String yearsCodeLink = "";

    @Override
    protected void initActivity() {
        initializeView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_top_level;
    }

    private void initializeView() {
        mButton2017Year = (Button) findViewById(R.id.button2017YearTopLevelActivity);
        mButton2016Year = (Button) findViewById(R.id.button2016YearTopLevelActivity);
        mButton2015Year = (Button) findViewById(R.id.button2015YearTopLevelActivity);

        mButton2017Year.setOnClickListener(this);
        mButton2016Year.setOnClickListener(this);
        mButton2015Year.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.button2017YearTopLevelActivity:
                //TODO fix when can opened 2017vstupInfoPage
                intent = new Intent(this, Applicant2017Activity.class);
                yearsCodeLink = "";
                break;

            case R.id.button2016YearTopLevelActivity:
                yearsCodeLink = HTTP_WWW_VSTUP_INFO_2016;
                 intent = new Intent(this, CitiesListActivity.class);
                intent.putExtra(CitiesListActivity.KEY_YEARS_CITIES_LIST_ACTIVITY, yearsCodeLink);
                break;

            case R.id.button2015YearTopLevelActivity:
                yearsCodeLink = HTTP_WWW_VSTUP_INFO_2015;
                 intent = new Intent(this, CitiesListActivity.class);
                intent.putExtra(CitiesListActivity.KEY_YEARS_CITIES_LIST_ACTIVITY, yearsCodeLink);

                break;
        }
        if (intent != null){
            startActivity(intent);
        } else {
            Toast.makeText(getApplication(), "Error ", Toast.LENGTH_SHORT).show();
        }

    }
}
