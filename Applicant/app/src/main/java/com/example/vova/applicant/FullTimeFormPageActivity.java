package com.example.vova.applicant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FullTimeFormPageActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String KEY_DEGREE_TITLE = "KEY_DEGREE_TITLE";
    public static final String KEY_DEGREE_LINK = "KEY_DEGREE_LINK";

    private static final String KEY_BACHELOR = "#d_o_1";
    private static final String KEY_SPECIALIST = "#d_o_3";
    private static final String KEY_MASTER = "#d_o_2";


    private TextView mTextViewDegree;

    private Button mButtonBachelor;
    private Button mButtonMaster;
    private Button mButtonSpecialist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_time_form_page);

        mTextViewDegree = (TextView)findViewById(R.id.textViewChooseDegreeFullTimeFormPageActivity);
        //TODO setText

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
                break;
            case R.id.buttonSpecialistDegreeFullTimeFormPageActivity:
                intent = new Intent(this, MasterListActivity.class);
                break;
            case R.id.buttonMasterDegreeFullTimeFormPageActivity:
                intent = new Intent(this, SpecialistListActivity.class);
                break;
        }

        startActivity(intent);

    }
}
