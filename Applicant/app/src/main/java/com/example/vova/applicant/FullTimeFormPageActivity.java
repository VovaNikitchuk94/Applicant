package com.example.vova.applicant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FullTimeFormPageActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String KEY_DEGREE_TITLE = "KEY_DEGREE_TITLE";
    public static final String KEY_DEGREE_LINK = "KEY_DEGREE_LINK";

    private static final String KEY_BACHELOR_FULL_TIME = "#d_o_1";
    private static final String KEY_SPECIALIST_FULL_TIME = "#d_o_3";
    private static final String KEY_MASTER_FULL_TIME = "#d_o_2";

    private TextView mTextViewDegree;

    private Button mButtonBachelor;
    private Button mButtonMaster;
    private Button mButtonSpecialist;

    private String mStrFullTimeName;
    private String mStrFullTimeLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_time_form_page);

        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){
                mStrFullTimeName = bundle.getString(KEY_DEGREE_TITLE);
                mStrFullTimeLink = bundle.getString(KEY_DEGREE_LINK);
            }
        }

        initializeView();
    }

    private void initializeView() {
        mTextViewDegree = (TextView)findViewById(R.id.textViewChooseDegreeFullTimeFormPageActivity);
        //TODO setText
        mTextViewDegree.setText(getText(R.string.textChooseDegreeFullTimeFormPageActivity));

        mButtonBachelor = (Button)findViewById(R.id.buttonBachelorDegreeFullTimeFormPageActivity);
        mButtonSpecialist = (Button)findViewById(R.id.buttonSpecialistDegreeFullTimeFormPageActivity);
        mButtonMaster = (Button)findViewById(R.id.buttonMasterDegreeFullTimeFormPageActivity);

        mButtonBachelor.setOnClickListener(this);
        mButtonSpecialist.setOnClickListener(this);
        mButtonMaster.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = null;

        switch (view.getId()){

            case R.id.buttonBachelorDegreeFullTimeFormPageActivity:
                intent = new Intent(this, BachelorListActivity.class);
                intent.putExtra(BachelorListActivity.KEY_BACHELOR_TITLE, mStrFullTimeName);
                intent.putExtra(BachelorListActivity.KEY_BACHELOR_LINK, mStrFullTimeLink
                        + KEY_BACHELOR_FULL_TIME);
                Log.d("My", "FullTimeFormPageActivity -> onClick ->  mStrFullTimeLink \n" +
                        " + KEY_BACHELOR_FULL_TIME" +  mStrFullTimeLink + KEY_BACHELOR_FULL_TIME);
                Log.d("My", "FullTimeFormPageActivity -> onClick ->  mStrFullTimeName \n" +
                        " + KEY_BACHELOR_FULL_TIME" +  mStrFullTimeName + KEY_BACHELOR_FULL_TIME);
                break;
            case R.id.buttonSpecialistDegreeFullTimeFormPageActivity:
                intent = new Intent(this, SpecialistListActivity.class);
                intent.putExtra(SpecialistListActivity.KEY_SPECIALIST_TITLE, mStrFullTimeName);
                intent.putExtra(SpecialistListActivity.KEY_SPECIALIST_LINK, mStrFullTimeLink
                        + KEY_SPECIALIST_FULL_TIME);
                break;
            case R.id.buttonMasterDegreeFullTimeFormPageActivity:
                intent = new Intent(this, MasterListActivity.class);
                intent.putExtra(MasterListActivity.KEY_MASTER_TITLE, mStrFullTimeName);
                intent.putExtra(MasterListActivity.KEY_MASTER_LINK, mStrFullTimeLink
                        + KEY_MASTER_FULL_TIME);
                break;
        }

        startActivity(intent);

    }
}
