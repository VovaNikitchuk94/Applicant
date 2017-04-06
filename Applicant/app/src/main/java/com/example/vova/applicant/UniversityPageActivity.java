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

public class UniversityPageActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String KEY_UNIVERSITY_TITLE = "KEY_UNIVERSITY_TITLE";
    public static final String KEY_UNIVERSITY_LINK = "KEY_UNIVERSITY_LINK";

    private TextView mNameOfUniversity;

    private Button mButtonAboutUniversity;
    private Button mButtonFullTimeForm;
    private Button mButtonExternalForm;

    private String universityName;
    private String universityLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_page);

        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){

                universityName = bundle.getString(KEY_UNIVERSITY_TITLE);
                universityLink = bundle.getString(KEY_UNIVERSITY_LINK);
            }
        }

        initialView();
    }

    private void initialView() {
        mNameOfUniversity = (TextView)findViewById(R.id.textViewNameOfUniversityPageActivity);
        mNameOfUniversity.setText(universityName);

        mButtonAboutUniversity = (Button)findViewById(R.id.buttonAboutUniversityPageActivity);
        mButtonFullTimeForm = (Button)findViewById(R.id.buttonFullTimeFormUniversityPageActivity);
        mButtonExternalForm = (Button)findViewById(R.id.buttonExternalFormUniversityPageActivity);

        mButtonAboutUniversity.setOnClickListener(this);
        mButtonFullTimeForm.setOnClickListener(this);
        mButtonExternalForm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = null;
        switch (view.getId()){

            case R.id.buttonAboutUniversityPageActivity:
                intent = new Intent(this, AboutUniversityActivity.class);
                intent.putExtra(AboutUniversityActivity.KEY_ABOUT_UNIVERSITY_ACTIVITY,
                        universityLink);
                Log.d("My", "UniversityPageActivity -> onClick -> universityLink" + universityLink);
                break;
            case R.id.buttonFullTimeFormUniversityPageActivity:
                intent = new Intent(this, FullTimeFormListActivity.class);
                intent.putExtra(FullTimeFormListActivity.KEY_ABOUT_FULL_TIME_FORM,
                        universityLink);
                Log.d("My", "UniversityPageActivity -> onClick -> universityLink" + universityLink);
                break;
            case R.id.buttonExternalFormUniversityPageActivity:
                intent = new Intent(this, ExternalFormListActivity.class);
                intent.putExtra(ExternalFormListActivity.KEY_ABOUT_EXTERNAL_FORM,
                        universityLink);
                Log.d("My", "UniversityPageActivity -> onClick -> universityLink" + universityLink);
                break;
        }

        if (intent != null){
            startActivity(intent);
        }else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }


    }
}
