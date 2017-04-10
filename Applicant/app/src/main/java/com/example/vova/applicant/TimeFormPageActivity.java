package com.example.vova.applicant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TimeFormPageActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String KEY_DEGREE_TITLE = "KEY_DEGREE_TITLE";
    public static final String KEY_DEGREE_LINK = "KEY_DEGREE_LINK";

    public static final int INT_FULL_TIME_FORM = 1;
    public static final int INT_EXTERNAL_FORM = 2;

    public static final String KEY_TIME_FORM = "KEY_TIME_FORM";

    private static final String KEY_BACHELOR_FULL_TIME = "#d_o_1";
    private static final String KEY_SPECIALIST_FULL_TIME = "#d_o_3";
    private static final String KEY_MASTER_FULL_TIME = "#d_o_2";
    private static final String KEY_JUNIOR_SPECIALIST_FULL_TIME = "#d_o_4";

    private static final String KEY_BACHELOR_EXTERNAL = "#z_o_1";
    private static final String KEY_SPECIALIST_EXTERNAL = "#z_o_3";
    private static final String KEY_JUNIOR_SPECIALIST_EXTERNAL = "#z_o_4";
    private static final String KEY_MASTER_EXTERNAL = "#z_o_2";

    private TextView mTextViewDegree;

    private Button mButtonBachelor;
    private Button mButtonMaster;
    private Button mButtonSpecialist;
    private Button mButtonJuniorSpecialist;

    private String mStrFullTimeName;
    private String mStrFullTimeLink;

    private int mIntTimeForm = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_form_page);

        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){
                mStrFullTimeName = bundle.getString(KEY_DEGREE_TITLE);
                mStrFullTimeLink = bundle.getString(KEY_DEGREE_LINK);
                mIntTimeForm = bundle.getInt(KEY_TIME_FORM);
            }
        }

        initializeView();
    }

    private void initializeView() {
        mTextViewDegree = (TextView)findViewById(R.id.textViewChooseDegreeTimeFormPageActivity);
        //TODO setText
        mTextViewDegree.setText(getText(R.string.textChooseDegreeTimeFormPageActivity));

        mButtonBachelor = (Button)findViewById(R.id.buttonBachelorDegreeTimeFormPageActivity);
        mButtonSpecialist = (Button)findViewById(R.id.buttonSpecialistDegreeTimeFormPageActivity);
        mButtonSpecialist = (Button)findViewById(R.id.buttonJuniorSpecialistDegreeTimeFormPageActivity);
        mButtonMaster = (Button)findViewById(R.id.buttonMasterDegreeTimeFormPageActivity);

        mButtonBachelor.setOnClickListener(this);
        mButtonSpecialist.setOnClickListener(this);
        mButtonMaster.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = null;

        switch (view.getId()){

            case R.id.buttonBachelorDegreeTimeFormPageActivity:
                intent = new Intent(this, SpecialtiesListActivity.class);
                intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_TITLE, mStrFullTimeName);
                intent.putExtra(SpecialtiesListActivity.KEY_DEGREE, 5);
                if (mIntTimeForm == 1){
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, mStrFullTimeLink
                            + KEY_BACHELOR_FULL_TIME);
                }else if (mIntTimeForm == INT_EXTERNAL_FORM){
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, mStrFullTimeLink
                            + KEY_BACHELOR_EXTERNAL);
                } else {
                    Toast.makeText(TimeFormPageActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
                Log.d("My", "TimeFormPageActivity -> onClick ->  mStrFullTimeLink \n" +
                        " + KEY_BACHELOR_FULL_TIME -> " +  mStrFullTimeLink + KEY_BACHELOR_FULL_TIME);
                Log.d("My", "TimeFormPageActivity -> onClick ->  mStrFullTimeName \n" +
                        " + KEY_BACHELOR_FULL_TIME -> " +  mStrFullTimeName + KEY_BACHELOR_FULL_TIME);
                break;
            case R.id.buttonSpecialistDegreeTimeFormPageActivity:
                intent = new Intent(this, SpecialtiesListActivity.class);
                intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_TITLE, mStrFullTimeName);
                intent.putExtra(SpecialtiesListActivity.KEY_DEGREE, 6);
                if (mIntTimeForm == INT_FULL_TIME_FORM){
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, mStrFullTimeLink
                            + KEY_SPECIALIST_FULL_TIME);
                }else if (mIntTimeForm == INT_EXTERNAL_FORM){
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, mStrFullTimeLink
                            + KEY_SPECIALIST_EXTERNAL);
                } else {
                    Toast.makeText(TimeFormPageActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonJuniorSpecialistDegreeTimeFormPageActivity:
                intent = new Intent(this, SpecialtiesListActivity.class);
                intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_TITLE, mStrFullTimeName);
                intent.putExtra(SpecialtiesListActivity.KEY_DEGREE, 8);
                if (mIntTimeForm == INT_FULL_TIME_FORM){
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, mStrFullTimeLink
                            + KEY_JUNIOR_SPECIALIST_FULL_TIME);
                }else if (mIntTimeForm == INT_EXTERNAL_FORM){
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, mStrFullTimeLink
                            + KEY_JUNIOR_SPECIALIST_EXTERNAL);
                } else {
                    Toast.makeText(TimeFormPageActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonMasterDegreeTimeFormPageActivity:
                intent = new Intent(this, SpecialtiesListActivity.class);
                intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_TITLE, mStrFullTimeName);
                intent.putExtra(SpecialtiesListActivity.KEY_DEGREE, 7);
                if (mIntTimeForm == INT_FULL_TIME_FORM){
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, mStrFullTimeLink
                            + KEY_MASTER_FULL_TIME);
                } else if (mIntTimeForm == INT_EXTERNAL_FORM){
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, mStrFullTimeLink
                            + KEY_MASTER_EXTERNAL);
                } else {
                    Toast.makeText(TimeFormPageActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
                break;
        }

        startActivity(intent);

    }
}
