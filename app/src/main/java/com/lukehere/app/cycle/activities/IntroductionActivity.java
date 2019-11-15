package com.lukehere.app.cycle.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.lukehere.app.cycle.R;

import androidx.appcompat.app.AppCompatActivity;

import static com.lukehere.app.cycle.activities.SplashActivity.FIRST_OPEN;
import static com.lukehere.app.cycle.activities.SplashActivity.PREFERENCE_FILE;

public class IntroductionActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        final SharedPreferences sharedpreferences = getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);

        MaterialButton mStartButton = findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(FIRST_OPEN, 1);
                editor.apply();

                Intent i = new Intent(IntroductionActivity.this, AuthenticationActivity.class);
                startActivity(i);

                finish();
            }
        });
    }
}
