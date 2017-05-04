package com.example.vova.applicant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vova.applicant.R;

public class TopLevelActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String HTTP_WWW_VSTUP_INFO_2016 = "http://www.vstup.info/2016/";
    public static final String HTTP_WWW_VSTUP_INFO_2015 = "http://www.vstup.info/2015/";
    private EditText mSearchEditText;

    private Button mButton2017Year;
    private Button mButton2016Year;
    private Button mButton2015Year;

    public String yearsCodeLink = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        Log.d("OnCreate", "TopLevelActivity -> OnCreate");

        initializeView();
    }

    private void initializeView() {
        mSearchEditText = (EditText)findViewById(R.id.editTextSearchTopLevelActivity);
        mSearchEditText.setEnabled(false);

        mButton2017Year = (Button) findViewById(R.id.button2017YearTopLevelActivity);
        mButton2016Year = (Button) findViewById(R.id.button2016YearTopLevelActivity);
        mButton2015Year = (Button) findViewById(R.id.button2015YearTopLevelActivity);

        mButton2017Year.setOnClickListener(this);
        mButton2017Year.setEnabled(false);

        mButton2016Year.setOnClickListener(this);
        mButton2015Year.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button2017YearTopLevelActivity:
                yearsCodeLink = "";
                break;
            case R.id.button2016YearTopLevelActivity:
                yearsCodeLink = HTTP_WWW_VSTUP_INFO_2016;
                break;
            case R.id.button2015YearTopLevelActivity:
                yearsCodeLink = HTTP_WWW_VSTUP_INFO_2015;
                break;
        }

        if (("".equals(yearsCodeLink))){
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, CitiesListActivity.class);
            intent.putExtra(CitiesListActivity.KEY_YEARS_CITIES_LIST_ACTIVITY, yearsCodeLink);
            startActivity(intent);
        }
    }
}
