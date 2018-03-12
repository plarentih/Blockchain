package com.example.plarent.blockchain.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.adapter.SwipePagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SwipeActivity extends AppCompatActivity {

    private SwipePagerAdapter swipePagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        swipePagerAdapter = new SwipePagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(swipePagerAdapter);

        Button continueBtn = findViewById(R.id.fab);
        Button skipBtn = findViewById(R.id.skipBtn);

        TabLayout tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mViewPager, true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            continueBtn.setVisibility(View.GONE);
        }else {
            skipBtn.setVisibility(View.GONE);
        }

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StartingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PhoneAuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
