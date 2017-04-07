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

        Log.d("OnCreate", "UniversityPageActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){

                universityName = bundle.getString(KEY_UNIVERSITY_TITLE);
                universityLink = bundle.getString(KEY_UNIVERSITY_LINK);
            }
        }

        initializeView();
    }

    private void initializeView() {
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
            // по нажатию на кнопку переход на FullTimeFormPageActivity и передача ссылки и имени университета
            case R.id.buttonFullTimeFormUniversityPageActivity:
                intent = new Intent(this, FullTimeFormPageActivity.class);
                intent.putExtra(FullTimeFormPageActivity.KEY_DEGREE_TITLE,
                        universityName);
                intent.putExtra(FullTimeFormPageActivity.KEY_DEGREE_LINK,
                        universityLink);
                Log.d("My", "UniversityPageActivity -> onClick -> universityLink" + universityLink);
                Log.d("My", "UniversityPageActivity -> onClick -> universityName" + universityName);
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
