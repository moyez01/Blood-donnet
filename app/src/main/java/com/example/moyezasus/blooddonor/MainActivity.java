package com.example.moyezasus.blooddonor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout searchDonor, bloodRequest, facts, postARequest, bloodBank, addDonor, myAccount, aboutApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchDonor = (LinearLayout) findViewById(R.id.searchDonor);
        bloodRequest = (LinearLayout) findViewById(R.id.bloodRequest);
        facts = (LinearLayout) findViewById(R.id.facts);
        postARequest = (LinearLayout) findViewById(R.id.postARequest);
        bloodBank = (LinearLayout) findViewById(R.id.bloodBank);
        addDonor = (LinearLayout) findViewById(R.id.aboutApp);
        myAccount = (LinearLayout) findViewById(R.id.addDonor);
        aboutApp = (LinearLayout) findViewById(R.id.myAccount);

        searchDonor.setOnClickListener((View.OnClickListener) this);
        bloodRequest.setOnClickListener((View.OnClickListener) this);
        facts.setOnClickListener((View.OnClickListener) this);
        postARequest.setOnClickListener((View.OnClickListener) this);
        bloodBank.setOnClickListener((View.OnClickListener) this);
        addDonor.setOnClickListener((View.OnClickListener) this);
        myAccount.setOnClickListener((View.OnClickListener) this);
        aboutApp.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        if(R.id.searchDonor == v.getId()){
            startActivity(new Intent(MainActivity.this, SearchDonor.class));
        }
        else if(R.id.bloodRequest == v.getId()){
            startActivity(new Intent(MainActivity.this, BloodRequest.class));
        }
        else if(R.id.facts == v.getId()){
            startActivity(new Intent(MainActivity.this, Facts.class));
        }
        else if(R.id.postARequest == v.getId()){
            startActivity(new Intent(MainActivity.this, PostARequest.class));
        }
        else if(R.id.bloodBank == v.getId()){
            startActivity(new Intent(MainActivity.this, BloodBank.class));
        }
        else if(R.id.addDonor == v.getId()){
            startActivity(new Intent(MainActivity.this, AddDonor.class));
        }
        else if(R.id.myAccount == v.getId()){
            startActivity(new Intent(MainActivity.this, UserLogin.class));
        }
        else if(R.id.aboutApp == v.getId()){
            startActivity(new Intent(MainActivity.this, AboutApp.class));
        }


    }
}
