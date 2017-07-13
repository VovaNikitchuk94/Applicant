package com.example.vova.applicant.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.toolsAndConstans.Constans;

public class ArchiveActivity extends BaseActivity implements View.OnClickListener {

    private Button mButton2016Year;
    private Button mButton2015Year;

    private String yearsCodeLink = "";

    @Override
    protected void initActivity() {
        initializeView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_archive;
    }

    private void initializeView() {
        mButton2016Year = (Button) findViewById(R.id.button2016YearTopLevelActivity);
        mButton2015Year = (Button) findViewById(R.id.button2015YearTopLevelActivity);

        mButton2016Year.setOnClickListener(this);
        mButton2015Year.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.button2016YearTopLevelActivity:
                yearsCodeLink = Constans.URL_VSTUP_INFO_2016;
                 intent = new Intent(this, CitiesListActivity.class);
                intent.putExtra(CitiesListActivity.KEY_YEARS_CITIES_LIST_ACTIVITY, yearsCodeLink);
                break;

            case R.id.button2015YearTopLevelActivity:
                yearsCodeLink = Constans.URL_VSTUP_INFO_2015;
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
